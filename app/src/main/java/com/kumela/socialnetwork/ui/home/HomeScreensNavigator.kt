package com.kumela.socialnetwork.ui.home

import android.net.Uri
import androidx.navigation.NavController
import com.kumela.socialnetwork.ui.user_list.DataType
import javax.inject.Inject

/**
 * Created by Toko on 25,October,2020
 **/

class HomeScreensNavigator @Inject constructor(private val navController: NavController) {

    fun toUserProfile(uid: String, imageUri: String, username: String) {
//        val action = HomeFragmentDirections.actionGlobalUserProfileFragment(uid, imageUri, username)
//        navController.navigate(action)
    }

    fun toStoryUploader(imageUri: Uri) {
        val action = HomeFragmentDirections.actionHomeFragmentToStoryUploaderFragment(imageUri)
        navController.navigate(action)
    }

    fun toUsersList(dataType: DataType, postId: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToUserListFragment(dataType, postId)
        navController.navigate(action)
    }

    fun toComments(
        postId: String,
        currentUserId: String,
        currentUserProfileImageUri: String,
        currentUserUsername: String
    ) {
        val action = HomeFragmentDirections.actionHomeFragmentToCommentsFragment(
            postId,
            currentUserId,
            currentUserProfileImageUri,
            currentUserUsername
        )
        navController.navigate(action)
    }

    fun toStoryPresenter(userId: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToStoryPresenterFragment(userId)
        navController.navigate(action)
    }
}