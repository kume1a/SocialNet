package com.kumela.socialnetwork.common.di.app

import com.kumela.socialnetwork.common.di.annotations.AppScope
import com.kumela.socialnetwork.network.api.ApiService
import com.kumela.socialnetwork.network.repositories.SearchRepository
import dagger.Module
import dagger.Provides

/**
 * Created by Toko on 16,January,2021
 **/

@Module
class RepositoryModule {
    @AppScope
    @Provides
    fun bindsSearchRepository(apiService: ApiService): SearchRepository =
        SearchRepository(apiService)
}