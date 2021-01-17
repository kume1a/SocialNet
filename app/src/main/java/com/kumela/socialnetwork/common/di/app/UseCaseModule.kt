package com.kumela.socialnetwork.common.di.app

import com.kumela.socialnetwork.common.di.annotations.AppScope
import com.kumela.socialnetwork.common.utils.CredentialChecker
import com.kumela.socialnetwork.network.api.ApiService
import com.kumela.socialnetwork.network.authentication.AuthUseCase
import com.kumela.socialnetwork.network.authentication.KeyStore
import dagger.Module
import dagger.Provides

/**
 * Created by Toko on 27,September,2020
 **/

@Module
class UseCaseModule {

    @Provides
    fun providesCredentialChecker() = CredentialChecker()

    @AppScope
    @Provides
    fun providesAuthUseCase(apiService: ApiService, keyStore: KeyStore) =
        AuthUseCase(apiService, keyStore)
}