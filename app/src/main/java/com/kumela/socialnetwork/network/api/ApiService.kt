package com.kumela.socialnetwork.network.api

import com.kumela.socialnetwork.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by Toko on 13,January,2021
 **/

data class SignupBody(val name: String, val email: String, val password: String)
data class SigninBody(val email: String, val password: String)
data class PostBody(val imageUrl: String, val header: String, val description: String)

data class AuthResponse(val userId: Int, val token: String)

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
}