package com.kumela.socialnetwork.ui.adapters.reply

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.Reply
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.utils.load
import com.kumela.socialnetwork.ui.common.utils.setTimePassed
import com.kumela.socialnetwork.ui.views.RoundedImageView

class ReplyItemViewMvc(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<ReplyItemViewMvc.Listener>(
    inflater, parent, R.layout.item_reply
) {
    interface Listener {
        fun onUserProfileImageClicked(reply: Reply)
        fun onUserNameClicked(reply: Reply)
        fun onLikeClicked(reply: Reply)
    }

    private var reply: Reply? = null

    private val imageProfile: RoundedImageView = findViewById(R.id.image_profile)
    private val textUsername: TextView = findViewById(R.id.text_username)
    private val textReply: TextView = findViewById(R.id.text_reply)
    private val textTime: TextView = findViewById(R.id.text_time)
    private val textLike: TextView = findViewById(R.id.text_like)

    init {
        imageProfile.setOnClickListener { listener?.onUserProfileImageClicked(reply!!) }
        textUsername.setOnClickListener { listener?.onUserNameClicked(reply!!) }
        textLike.setOnClickListener { listener?.onLikeClicked(reply!!) }
    }

    fun bindReply(reply: Reply) {
        this.reply = reply

        imageProfile.load(reply.userImageUrl)
        textUsername.text = reply.userName
        textReply.text = reply.body
        textTime.setTimePassed(reply.createdAt, shorten = true)
    }
}