package com.kumela.socialnet.ui.profile

import androidx.navigation.NavController
import com.kumela.socialnet.ui.user_list.DataType
import javax.inject.Inject

/**
 * Created by Toko on 18,October,2020
 **/

class ProfileScreensNavigator @Inject constructor(private val navController: NavController) {
    fun toSignIn() {
        val action = ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
        navController.navigate(action)
    }

    fun toUsersList(dataType: DataType, userId: String) {
        val action = ProfileFragmentDirections.actionProfileFragmentToUserListFragment(dataType, userId)
        navController.navigate(action)
    }
}