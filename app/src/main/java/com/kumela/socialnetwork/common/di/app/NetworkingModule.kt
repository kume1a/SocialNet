package com.kumela.socialnetwork.common.di.app

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kumela.socialnetwork.common.Constants
import com.kumela.socialnetwork.common.di.annotations.AppScope
import com.kumela.socialnetwork.network.api.ApiService
import com.kumela.socialnetwork.network.authentication.KeyStore
import com.kumela.socialnetwork.network.common.GsonConverters
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

    @Provides
    fun providesGsonConverters(): GsonConverters = GsonConverters()

    @Provides
    fun providesGson(gsonConverters: GsonConverters): Gson =
        GsonBuilder()
            .registerTypeAdapter(Boolean::class.java, gsonConverters.getBooleanToIntAdapter())
            .create()

    @AppScope
    @Provides
    fun providesRetrofit(httpClient: HttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.getClient())
            .build()

    @AppScope
    @Provides
    fun providesApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}