package com.kumela.socialnetwork.common.di.activity

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.kumela.socialnetwork.common.di.annotations.ActivityScope
import com.kumela.socialnetwork.common.permissions.PermissionHelper
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.IntentDispatcher
import com.kumela.socialnetwork.ui.common.controllers.RequestResultDispatcher
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