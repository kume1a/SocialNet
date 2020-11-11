package com.kumela.socialnet.common.di.app

import com.kumela.socialnet.common.di.activity.ActivityComponent
import com.kumela.socialnet.common.di.annotations.AppScope
import dagger.Component

@AppScope
@Component(modules = [AppModule::class])
interface AppComponent {

    fun newActivityComponentBuilder(): ActivityComponent.Builder
}