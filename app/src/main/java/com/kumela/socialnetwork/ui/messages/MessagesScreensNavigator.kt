package com.kumela.socialnetwork.ui.messages

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

    fun toChat(userId: Int, userImageUrl: String, userName: String) {
        val action = MessagesFragmentDirections
            .actionMessagesFragmentToChatFragment(userId, userImageUrl, userName)
        navController.navigate(action)
    }
}