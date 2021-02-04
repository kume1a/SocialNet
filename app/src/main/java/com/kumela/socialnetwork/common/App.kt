package com.kumela.socialnetwork.common

import android.app.Application
import com.kumela.socialnetwork.common.di.app.AppComponent
import com.kumela.socialnetwork.common.di.app.AppModule
import com.kumela.socialnetwork.common.di.app.DaggerAppComponent
import com.kumela.socialnetwork.network.repositories.UserRepository
import javax.inject.Inject

/**
 * Created by Toko on 08,September,2020
 **/

class App : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    @Inject lateinit var userRepository: UserRepository

    override fun onCreate() {
        super.onCreate()

        appComponent.inject(this)

        userRepository.registerUserPresenceListener()
    }
}