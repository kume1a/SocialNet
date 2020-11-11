package com.kumela.socialnet.ui.user_list

import com.kumela.socialnet.models.firebase.UserModel
import com.kumela.socialnet.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 05,November,2020
 **/

interface UserListViewMvc : ObservableViewMvc<UserListViewMvc.Listener> {
    interface Listener {
        fun onNavigateUpClicked()
        fun onUserClicked(userModel: UserModel)
        fun onLastUserBound()
    }

    fun bindToolbarHeader(text: String)

    fun bindUsers(users: List<UserModel>)
    fun addUser(userModel: UserModel)
}