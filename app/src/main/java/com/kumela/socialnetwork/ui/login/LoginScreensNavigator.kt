package com.kumela.socialnetwork.ui.login

import androidx.navigation.NavController
import javax.inject.Inject

/**
 * Created by Toko on 27,September,2020
 **/

class LoginScreensNavigator @Inject constructor(private val navController: NavController) {
    fun toRegister() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        navController.navigate(action)
    }

    fun toHome() {
        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        navController.navigate(action)
    }

    fun toPersonalInfo(uid: String) {
        val action = LoginFragmentDirections.actionLoginFragmentToPersonalInfoFragment(uid)
        navController.navigate(action)
    }
}