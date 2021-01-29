package com.kumela.socialnetwork.ui.comments

import com.kumela.socialnetwork.models.Comment
import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.api.PaginatedCommentResponse
import com.kumela.socialnetwork.network.api.PaginatedReplyResponse
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.repositories.CommentRepository
import com.kumela.socialnetwork.ui.common.CachedViewModel

/**
 * Created by Toko on 08,November,2020
 **/

class CommentsViewModel(
    private val commentRepository: CommentRepository
) : CachedViewModel() {

    suspend fun createComment(
        postId: Int,
        commentBody: String
    ): Result<Comment, NetworkError> {
        return commentRepository.createComment(postId, commentBody)
    }

    suspend fun getComments(
        postId: Int
    ): Result<PaginatedCommentResponse?, NetworkError> {
        return fetchAndCachePage { page -> commentRepository.getComments(postId, page, 10) }
    }

    suspend fun getFirstReplies(postId: Int, commentId: Int): Result<PaginatedReplyResponse, NetworkError> {
        return commentRepository.getReplies(postId, commentId, 1, 3)
    }

    fun getCachedComments(): PaginatedCommentResponse? = getFromCache()
}