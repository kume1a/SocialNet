package com.kumela.socialnetwork.common.di.app

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kumela.socialnetwork.common.Constants
import com.kumela.socialnetwork.common.di.annotations.AppScope
import com.kumela.socialnetwork.network.api.ApiService
import com.kumela.socialnetwork.network.authentication.KeyStore
import com.kumela.socialnetwork.network.common.GsonConverters
import com.kumela.socialnetwork.network.common.HttpInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Toko on 13,January,2021
 **/

@Module
class NetworkingModule {
    @Provides
    fun providesHttpInterceptor(keyStore: KeyStore): Interceptor = HttpInterceptor(keyStore)

    @Provides
    fun providesHttpClient(interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

    @Provides
    fun providesGsonConverters(): GsonConverters = GsonConverters()

    @Provides
    fun providesGson(gsonConverters: GsonConverters): Gson =
        GsonBuilder()
            .registerTypeAdapter(Boolean::class.java, gsonConverters.getBooleanToIntAdapter())
            .create()

    @AppScope
    @Provides
    fun providesRetrofit(httpClient: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()

    @AppScope
    @Provides
    fun providesApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}