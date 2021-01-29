package com.kumela.socialnetwork.ui.replies

import androidx.navigation.NavController
import com.kumela.socialnetwork.ui.comments.CommentsFragmentDirections
import javax.inject.Inject

class ReplyScreensNavigator @Inject constructor(private val navController: NavController) {
    fun navigateUp() {
        navController.navigateUp()
    }

    fun toUserProfile(userId: Int, userImageUrl: String, userName: String) {
        val action =
            CommentsFragmentDirections.actionGlobalUserProfileFragment(
                userId,
                userImageUrl,
                userName
            )
        navController.navigate(action)
    }
}