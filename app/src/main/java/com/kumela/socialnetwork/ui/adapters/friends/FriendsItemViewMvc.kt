package com.kumela.socialnetwork.ui.adapters.friends

import com.kumela.socialnetwork.models.firebase.UserModel
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 04,October,2020
 **/

interface FriendsItemViewMvc : ObservableViewMvc<FriendsItemViewMvc.Listener> {
    interface Listener {
        fun onFriendItemClicked(userModel: UserModel)
    }

    fun bindUserModel(userModel: UserModel)
}