package com.kumela.socialnetwork.network.api

import com.kumela.socialnetwork.models.*
import com.kumela.socialnetwork.models.list.Post
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Toko on 13,January,2021
 **/

data class SignupBody(val name: String, val email: String, val password: String)
data class SigninBody(val email: String, val password: String)
data class PostBody(val imageUrl: String, val header: String, val description: String)
data class LikeDislikeBody(val postId: Int)
data class CommentBody(val postId: Int, val body: String)
data class ReplyBody(val body: String)
data class StoryBody(val imageUrl: String)

interface PaginatedResponse<T : Any> {
    val data: ArrayList<T>
    var total: Int
    var page: Int
    var perPage: Int
}

data class AuthResponse(val userId: Int, val token: String)
data class PaginatedPostResponse(
    override val data: ArrayList<Post>,
    override var total: Int,
    override var page: Int,
    override var perPage: Int,
) : PaginatedResponse<Post>
data class PaginatedFeedResponse(
    override val data: ArrayList<Feed>,
    override var total: Int,
    override var page: Int,
    override var perPage: Int,
) : PaginatedResponse<Feed>
data class PaginatedCommentResponse(
    override val data: ArrayList<Comment>,
    override var total: Int,
    override var page: Int,
    override var perPage: Int
): PaginatedResponse<Comment>
data class PaginatedReplyResponse(
    override val data: ArrayList<Reply>,
    override var total: Int,
    override var page: Int,
    override var perPage: Int
): PaginatedResponse<Reply>
data class PaginatedStoryResponse(
    override val data: ArrayList<Story>,
    override var total: Int,
    override var page: Int,
    override var perPage: Int
): PaginatedResponse<Story>
data class PaginatedUserResponse(
    override val data: ArrayList<User>,
    override var total: Int,
    override var page: Int,
    override var perPage: Int
): PaginatedResponse<User>



interface ApiService {
    @POST("/auth/signin")
    suspend fun signin(@Body body: SigninBody): Response<AuthResponse>

    @POST("/auth/signup")
    suspend fun signup(@Body body: SignupBody): Response<AuthResponse>

    @GET("/users/{userId}")
    suspend fun getUser(@Path("userId") userId: Int): Response<User>

    @GET("/search/users/{query}")
    suspend fun searchUsers(@Path("query") query: String): Response<List<User>>

    @GET("/users/{userId}/followStatus")
    suspend fun getFollowStatus(@Path("userId") userId: Int): Response<Boolean>

    @POST("/users/{userId}/switchFollowStatus")
    suspend fun switchFollowStatus(@Path("userId") userId: Int): Response<Unit>

    @POST("/posts")
    suspend fun createPost(@Body body: PostBody): Response<Post>

    @GET("/posts/user/{userId}")
    suspend fun getPosts(
        @Path("userId") userId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<PaginatedPostResponse>

    @GET("/posts/feed")
    suspend fun getFeedPosts(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<PaginatedFeedResponse>

    @GET("/posts/explore")
    suspend fun getExplorePosts(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): Response<PaginatedPostResponse>

    @POST("/posts/like")
    suspend fun likePost(@Body body: LikeDislikeBody): Response<Unit>

    @POST("/posts/dislike")
    suspend fun dislikePost(@Body body: LikeDislikeBody): Response<Unit>

    @POST("/comments/")
    suspend fun createComment(@Body body: CommentBody): Response<Comment>

    @POST("/comments/{postId}/{commentId}")
    suspend fun createReply(
        @Path("postId") postId: Int,
        @Path("commentId") commentId: Int,
        @Body body: ReplyBody
    ): Response<Reply>

    @GET("/comments/{postId}")
    suspend fun getComments(
        @Path("postId") postId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<PaginatedCommentResponse>

    @GET("/comments/{postId}/{commentId}")
    suspend fun getReplies(
        @Path("postId") postId: Int,
        @Path("commentId") commentId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<PaginatedReplyResponse>

    @GET("/users/{userId}/meta")
    suspend fun getUserMeta(@Path("userId") userId: Int): Response<UserMeta>

    @POST("/stories")
    suspend fun createStory(@Body body: StoryBody): Response<Story>

    @GET("/stories/user/{userId}")
    suspend fun getUserStories(
        @Path("userId") userId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<PaginatedStoryResponse>

    @GET("/stories/feed")
    suspend fun getFeedStories(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<PaginatedUserResponse>
}