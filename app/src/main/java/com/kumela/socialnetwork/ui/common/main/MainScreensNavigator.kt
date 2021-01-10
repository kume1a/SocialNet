package com.kumela.socialnetwork.ui.common.main

import android.graphics.Bitmap
import androidx.navigation.NavController
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.ui.post_image.PostImageFragmentDirections
import javax.inject.Inject

/**
 * Created by Toko on 27,September,2020
 **/

class MainScreensNavigator @Inject constructor(private val navController: NavController) {

    enum class BottomNavDest {
        HOME, EXPLORE, CHATS, PROFILE
    }

    private fun getBottomNavDest(id: Int?): BottomNavDest? {
        return when (id) {
            R.id.homeFragment -> BottomNavDest.HOME
            R.id.exploreFragment -> BottomNavDest.EXPLORE
            R.id.messagesFragment -> BottomNavDest.CHATS
            R.id.profileFragment -> BottomNavDest.PROFILE
            else -> null
        }
    }

    fun toProfile() {
        val id = when (getBottomNavDest(navController.currentDestination?.id)) {
            BottomNavDest.HOME -> R.id.action_homeFragment_to_profileFragment
            BottomNavDest.EXPLORE -> R.id.action_exploreFragment_to_profileFragment
            BottomNavDest.CHATS -> R.id.action_messagesFragment_to_profileFragment
            else -> throw RuntimeException()
        }
        navController.navigate(id)
    }

    fun toMessages() {
        val id = when (getBottomNavDest(navController.currentDestination?.id)) {
            BottomNavDest.HOME -> R.id.action_homeFragment_to_messagesFragment
            BottomNavDest.EXPLORE -> R.id.action_exploreFragment_to_messagesFragment
            BottomNavDest.PROFILE -> R.id.action_profileFragment_to_messagesFragment
            else -> throw RuntimeException()
        }
        navController.navigate(id)
    }

    fun toExplore() {
        val id = when (getBottomNavDest(navController.currentDestination?.id)) {
            BottomNavDest.HOME -> R.id.action_homeFragment_to_exploreFragment
            BottomNavDest.CHATS -> R.id.action_messagesFragment_to_exploreFragment
            BottomNavDest.PROFILE -> R.id.action_profileFragment_to_exploreFragment
            else -> throw RuntimeException()
        }
        navController.navigate(id)
    }

    fun toHome() {
        val id = when (getBottomNavDest(navController.currentDestination?.id)) {
            BottomNavDest.EXPLORE -> R.id.action_exploreFragment_to_homeFragment
            BottomNavDest.CHATS -> R.id.action_messagesFragment_to_homeFragment
            BottomNavDest.PROFILE -> R.id.action_profileFragment_to_homeFragment
            else -> throw RuntimeException()
        }
        navController.navigate(id)
    }

    fun toPostImage(imageUri: String) {
        val action = PostImageFragmentDirections.actionGlobalPostImageFragment(imageUri = imageUri)
        navController.navigate(action)
    }

    fun toPostImage(imageBitmap: Bitmap) {
        val action = PostImageFragmentDirections.actionGlobalPostImageFragment(imageBitmap = imageBitmap)
        navController.navigate(action)
    }
}