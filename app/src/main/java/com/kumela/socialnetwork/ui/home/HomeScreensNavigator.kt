package com.kumela.socialnetwork.ui.home

import android.net.Uri
import androidx.navigation.NavController
import com.kumela.socialnetwork.ui.user_list.DataType
import javax.inject.Inject

/**
 * Created by Toko on 25,October,2020
 **/

class HomeScreensNavigator @Inject constructor(private val navController: NavController) {

    fun toUserProfile(userId: Int, name: String, imageUri: String) {
        val action =
            HomeFragmentDirections.actionGlobalUserProfileFragment(userId, imageUri, name)
        navController.navigate(action)
    }

    fun toStoryUploader(imageUri: Uri) {
        val action = HomeFragmentDirections.actionHomeFragmentToStoryUploaderFragment(imageUri)
        navController.navigate(action)
    }

    fun toUsersList(dataType: DataType, postId: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToUserListFragment(dataType, postId)
        navController.navigate(action)
    }

    fun toComments(postId: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToCommentsFragment(postId)
        navController.navigate(action)
    }

    fun toStoryPresenter(userId: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToStoryPresenterFragment(userId)
        navController.navigate(action)
    }
}