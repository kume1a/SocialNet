package com.kumela.socialnetwork.ui.adapters.comments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.kumela.socialnetwork.ui.views.RoundedImageView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.list.CommentList
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.utils.load
import com.kumela.socialnetwork.ui.common.utils.setTimePassed

/**
 * Created by Toko on 08,November,2020
 **/

class CommentsItemViewMvc(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<CommentsItemViewMvc.Listener>(
    inflater, parent, R.layout.item_comment
){

    interface Listener {
        fun onUserProfileImageClicked(data: CommentList)
        fun onUserUsernameClicked(data: CommentList)
        fun onMenuClicked(data: CommentList)
    }

    private var commentList: CommentList? = null

    private val imageProfile: RoundedImageView = findViewById(R.id.image_profile)
    private val textUsername: TextView = findViewById(R.id.text_username)
    private val textComment: TextView = findViewById(R.id.text_comment)
    private val textTime: TextView = findViewById(R.id.text_time)
    private val buttonMenu: ImageButton = findViewById(R.id.button_menu)

    init {
        imageProfile.setOnClickListener { listener?.onUserProfileImageClicked(commentList!!) }
        textUsername.setOnClickListener { listener?.onUserUsernameClicked(commentList!!) }
        buttonMenu.setOnClickListener { listener?.onMenuClicked(commentList!!) }
    }

    fun bindComment(commentList: CommentList) {
        this.commentList = commentList

        imageProfile.load(commentList.posterImageUri)
        textUsername.text = commentList.posterUsername
        textComment.text = commentList.comment
        textTime.setTimePassed(commentList.timestamp, shorten = true)
    }
}