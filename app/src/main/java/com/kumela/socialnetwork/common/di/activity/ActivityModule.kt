package com.kumela.socialnet.common.di.activity

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.kumela.socialnet.common.di.annotations.ActivityScope
import com.kumela.socialnet.common.permissions.PermissionHelper
import com.kumela.socialnet.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnet.ui.common.controllers.IntentDispatcher
import com.kumela.socialnet.ui.common.controllers.RequestResultDispatcher
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {

    @ActivityScope
    @Provides
    fun providesBottomNavHelper(activity: AppCompatActivity) =
        activity as BottomNavHelper

    @ActivityScope
    @Provides
    fun providesPermissionHelper(activity: AppCompatActivity): PermissionHelper =
        PermissionHelper(activity)

    @ActivityScope
    @Provides
    fun providesRequestResultDispatcher(activity: AppCompatActivity): RequestResultDispatcher =
        activity as RequestResultDispatcher

    @ActivityScope
    @Provides
    fun providesIntentDispatcher(activity: AppCompatActivity): IntentDispatcher =
        activity as IntentDispatcher


    companion object {
        @Provides
        fun providesLayoutInflater(activity: AppCompatActivity): LayoutInflater =
            LayoutInflater.from(activity)

        @Provides
        fun providesFragmentManager(activity: AppCompatActivity): FragmentManager =
            activity.supportFragmentManager
    }
}