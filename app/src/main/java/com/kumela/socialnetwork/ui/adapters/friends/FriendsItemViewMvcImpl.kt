package com.kumela.socialnetwork.ui.adapters.friends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kumela.socialnetwork.ui.views.RoundedImageView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.utils.isOnline
import com.kumela.socialnetwork.ui.common.utils.load

/**
 * Created by Toko on 04,October,2020
 **/

class FriendsItemViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<FriendsItemViewMvc.Listener>(
    inflater,
    parent,
    R.layout.item_friend
), FriendsItemViewMvc {

    private var user: User? = null

    private val image: RoundedImageView = findViewById(R.id.image)
    private val imageOnlineIndicator: RoundedImageView = findViewById(R.id.iv_online)

    init {
        rootView.setOnClickListener { listener?.onFriendItemClicked(user!!) }
    }

    override fun bindUserModel(user: User) {
        this.user = user

        image.load(user.imageUrl)
        imageOnlineIndicator.visibility = View.GONE
        imageOnlineIndicator.visibility = if (user.isOnline()) View.VISIBLE else View.GONE
    }
}