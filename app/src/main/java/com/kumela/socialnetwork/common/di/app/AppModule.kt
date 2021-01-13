package com.kumela.socialnetwork.common.di.app

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kumela.socialnetwork.common.di.annotations.AppScope
import com.kumela.socialnetwork.ui.common.dialogs.DialogEventBus
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: Application) {

    @Provides
    fun application() = application

    @Provides
    @AppScope
    fun providesDialogEventBus(): DialogEventBus = DialogEventBus()

    @AppScope
    @Provides
    fun providesFirebaseAuth(): FirebaseAuth = Firebase.auth
}