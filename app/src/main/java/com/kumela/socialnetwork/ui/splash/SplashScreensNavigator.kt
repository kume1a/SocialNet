package com.kumela.socialnet.ui.splash

import androidx.navigation.NavController
import com.kumela.socialnet.ui.common.utils.navigateSafely
import javax.inject.Inject

/**
 * Created by Toko on 27,September,2020
 **/

class SplashScreensNavigator @Inject constructor(private val navController: NavController) {
    fun toSignIn() {
        val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
        navController.navigateSafely(action)
    }

    fun toHome() {
        val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
        navController.navigateSafely(action)
    }

    fun toPersonalInfo(uid: String) {
        val action = SplashFragmentDirections.actionSplashFragmentToPersonalInfoFragment(uid)
        navController.navigateSafely(action)
    }
}