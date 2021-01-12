package com.kumela.socialnetwork.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kumela.socialnetwork.network.authentication.AuthCheckerUseCase
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Toko on 24,September,2020
 **/

class SplashFragment : BaseFragment() {

    private lateinit var mViewMvc: SplashViewMvc

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mScreensNavigator: SplashScreensNavigator
    @Inject lateinit var mAuthChecker: AuthCheckerUseCase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        injector.inject(this)

        mViewMvc = mViewMvcFactory.newInstance(SplashViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onStart() {
        super.onStart()

        CoroutineScope(Dispatchers.Main).launch {
            delay(500)
            if (mAuthChecker.isUserSignedIn()) {
                mScreensNavigator.toHome()
            } else {
                mScreensNavigator.toAuth()
            }
        }
    }
}