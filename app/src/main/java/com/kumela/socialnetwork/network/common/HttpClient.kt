package com.kumela.socialnetwork.network.common

import com.kumela.socialnetwork.network.authentication.KeyStore
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by Toko on 17,January,2021
 **/

class HttpClient(private val keyStore: KeyStore) {
    fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request: Request = chain
                    .request()
                    .newBuilder()
                    .addHeader("Authorization", keyStore.readKey() ?: "")
                    .build()
                chain.proceed(request)
            }
            .build()
    }
}