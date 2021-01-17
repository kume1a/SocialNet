package com.kumela.socialnetwork.ui.user_list

import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 05,November,2020
 **/

interface UserListViewMvc : ObservableViewMvc<UserListViewMvc.Listener> {
    interface Listener {
        fun onNavigateUpClicked()
        fun onUserClicked(user: User)
        fun onLastUserBound()
    }

    fun bindToolbarHeader(text: String)

    fun bindUsers(users: List<User>)
    fun addUser(user: User)
}