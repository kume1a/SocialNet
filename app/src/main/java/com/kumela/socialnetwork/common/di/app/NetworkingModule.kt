package com.kumela.socialnetwork.common.di.app

import com.kumela.socialnetwork.common.Constants
import com.kumela.socialnetwork.common.di.annotations.AppScope
import com.kumela.socialnetwork.network.api.ApiService
import com.kumela.socialnetwork.network.authentication.KeyStore
import com.kumela.socialnetwork.network.common.HttpClient
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Toko on 13,January,2021
 **/

@Module
class NetworkingModule {
    @Provides
    fun providesHttpClient(keyStore: KeyStore): HttpClient = HttpClient(keyStore)

    @AppScope
    @Provides
    fun providesRetrofit(httpClient: HttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.getClient())
            .build()

    @AppScope
    @Provides
    fun providesApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}