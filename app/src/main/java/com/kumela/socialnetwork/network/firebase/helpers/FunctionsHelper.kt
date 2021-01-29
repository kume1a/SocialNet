package com.kumela.socialnetwork.network.firebase.helpers

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.HttpsCallableResult
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.kumela.socialnetwork.models.Feed
import com.kumela.socialnetwork.models.Message
import com.kumela.socialnetwork.models.UserFields
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.models.list.Post
import com.kumela.socialnetwork.network.firebase.UserUseCase

/**
 * Created by Toko on 22,October,2020
 **/

object FunctionsHelper {
    private const val FUNCTION_FOLLOW_USER = "followUser"
    private const val FUNCTION_UN_FOLLOW_USER = "unFollowUser"
    private const val FUNCTION_CREATE_POST = "createPost"
    private const val FUNCTION_GET_FEED_POST = "getFeedPost"
    private const val FUNCTION_LIKE_OR_DISLIKE_POST = "likeOrDislikePost"
    private const val FUNCTION_GET_FOLLOWING_USERS = "getFollowingUsers"
    private const val FUNCTION_SEND_MESSAGE = "sendMessage"

    private val firebaseFunctions = Firebase.functions

    fun followUser(
        currentUid: String,
        targetUid: String,
        onCompleteListener: (Task<HttpsCallableResult?>) -> Unit
    ) {
        val data = hashMapOf(
            "currentUid" to currentUid,
            "targetUid" to targetUid
        )

        firebaseFunctions
            .getHttpsCallable(FUNCTION_FOLLOW_USER)
            .call(data)
            .addOnCompleteListener { task ->
                onCompleteListener.invoke(task)
            }
    }

    fun unFollowUser(
        currentUid: String,
        targetUid: String,
        onCompleteListener: (Task<HttpsCallableResult?>) -> Unit
    ) {
        val data = hashMapOf(
            "currentUid" to currentUid,
            "targetUid" to targetUid
        )

        firebaseFunctions
            .getHttpsCallable(FUNCTION_UN_FOLLOW_USER)
            .call(data)
            .addOnCompleteListener { task ->
                onCompleteListener.invoke(task)
            }
    }

    fun createPost(
        post: Post,
        onCompleteListener: (Task<HttpsCallableResult?>) -> Unit
    ) {
        val data = post.toMap()

        firebaseFunctions
            .getHttpsCallable(FUNCTION_CREATE_POST)
            .call(data)
            .addOnCompleteListener { task ->
                onCompleteListener.invoke(task)
            }
    }

    fun getFeedPost(
        postId: String,
        onCompleteListener: (Task<HttpsCallableResult?>) -> Unit
    ) {
        val data = hashMapOf(
            "postId" to postId,
            "currentUid" to UserUseCase.uid
        )

        firebaseFunctions
            .getHttpsCallable(FUNCTION_GET_FEED_POST)
            .call(data)
            .addOnCompleteListener { task ->
                onCompleteListener.invoke(task)
            }
    }

    fun likeOrDislikePost(
        uid: String,
        postId: String,
        onCompleteListener: (Task<HttpsCallableResult?>) -> Unit
    ) {
        val data = hashMapOf(
            "currentUid" to uid,
            "postId" to postId
        )

        firebaseFunctions
            .getHttpsCallable(FUNCTION_LIKE_OR_DISLIKE_POST)
            .call(data)
            .addOnCompleteListener { task ->
                onCompleteListener.invoke(task)
            }
    }

    fun getFollowingUsers(
        uid: String,
        onCompleteListener: (Task<HttpsCallableResult?>) -> Unit
    ) {
        val data = hashMapOf(
            "currentUid" to uid
        )

        firebaseFunctions
            .getHttpsCallable(FUNCTION_GET_FOLLOWING_USERS)
            .call(data)
            .addOnCompleteListener { task ->
                onCompleteListener.invoke(task)
            }
    }

    fun sendMessage(chatId: String, message: Message) {
        val data: HashMap<String, Any> = hashMapOf(
            "chatId" to chatId
        )
        data.putAll(message.toMap())

        firebaseFunctions
            .getHttpsCallable(FUNCTION_SEND_MESSAGE)
            .call(data)
            .addOnFailureListener { Log.e(javaClass.simpleName, "sendMessage: ", it) }
    }
}

object FunctionsObjectsDeserializer {
    @JvmStatic
    inline fun <reified T> get(map: HashMap<*, *>): T {
        // TODO: 11/1/2020 refactor from strings to constant fields
        return when (T::class) {
            Feed::class -> Feed(
                map["postId"] as Int,
                map["posterUid"] as Int,
                map["posterUsername"] as String,
                map["posterImageUri"] as String,
                map["liked"] as Boolean,
                map["timestamp"] as Long,
                map["postImageUri"] as String,
                map["likeCount"] as Int,
                map["commentCount"] as Int,
                map["header"] as String,
                map["description"] as String,
            ) as T
            User::class -> User(
                map[UserFields.id] as Int,
                map[UserFields.username] as String,
                map[UserFields.imageUri] as String,
            ) as T
            else -> Any() as T
        }
    }
}

class UnknownFunctionException : Exception()