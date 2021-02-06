package com.kumela.socialnetwork.network.common

import com.kumela.socialnetwork.network.authentication.KeyStore
import okhttp3.*
import okhttp3.internal.http2.ConnectionShutdownException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by Toko on 17,January,2021
 **/

class HttpInterceptor(private val keyStore: KeyStore) : Interceptor {

    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request: Request = chain
                .request()
                .newBuilder()
                .addHeader("Authorization", keyStore.readKey() ?: "")
                .build()

            return chain.proceed(request)
        } catch (e: Exception) {
            e.printStackTrace()
            val msg: String = when (e) {
                is SocketTimeoutException -> "Timeout - Please check your internet connection"
                is UnknownHostException -> "Unable to make a connection. Please check your internet"
                is ConnectionShutdownException -> "Connection shutdown. Please check your internet"
                is IOException -> "Server is unreachable, please try again later."
                is IllegalStateException -> "${e.message}"
                else -> "${e.message}"
            }

            return Response.Builder()
                .protocol(Protocol.HTTP_1_1)
                .code(999)
                .message(msg)
                .body(ResponseBody.create(null, "{${e}}")).build()
        }
    }
}