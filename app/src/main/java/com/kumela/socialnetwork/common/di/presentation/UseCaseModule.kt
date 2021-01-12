package com.kumela.socialnetwork.common.di.presentation

import com.google.firebase.auth.FirebaseAuth
import com.kumela.socialnetwork.common.utils.CredentialChecker
import com.kumela.socialnetwork.network.authentication.AuthCheckerUseCase
import com.kumela.socialnetwork.network.authentication.AuthUseCase
import dagger.Module
import dagger.Provides

/**
 * Created by Toko on 27,September,2020
 **/

@Module
class UseCaseModule {

    @Provides
    fun providesCredentialChecker() = CredentialChecker()

    @Provides
    fun providesAuthCheckerUseCase(firebaseAuth: FirebaseAuth) = AuthCheckerUseCase(firebaseAuth)

    @Provides
    fun providesLoginUseCase(firebaseAuth: FirebaseAuth) = AuthUseCase(firebaseAuth)
}