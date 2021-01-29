package com.kumela.socialnetwork.ui.adapters.comments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.Comment
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.utils.load
import com.kumela.socialnetwork.ui.common.utils.setTimePassed
import com.kumela.socialnetwork.ui.views.CommentTreeLine
import com.kumela.socialnetwork.ui.views.RoundedImageView

/**
 * Created by Toko on 08,November,2020
 **/

class CommentsItemViewMvc(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<CommentsItemViewMvc.Listener>(
    inflater, parent, R.layout.item_comment
) {

    interface Listener {
        fun onUserProfileImageClicked(comment: Comment)
        fun onUserNameClicked(comment: Comment)
        fun onLikeClicked(comment: Comment)
        fun onReplyClicked(comment: Comment)
        fun onReplierClicked(comment: Comment)
    }

    private var comment: Comment? = null

    private val linearLayout: LinearLayout = findViewById(R.id.ll)

    private val imageProfile: RoundedImageView = findViewById(R.id.image_profile)
    private val viewLine: View = findViewById(R.id.reply_line)
    private val textUsername: TextView = findViewById(R.id.text_username)
    private val textComment: TextView = findViewById(R.id.text_comment)
    private val textTime: TextView = findViewById(R.id.text_time)
    private val textLike: TextView = findViewById(R.id.text_like)
    private val textReply: TextView = findViewById(R.id.text_reply)

    init {
        imageProfile.setOnClickListener { listener?.onUserProfileImageClicked(comment!!) }
        textUsername.setOnClickListener { listener?.onUserNameClicked(comment!!) }
        textLike.setOnClickListener { listener?.onLikeClicked(comment!!) }
        textReply.setOnClickListener { listener?.onReplyClicked(comment!!) }
    }

    fun bindComment(comment: Comment) {
        this.comment = comment

        imageProfile.load(comment.userImageUrl)
        textUsername.text = comment.userName
        textComment.text = comment.body
        textTime.setTimePassed(comment.createdAt, shorten = true)

        linearLayout.removeViews(1, linearLayout.childCount - 1)
        if (comment.firstReplies != null && comment.firstReplies.isNotEmpty()) {
            viewLine.visibility = View.VISIBLE

            for ((index, reply) in comment.firstReplies.withIndex()) {
                val view = LayoutInflater.from(context).inflate(
                    R.layout.item_comment_reply,
                    linearLayout,
                    false
                )
                view.setOnClickListener { listener?.onReplierClicked(comment) }

                val treeLine = view.findViewById<CommentTreeLine>(R.id.tree_line)
                val image = view.findViewById<RoundedImageView>(R.id.image)
                val textName = view.findViewById<TextView>(R.id.text_name)
                val textBody = view.findViewById<TextView>(R.id.text_body)
                val textViewMoreReplies = view.findViewById<TextView>(R.id.view_more_replies)

                image.load(reply.userImageUrl)
                textName.text = reply.userName
                textBody.text = reply.body
                if (index == comment.firstReplies.size - 1) {
                    treeLine.paintBottomLine = false
                    textViewMoreReplies.text = "view ${comment.replyCount} more replies"
                    textViewMoreReplies.visibility = View.VISIBLE
                } else {
                    textViewMoreReplies.visibility = View.GONE
                }

                linearLayout.addView(view)
            }
        } else {
            viewLine.visibility = View.GONE
        }
    }
}