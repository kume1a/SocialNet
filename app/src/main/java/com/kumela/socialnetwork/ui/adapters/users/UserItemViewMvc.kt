package com.kumela.socialnetwork.ui.adapters.users

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.kumela.socialnetwork.ui.views.RoundedImageView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.User
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
        fun onItemClicked(user: User)
    }

    private var user: User? = null

    private val imageProfile: RoundedImageView = findViewById(R.id.image_profile)
    private val textUsername: TextView = findViewById(R.id.text_username)

    init {
        rootView.setOnClickListener { listener?.onItemClicked(user!!) }
    }

    fun bindUser(user: User) {
        this.user = user

        imageProfile.load(user.imageUrl)
        textUsername.text = user.name
    }
}