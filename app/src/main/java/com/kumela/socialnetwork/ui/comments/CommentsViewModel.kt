package com.kumela.socialnet.ui.comments

import android.util.Log
import com.kumela.socialnet.models.firebase.CommentModel
import com.kumela.socialnet.models.list.CommentListModel
import com.kumela.socialnet.network.firebase.PostUseCase
import com.kumela.socialnet.network.firebase.UserUseCase
import com.kumela.socialnet.ui.common.viewmodels.ObservableViewModel

/**
 * Created by Toko on 08,November,2020
 **/

class CommentsViewModel : ObservableViewModel<CommentsViewModel.Listener>() {
    interface Listener {
        fun onCommentFetched(comment: CommentListModel)
    }

    private var comments = ArrayList<CommentListModel>()

    fun getComments(): List<CommentListModel> = comments

    init {
        PostUseCase.registerListener(uuid)
        UserUseCase.registerListener(uuid)
    }

    override fun onCleared() {
        super.onCleared()

        PostUseCase.unregisterListener(uuid)
        UserUseCase.unregisterListener(uuid)
    }

    fun createComment(postId: String, commentModel: CommentModel) {
        PostUseCase.createComment(uuid, postId, commentModel,
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

    private fun fetchCommentAuthorAndNotifyListeners(comment: CommentModel) {
        UserUseCase.fetchUserAndNotify(uuid, comment.userId,
            onSuccessListener = { userModel ->
                val commentListModel = CommentListModel(
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