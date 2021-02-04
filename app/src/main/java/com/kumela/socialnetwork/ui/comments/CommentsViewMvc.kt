package com.kumela.socialnetwork.ui.comments

import com.kumela.socialnetwork.models.Comment
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

interface CommentsViewMvc : ObservableViewMvc<CommentsViewMvc.Listener> {
    interface Listener {
        fun onNavigateUpClicked()

        fun onKeyboardUp()

        fun onUserClicked(user: User)
        fun onLikeClicked(comment: Comment)
        fun onReplyClicked(comment: Comment)
        fun onReplierClicked(comment: Comment)
        fun onLastCommentBound()

        fun onPostClicked()
    }

    fun scrollToBottom()

    fun addComments(comments: List<Comment>)
    fun addComment(comment: Comment)
    fun updateComment(comment: Comment)

    fun getComment(): String
    fun clearInputField()
}