package com.kumela.socialnetwork.ui.adapters.friends

import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 04,October,2020
 **/

interface FriendsItemViewMvc : ObservableViewMvc<FriendsItemViewMvc.Listener> {
    interface Listener {
        fun onFriendItemClicked(user: User)
    }

    fun bindUserModel(user: User)
}