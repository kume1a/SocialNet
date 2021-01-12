package com.kumela.socialnetwork.ui.login

import androidx.navigation.NavController
import javax.inject.Inject

/**
 * Created by Toko on 27,September,2020
 **/

class AuthScreensNavigator @Inject constructor(private val navController: NavController) {
    fun toHome() {
        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        navController.navigate(action)
    }
}