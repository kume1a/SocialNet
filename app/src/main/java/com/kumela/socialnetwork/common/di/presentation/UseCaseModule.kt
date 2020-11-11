package com.kumela.socialnet.common.di.presentation

import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.kumela.socialnet.common.utils.CredentialChecker
import com.kumela.socialnet.network.authentication.*
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
    fun providesGoogleSignInUseCase(
        activity: AppCompatActivity,
        firebaseAuth: FirebaseAuth
    ): GoogleSignInUseCase = GoogleSignInUseCase(activity, firebaseAuth)

    @Provides
    fun providesFacebookLoginUseCase(
        activity: AppCompatActivity,
        firebaseAuth: FirebaseAuth,
        loginManager: LoginManager,
        callbackManager: CallbackManager
    ): FacebookSignInUseCase =
        FacebookSignInUseCase(activity, firebaseAuth, loginManager, callbackManager)

    @Provides
    fun providesLoginUseCase(
        firebaseAuth: FirebaseAuth,
        googleSignInUseCase: GoogleSignInUseCase,
        facebookSignInUseCase: FacebookSignInUseCase
    ) = LoginUseCase(firebaseAuth, googleSignInUseCase, facebookSignInUseCase)

    @Provides
    fun providesRegisterUseCase(
        firebaseAuth: FirebaseAuth,
        googleSignInUseCase: GoogleSignInUseCase,
        facebookSignInUseCase: FacebookSignInUseCase
    ) = RegisterUseCase(firebaseAuth, googleSignInUseCase, facebookSignInUseCase)
}