package com.kumela.socialnetwork.ui.common.dialogs

import androidx.fragment.app.DialogFragment
import com.kumela.socialnetwork.common.di.presentation.PresentationModule
import com.kumela.socialnetwork.ui.common.main.MainActivity

/**
 * Created by Toko on 14,October,2020
 **/

abstract class BaseDialog : DialogFragment() {
//    private val appComponent get() = (requireActivity().application as App).appComponent
//
//    private val activityComponent by lazy {
//        appComponent.newActivityComponentBuilder()
//            .activity(activity as AppCompatActivity)
//            .build()
//    }
//
//    protected val injector by lazy {
//        activityComponent.newPresentationComponent(PresentationModule(this, this))
//    }

    protected val injector by lazy {
        (activity as MainActivity)
            .activityComponent
            .newPresentationComponent(PresentationModule(this))
    }
}