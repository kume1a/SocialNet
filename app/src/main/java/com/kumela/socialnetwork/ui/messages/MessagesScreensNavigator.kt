package com.kumela.socialnet.ui.messages

import androidx.navigation.NavController
import javax.inject.Inject

/**
 * Created by Toko on 27,October,2020
 **/

class MessagesScreensNavigator @Inject constructor(private val navController: NavController) {

    fun toSearch() {
        val action = MessagesFragmentDirections.actionMessagesFragmentToSearchFragment()
        navController.navigate(action)
    }

    fun toChat(userId: String, userImageUri: String, userUsername: String) {
        val action = MessagesFragmentDirections
            .actionMessagesFragmentToChatFragment(userId, userImageUri, userUsername)
        navController.navigate(action)
    }
}