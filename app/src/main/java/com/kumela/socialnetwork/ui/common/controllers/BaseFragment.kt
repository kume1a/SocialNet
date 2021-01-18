package com.kumela.socialnetwork.ui.common.controllers

import androidx.fragment.app.Fragment
import com.kumela.socialnetwork.common.di.presentation.PresentationModule
import com.kumela.socialnetwork.ui.common.main.MainActivity


/**
 * Created by Toko on 08,September,2020
 **/

abstract class BaseFragment : Fragment() {
    protected val injector by lazy {
        (activity as MainActivity)
            .activityComponent
            .newPresentationComponent(PresentationModule(this))
    }
}