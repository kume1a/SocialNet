package com.kumela.socialnetwork.ui.register

import androidx.navigation.NavController
import javax.inject.Inject

class RegisterScreensNavigator @Inject constructor(private val navController: NavController) {

    fun toHome() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
        navController.navigate(action)
    }

    fun toLogin() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        navController.navigate(action)
    }

    fun toPersonalInfo(uid: String) {
        val action = RegisterFragmentDirections.actionRegisterFragmentToPersonalInfoFragment(uid)
        navController.navigate(action)
    }
}
