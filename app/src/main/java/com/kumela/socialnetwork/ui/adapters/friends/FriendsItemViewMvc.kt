package com.kumela.socialnet.ui.adapters.friends

import com.kumela.socialnet.models.firebase.UserModel
import com.kumela.socialnet.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 04,October,2020
 **/

interface FriendsItemViewMvc : ObservableViewMvc<FriendsItemViewMvc.Listener> {
    interface Listener {
        fun onFriendItemClicked(userModel: UserModel)
    }

    fun bindUserModel(userModel: UserModel)
}