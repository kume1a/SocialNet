package com.kumela.socialnetwork.ui.comments

import android.util.Log
import com.kumela.socialnetwork.models.Comment
import com.kumela.socialnetwork.models.list.CommentList
import com.kumela.socialnetwork.network.firebase.PostUseCase
import com.kumela.socialnetwork.network.firebase.UserUseCase
import com.kumela.socialnetwork.ui.common.viewmodels.ObservableViewModel

/**
 * Created by Toko on 08,November,2020
 **/

class CommentsViewModel : ObservableViewModel<CommentsViewModel.Listener>() {
    interface Listener {
        fun onCommentFetched(comment: CommentList)
    }

    private var comments = ArrayList<CommentList>()

    fun getComments(): List<CommentList> = comments

    init {
        PostUseCase.registerListener(uuid)
        UserUseCase.registerListener(uuid)
    }

    override fun onCleared() {
        super.onCleared()

        PostUseCase.unregisterListener(uuid)
        UserUseCase.unregisterListener(uuid)
    }

    fun createComment(postId: String, comment: Comment) {
        PostUseCase.createComment(uuid, postId, comment,
            onSuccessListener = {
                Log.d(javaClass.simpleName, "createComment: comment posted")
            },
            onFailureListener = { exception ->
                Log.e(javaClass.simpleName, "createComment: ", exception)
            })
    }

    fun fetchCommentsAndNotify(postId: String) {
        PostUseCase.fetchComments(uuid, postId,
            onSuccessListener = { comments ->
                for (comment in comments) {
                    fetchCommentAuthorAndNotifyListeners(comment)
                }
            },
            onFailureListener = { databaseError ->
                Log.e(javaClass.simpleName, "fetchCommentsAndNotify: ", databaseError.toException())
            })

    }

    private fun fetchCommentAuthorAndNotifyListeners(comment: Comment) {
        UserUseCase.fetchUserAndNotify(uuid, comment.userId,
            onSuccessListener = { userModel ->
                val commentListModel = CommentList(
                    userModel.id,
                    userModel.imageUri,
                    userModel.username,
                    comment.comment,
                    comment.timestamp
                )
                this.comments.add(commentListModel)
                for (listener in listeners) {
                    listener.onCommentFetched(commentListModel)
                }
            },
            onFailureListener = { databaseError ->
                Log.e(javaClass.simpleName, "fetchCommentAuthorAndNotifyListeners: ", databaseError.toException())
            })
    }
}