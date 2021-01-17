package com.kumela.socialnetwork.network.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.kumela.socialnetwork.common.UseCase
import com.kumela.socialnetwork.models.PostFields
import com.kumela.socialnetwork.models.Comment
import com.kumela.socialnetwork.models.Feed
import com.kumela.socialnetwork.models.list.Post
import com.kumela.socialnetwork.network.firebase.helpers.DatabaseHelper
import com.kumela.socialnetwork.network.firebase.helpers.FunctionsHelper
import com.kumela.socialnetwork.network.firebase.helpers.FunctionsObjectsDeserializer
import com.kumela.socialnetwork.network.firebase.helpers.UnknownFunctionException
import java.util.*

/**
 * Created by Toko on 16,October,2020
 **/

object PostUseCase : UseCase() {

    fun getPostLikersRef(postId: String): DatabaseReference =
        DatabaseHelper.getLikesTableRef().child(postId)

    fun getPostsRef(): DatabaseReference =
        DatabaseHelper.getPostTableRef()

    fun getFeedPostIdsDatabaseRef(): DatabaseReference =
        DatabaseHelper.getUserFeedPostsRef().child(UserUseCase.uid)

    inline fun createPost(
        uuid: UUID,
        post: Post,
        crossinline onSuccessListener: () -> Unit,
        crossinline onFailureListener: (Exception) -> Unit
    ) {
        FunctionsHelper.createPost(post) { task ->
            if (isActive(uuid)) {
                if (task.isSuccessful) {
                    onSuccessListener.invoke()
                } else {
                    onFailureListener.invoke(task.exception ?: UnknownFunctionException())
                }
            }
        }
    }

    inline fun fetchUserPosts(
        uuid: UUID,
        uid: String = UserUseCase.uid,
        crossinline onSuccessListener: (List<Post>) -> Unit,
        crossinline onFailureListener: (DatabaseError) -> Unit
    ) {
        DatabaseHelper
            .getPostTableRef()
            .orderByChild(PostFields.posterUid)
            .startAt(uid)
            .endAt("$uid\uf8ff")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (isActive(uuid)) {
                        val posts = snapshot.children.map { it.getValue<Post>()!! }
                        onSuccessListener.invoke(posts)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (isActive(uuid)) onFailureListener.invoke(error)
                }
            })
    }

    inline fun fetchFeedPost(
        uuid: UUID,
        postId: String,
        crossinline onSuccessListener: (Feed) -> Unit,
        crossinline onFailureListener: (Exception) -> Unit
    ) {
        FunctionsHelper.getFeedPost(postId) { task ->
            if (isActive(uuid)) {
                if (task.isSuccessful) {
                    val dataAsMap = task.result!!.data as HashMap<*, *>
                    val feedModel = FunctionsObjectsDeserializer.get<Feed>(dataAsMap)

                    onSuccessListener.invoke(feedModel)
                } else {
                    onFailureListener.invoke(task.exception ?: UnknownFunctionException())
                }
            }
        }
    }

    inline fun createComment(
        uuid: UUID,
        postId: String,
        comment: Comment,
        crossinline onSuccessListener: () -> Unit,
        crossinline onFailureListener: (Exception) -> Unit
    ) {
        DatabaseHelper
            .getCommentsTableRef()
            .child(postId)
            .push()
            .setValue(comment)
            .addOnSuccessListener {
                if (isActive(uuid)) onSuccessListener.invoke()
            }
            .addOnFailureListener { exception ->
                if (isActive(uuid)) onFailureListener.invoke(exception)
            }
    }

    inline fun fetchComments(
        uuid: UUID,
        postId: String,
        crossinline onSuccessListener: (List<Comment>) -> Unit,
        crossinline onFailureListener: (DatabaseError) -> Unit
    ) {
        DatabaseHelper
            .getCommentsTableRef()
            .child(postId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (isActive(uuid)) {
                        val comments = snapshot.children.map { it.getValue<Comment>()!! }
                        onSuccessListener.invoke(comments)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (isActive(uuid)) onFailureListener.invoke(error)
                }
            })
    }

    inline fun likeOrDislikePost(
        uuid: UUID,
        postId: String,
        crossinline onSuccessListener: (Boolean) -> Unit,
        crossinline onFailureListener: (Exception) -> Unit
    ) {
        FunctionsHelper.likeOrDislikePost(UserUseCase.uid, postId) { task ->
            if (isActive(uuid)) {
                if (task.isSuccessful) {
                    val data = task.result!!.data as Boolean
                    onSuccessListener.invoke(data)
                } else {
                    onFailureListener.invoke(task.exception ?: UnknownFunctionException())
                }
            }
        }

    }
}