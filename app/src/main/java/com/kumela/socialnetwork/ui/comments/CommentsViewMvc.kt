package com.kumela.socialnetwork.ui.comments

import com.kumela.socialnetwork.models.firebase.UserModel
import com.kumela.socialnetwork.models.list.CommentListModel
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

interface CommentsViewMvc : ObservableViewMvc<CommentsViewMvc.Listener> {
    interface Listener {
        fun onNavigateUpClicked()

        fun onUserClicked(userModel: UserModel)
        fun onPostClicked()
    }

    fun bindComments(comments: List<CommentListModel>)
    fun getComment(): String
    fun clearInputField()

    fun addComment(comment: CommentListModel)
}