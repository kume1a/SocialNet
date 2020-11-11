package com.kumela.socialnet.network.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.kumela.socialnet.common.UseCase
import com.kumela.socialnet.network.firebase.helpers.DatabaseHelper
import com.kumela.socialnet.network.firebase.helpers.FunctionsHelper
import com.kumela.socialnet.network.firebase.helpers.UnknownFunctionException
import java.util.*

/**
 * Created by Toko on 18,October,2020
 **/


object FollowUseCase : UseCase() {

    fun getFollowingUsersRef(userId: String): DatabaseReference =
        DatabaseHelper.getFollowingTableRef().child(userId)

    fun getFollowersRef(userId: String): DatabaseReference =
        DatabaseHelper.getFollowersTableRef().child(userId)

    inline fun followUserAndNotify(
        uuid: UUID,
        uid: String,
        crossinline onSuccessListener: () -> Unit,
        crossinline onFailureListener: (Exception) -> Unit
    ) {
        FunctionsHelper.followUser(UserUseCase.uid, uid) { task ->
            if (isActive(uuid)) {
                if (task.isSuccessful) {
                    onSuccessListener.invoke()
                } else {
                    onFailureListener.invoke(task.exception ?: UnknownFunctionException())
                }
            }
        }
    }

    inline fun unFollowUserAndNotify(
        uuid: UUID,
        uid: String,
        crossinline onSuccessListener: () -> Unit,
        crossinline onFailureListener: (Exception) -> Unit
    ) {
        FunctionsHelper.unFollowUser(UserUseCase.uid, uid) { task ->
            if (isActive(uuid)) {
                if (task.isSuccessful) {
                    onSuccessListener.invoke()
                } else {
                    onFailureListener.invoke(task.exception ?: UnknownFunctionException())
                }
            }
        }
    }

    inline fun getIfUserFollows(
        uuid: UUID,
        uid: String,
        crossinline onSuccessListener: (Boolean) -> Unit,
        crossinline onFailureListener: (DatabaseError) -> Unit
    ) {
        DatabaseHelper
            .getFollowingTableRef()
            .child(UserUseCase.uid)
            .child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (isActive(uuid)) {
                        onSuccessListener.invoke(snapshot.exists())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (isActive(uuid)) {
                        onFailureListener.invoke(error)
                    }
                }
            })
    }
}