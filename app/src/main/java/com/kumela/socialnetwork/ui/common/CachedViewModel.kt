package com.kumela.socialnetwork.ui.common

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
    val cache: ConcurrentHashMap<KClass<*>, Any> = ConcurrentHashMap()
    val dataPages: ConcurrentHashMap<KClass<*>, Int> = ConcurrentHashMap()

    suspend inline fun <reified T : Any, F> fetchAndCache(
        crossinline call: suspend () -> Result<T, F>
    ): Result<T, F> {
        val k = T::class
        var res: Result<T, F>? = null
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

        return res!!
    }

    suspend inline fun <reified T : PaginatedResponse<*>, F> fetchAndCachePage(
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

                    res = Result.Success(t)
                    dataPages[k] = page + 1
                },
                onFailure = { f ->
                    res = Result.Failure(f)
                }
            )
        }
        return res!!
    }

    inline fun <reified T> getFromCache(): T? {
        return cache[T::class] as? T
    }
}

