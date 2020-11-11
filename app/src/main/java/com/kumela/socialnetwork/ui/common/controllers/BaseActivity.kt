package com.kumela.socialnet.ui.common.controllers

import androidx.appcompat.app.AppCompatActivity
import com.kumela.socialnet.common.App
import com.kumela.socialnet.common.di.presentation.PresentationModule

/**
 * Created by Toko on 10,September,2020
 **/

abstract class BaseActivity : AppCompatActivity() {
    private val appComponent get() = (application as App).appComponent

    val activityComponent by lazy {
        appComponent.newActivityComponentBuilder()
            .activity(this)
            .build()
    }

    protected val presentationComponent by lazy {
        activityComponent.newPresentationComponent(PresentationModule())
    }
}