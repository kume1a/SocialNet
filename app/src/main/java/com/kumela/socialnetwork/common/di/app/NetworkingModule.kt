package com.kumela.socialnetwork.common.di.app

import com.kumela.socialnetwork.common.Constants
import com.kumela.socialnetwork.common.di.annotations.AppScope
import com.kumela.socialnetwork.network.api.ApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Toko on 13,January,2021
 **/

@Module
class NetworkingModule {
    @AppScope
    @Provides
    fun providesRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @AppScope
    @Provides
    fun providesApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}