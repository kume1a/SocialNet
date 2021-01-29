package com.kumela.socialnetwork.network.repositories

import com.kumela.socialnetwork.models.Comment
import com.kumela.socialnetwork.models.Reply
import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.api.*
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.common.mapToResult
import com.kumela.socialnetwork.network.common.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CommentRepository(private val apiService: ApiService) {
    suspend fun createComment(
        postId: Int,
        commentBody: String
    ): Result<Comment, NetworkError> = withContext(Dispatchers.IO) {
        val body = CommentBody(postId, commentBody)
        return@withContext safeCall { apiService.createComment(body) }.mapToResult()
    }

    suspend fun createReply(
        postId: Int,
        commentId: Int,
        replyBody: String
    ): Result<Reply, NetworkError> = withContext(Dispatchers.IO) {
        val body = ReplyBody(replyBody)
        return@withContext safeCall { apiService.createReply(postId, commentId, body) }.mapToResult()
    }

    suspend fun getComments(
        postId: Int,
        page: Int,
        limit: Int
    ): Result<PaginatedCommentResponse, NetworkError> = withContext(Dispatchers.IO) {
        return@withContext safeCall { apiService.getComments(postId, page, limit) }.mapToResult()
    }

    suspend fun getReplies(
        postId: Int,
        commentId: Int,
        page: Int,
        limit: Int
    ): Result<PaginatedReplyResponse, NetworkError> = withContext(Dispatchers.IO) {
        return@withContext safeCall { apiService.getReplies(postId,commentId, page, limit) }.mapToResult()
    }
}