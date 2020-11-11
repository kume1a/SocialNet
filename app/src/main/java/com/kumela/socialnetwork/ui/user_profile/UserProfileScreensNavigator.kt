package com.kumela.socialnet.ui.user_profile

import androidx.navigation.NavController
import com.kumela.socialnet.ui.user_list.DataType
import javax.inject.Inject

/**
 * Created by Toko on 18,October,2020
 **/

class UserProfileScreensNavigator @Inject constructor(private val navController: NavController) {
    fun navigateUp() {
        navController.navigateUp()
    }

    fun toChat(userId: String, userImageUri: String, userUsername: String) {
        val action =
            UserProfileFragmentDirections.actionUserProfileFragmentToChatFragment(userId, userImageUri, userUsername)
        navController.navigate(action)
    }

    fun toDataPresenter(dataType: DataType, userId: String) {
        val action =
            UserProfileFragmentDirections.actionUserProfileFragmentToUserListFragment(dataType, userId)
        navController.navigate(action)
    }
}