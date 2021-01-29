package com.kumela.socialnetwork.common.di.app

import com.kumela.socialnetwork.common.di.annotations.AppScope
import com.kumela.socialnetwork.network.api.ApiService
import com.kumela.socialnetwork.network.authentication.KeyStore
import com.kumela.socialnetwork.network.repositories.*
import dagger.Module
import dagger.Provides

/**
 * Created by Toko on 16,January,2021
 **/

@Module
class RepositoryModule {
    @AppScope
    @Provides
    fun providesSearchRepository(apiService: ApiService): SearchRepository =
        SearchRepository(apiService)

    @AppScope
    @Provides
    fun providesFollowRepository(apiService: ApiService): FollowRepository =
        FollowRepository(apiService)

    @AppScope
    @Provides
    fun providesPostRepository(apiService: ApiService): PostRepository =
        PostRepository(apiService)

    @AppScope
    @Provides
    fun providesImageRepository(): ImageRepository = ImageRepository()

    @AppScope
    @Provides
    fun providesUserRepository(apiService: ApiService, keyStore: KeyStore): UserRepository =
        UserRepository(apiService, keyStore)

    @AppScope
    @Provides
    fun providesCommentRepository(apiService: ApiService): CommentRepository =
        CommentRepository(apiService)
}