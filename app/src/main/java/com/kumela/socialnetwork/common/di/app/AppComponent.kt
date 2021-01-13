package com.kumela.socialnetwork.common.di.app

import com.kumela.socialnetwork.common.di.activity.ActivityComponent
import com.kumela.socialnetwork.common.di.annotations.AppScope
import dagger.Component

@AppScope
@Component(modules = [AppModule::class, NetworkingModule::class])
interface AppComponent {

    fun newActivityComponentBuilder(): ActivityComponent.Builder
}