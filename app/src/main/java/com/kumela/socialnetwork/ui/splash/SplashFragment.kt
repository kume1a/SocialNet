package com.kumela.socialnet.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kumela.socialnet.network.authentication.AuthCheckerUseCase
import com.kumela.socialnet.ui.common.ViewMvcFactory
import com.kumela.socialnet.ui.common.controllers.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Toko on 24,September,2020
 **/

class SplashFragment : BaseFragment(), SplashViewModel.Listener {

    private lateinit var mViewMvc: SplashViewMvc
    private lateinit var mViewModel: SplashViewModel

    private var launchCalled = false

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mScreensNavigator: SplashScreensNavigator
    @Inject lateinit var mAuthChecker: AuthCheckerUseCase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        injector.inject(this)

        mViewMvc = mViewMvcFactory.newInstance(SplashViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        mViewModel.registerListener(this)

        CoroutineScope(Dispatchers.Main).launch {
            launchCalled = true
            delay(500)
            navigate()
        }
    }

    override fun onStart() {
        super.onStart()

        if (launchCalled) {
            navigate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mViewModel.unregisterListener(this)
    }

    private fun navigate() {
        if (mAuthChecker.isUserSignedIn()) {
            mViewModel.fetchIfUserExtraInfoExistsAndNotify()
        } else {
            mScreensNavigator.toSignIn()
        }
    }

    override fun onUserExtraInfoExistsResult(exists: Boolean) {
        if (exists) {
            mScreensNavigator.toHome()
        } else {
            mScreensNavigator.toPersonalInfo(mAuthChecker.getUId()!!)
        }
    }
}