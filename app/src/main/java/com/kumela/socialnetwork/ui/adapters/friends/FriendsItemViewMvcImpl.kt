package com.kumela.socialnet.ui.adapters.friends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kumela.roundimageview.RoundedImageView
import com.kumela.socialnet.R
import com.kumela.socialnet.models.firebase.UserModel
import com.kumela.socialnet.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnet.ui.common.utils.isOnline
import com.kumela.socialnet.ui.common.utils.load

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

    private var userModel: UserModel? = null

    private val image: RoundedImageView = findViewById(R.id.image)
    private val imageOnlineIndicator: RoundedImageView = findViewById(R.id.iv_online)

    init {
        rootView.setOnClickListener { listener?.onFriendItemClicked(userModel!!) }
    }

    override fun bindUserModel(userModel: UserModel) {
        this.userModel = userModel

        image.load(userModel.imageUri)
        imageOnlineIndicator.visibility = View.GONE
        imageOnlineIndicator.visibility = if (userModel.isOnline()) View.VISIBLE else View.GONE
    }
}