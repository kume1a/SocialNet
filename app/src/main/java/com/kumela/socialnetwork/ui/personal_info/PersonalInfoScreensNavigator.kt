package com.kumela.socialnetwork.ui.personal_info

import androidx.navigation.NavController
import javax.inject.Inject

/**
 * Created by Toko on 30,September,2020
 **/

class PersonalInfoScreensNavigator @Inject constructor(private val navController: NavController) {

    fun toHome() {
        val action = PersonalInfoFragmentDirections.actionPersonalInfoFragmentToHomeFragment()
        navController.navigate(action)
    }
}