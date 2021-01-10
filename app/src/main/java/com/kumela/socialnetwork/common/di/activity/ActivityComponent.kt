package com.kumela.socialnetwork.common.di.activity

import androidx.appcompat.app.AppCompatActivity
import com.kumela.socialnetwork.common.di.annotations.ActivityScope
import com.kumela.socialnetwork.common.di.presentation.PresentationComponent
import com.kumela.socialnetwork.common.di.presentation.PresentationModule
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun newPresentationComponent(presentationModule: PresentationModule): PresentationComponent

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance fun activity(activity: AppCompatActivity): Builder
        fun build(): ActivityComponent
    }

}