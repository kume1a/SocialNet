package com.kumela.socialnetwork.ui.replies

import com.kumela.socialnetwork.models.Reply
import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.api.PaginatedReplyResponse
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.repositories.CommentRepository
import com.kumela.socialnetwork.ui.common.CachedViewModel

class ReplyViewModel(
    private val commentRepository: CommentRepository
) : CachedViewModel() {
    suspend fun createReply(
        postId: Int,
        commentId: Int,
        replyBody: String
    ): Result<Reply, NetworkError> {
        return commentRepository.createReply(postId, commentId, replyBody)
    }

    suspend fun getReplies(
        postId: Int,
        commentId: Int
    ): Result<PaginatedReplyResponse?, NetworkError> {
        return fetchAndCachePage { page ->
            commentRepository.getReplies(postId, commentId, page, 10)
        }
    }

    fun getCachedReplies(): PaginatedReplyResponse? = getFromCache()
}