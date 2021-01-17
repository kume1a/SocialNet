package com.kumela.socialnetwork.ui.comments

import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.models.list.CommentList
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

interface CommentsViewMvc : ObservableViewMvc<CommentsViewMvc.Listener> {
    interface Listener {
        fun onNavigateUpClicked()

        fun onUserClicked(user: User)
        fun onPostClicked()
    }

    fun bindComments(comments: List<CommentList>)
    fun getComment(): String
    fun clearInputField()

    fun addComment(comment: CommentList)
}