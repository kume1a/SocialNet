package com.kumela.socialnetwork.ui.replies

import com.kumela.socialnetwork.models.Comment
import com.kumela.socialnetwork.models.Reply
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

interface ReplyViewMvc: ObservableViewMvc<ReplyViewMvc.Listener> {
    interface Listener {
        fun onNavigateUpClicked()

        fun onUserClicked(user: User)
        fun onLikeClicked(reply: Reply)
        fun onLastReplyBound()

        fun onCommentUserClicked()
        fun onCommentLikeClicked()
        fun onCommentReplyClicked()

        fun onPostClicked()
    }

    fun bindComment(comment: Comment)

    fun requestInputFocus()

    fun addReplies(replies: List<Reply>)
    fun addReply(reply: Reply)

    fun getBody(): String
    fun clearInputField()
}