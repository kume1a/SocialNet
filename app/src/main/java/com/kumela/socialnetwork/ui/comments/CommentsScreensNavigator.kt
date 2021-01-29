package com.kumela.socialnetwork.ui.comments

import androidx.navigation.NavController
import javax.inject.Inject

/**
 * Created by Toko on 08,November,2020
 **/

class CommentsScreensNavigator @Inject constructor(private val navController: NavController) {
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

    fun toReplies(
        postId: Int,
        commentId: Int,
        commentUserId: Int,
        commentUserName: String,
        commentUserImageUrl: String,
        commentCreatedAt: Long,
        commentBody: String
    ) {
        val action = CommentsFragmentDirections.actionCommentsFragmentToReplyFragment(
            postId,
            commentId,
            commentUserId,
            commentUserName,
            commentUserImageUrl,
            commentCreatedAt,
            commentBody
        )
        navController.navigate(action)
    }

}