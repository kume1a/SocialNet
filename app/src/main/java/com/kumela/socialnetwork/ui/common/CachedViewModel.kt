package com.kumela.socialnetwork.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kumela.socialnetwork.network.api.PaginatedResponse
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.common.fold
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

/**
 * Created by Toko on 18,January,2021
 **/

abstract class CachedViewModel : ViewModel() {
    protected val cache: ConcurrentHashMap<KClass<*>, Any> = ConcurrentHashMap()
    protected val dataPages: ConcurrentHashMap<KClass<*>, Int> = ConcurrentHashMap()

    protected suspend inline fun <reified T : Any, F> getOrFetch(
        crossinline call: suspend () -> Result<T, F>
    ): Result<T, F> {
        val k = T::class
        var res: Result<T, F>? = null
        if (!cache.containsKey(k)) {
            withContext(viewModelScope.coroutineContext) {
                val result = call.invoke()
                result.fold(
                    onSuccess = { t ->
                        cache[k] = t
                        res = Result.Success(t)
                    },
                    onFailure = { f ->
                        res = Result.Failure(f)
                    }
                )
            }
        } else {
            res = Result.Success(cache[k] as T)
        }
        return res!!
    }

    protected suspend inline fun <reified T : PaginatedResponse<*>, F> getOrFetchPage(
        crossinline call: suspend (page: Int) -> Result<T, F>
    ): Result<T?, F> {
        val k = T::class
        val flagInvalidPage = -1

        if (!dataPages.containsKey(k)) {
            dataPages[k] = 1
        }
        val page = dataPages[k]!!
        if (page == flagInvalidPage) return Result.Success(null)

        var res: Result<T?, F>? = null
        withContext(viewModelScope.coroutineContext) {
            val result = call.invoke(page)
            result.fold(
                onSuccess = { t ->
                    if (t.page * t.perPage >= t.total) {
                        dataPages.remove(k)
                        dataPages[k] = flagInvalidPage
                    }

                    if (!cache.containsKey(k)) {
                        cache[k] = t
                    } else {
                        val prev = cache[k] as T
                        if (prev.page != t.page) {
                            @Suppress("UNCHECKED_CAST")
                            (prev.data as ArrayList<Any>).addAll(t.data as ArrayList<*>)

                            prev.page = t.page
                            prev.perPage = t.perPage
                            prev.total = t.total
                        }
                    }

                    if (t.data.isNotEmpty()) {
                        res = Result.Success(t)
                        dataPages[k] = page + 1
                    } else {
                        res = Result.Success(null)
                    }
                },
                onFailure = { f ->
                    res = Result.Failure(f)
                }
            )
        }
        return res!!
    }

    protected inline fun <reified T : PaginatedResponse<*>> getCachedPages(): T? {
        return cache[T::class] as? T
    }
}

