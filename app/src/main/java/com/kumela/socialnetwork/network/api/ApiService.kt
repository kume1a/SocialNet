package com.kumela.socialnetwork.network.api

import com.kumela.socialnetwork.models.Feed
import com.kumela.socialnetwork.models.User
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

interface PaginatedResponse<T: Any> {
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
    suspend fun createPost(@Body body: PostBody): Response<Unit>

    @GET("/posts/user/{userId}")
    suspend fun getPosts(
        @Path("userId") userId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<PaginatedPostResponse>

    @GET("/posts/feed/{userId}")
    suspend fun getFeedPosts(
        @Path("userId") userid: Int,
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
}