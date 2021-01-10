package com.kumela.socialnetwork.ui.adapters.users

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.kumela.socialnetwork.ui.common.views.RoundedImageView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.firebase.UserModel
import com.kumela.socialnetwork.ui.common.utils.load
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc

/**
 * Created by Toko on 18,October,2020
 **/

class UserItemViewMvc(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<UserItemViewMvc.Listener>(
    inflater, parent, R.layout.item_user
) {

    interface Listener {
        fun onItemClicked(userModel: UserModel)
    }

    private var userModel: UserModel? = null

    private val imageProfile: RoundedImageView = findViewById(R.id.image_profile)
    private val textUsername: TextView = findViewById(R.id.text_username)

    init {
        rootView.setOnClickListener { listener?.onItemClicked(userModel!!) }
    }

    fun bindUser(userModel: UserModel) {
        this.userModel = userModel

        imageProfile.load(userModel.imageUri)
        textUsername.text = userModel.username
    }
}