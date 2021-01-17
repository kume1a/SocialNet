package com.kumela.socialnetwork.network.firebase

import android.os.CountDownTimer
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.kumela.socialnetwork.common.Constants
import com.kumela.socialnetwork.common.UseCase
import com.kumela.socialnetwork.common.listeners.OnFailureListener
import com.kumela.socialnetwork.common.listeners.OnSuccessListener
import com.kumela.socialnetwork.models.UserFields
import com.kumela.socialnetwork.models.UserExtraInfo
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.network.firebase.helpers.DatabaseHelper
import com.kumela.socialnetwork.network.firebase.helpers.FunctionsHelper
import com.kumela.socialnetwork.network.firebase.helpers.FunctionsObjectsDeserializer
import com.kumela.socialnetwork.network.firebase.helpers.UnknownFunctionException
import java.util.*

/**
 * Created by Toko on 10,October,2020
 **/

object UserUseCase : UseCase() {

    val auth = Firebase.auth

    val uid: String
        get() = auth.uid!!

    fun signOut() {
        auth.signOut()
    }

    fun getFollowingUsersRef(): DatabaseReference =
        DatabaseHelper.getFollowingTableRef().child(uid)

    fun registerUserPresenceListener() {
        val ref = DatabaseHelper.getUserTableRef()

        val writer: () -> Unit = {
            auth.uid?.let { uid ->
                ref.child(uid)
                    .child(UserFields.lastOnline)
                    .setValue(System.currentTimeMillis())
            }
        }

        writer.invoke()

        object : CountDownTimer(Long.MAX_VALUE, Constants.INTERVAL_ONLINE_UPDATE) {
            override fun onTick(millisUntilFinished: Long) {
                writer.invoke()
            }

            override fun onFinish() {}
        }.start()
    }

    fun fetchUserAndNotify(
        uuid: UUID,
        uid: String?,
        onSuccessListener: OnSuccessListener<User>,
        onFailureListener: OnFailureListener<DatabaseError>
    ) {
        val userRef = DatabaseHelper.getUserTableRef()

        userRef.child(uid ?: UserUseCase.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (isActive(uuid)) {
                        onSuccessListener.onSuccess(snapshot.getValue<User>()!!)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailureListener.onFailure(error)
                }
            })
    }

    inline fun getIfUserExtraInfoExists(
        uuid: UUID,
        crossinline onSuccessListener: (Boolean) -> Unit,
        crossinline onFailureListener: (DatabaseError) -> Unit
    ) {
        DatabaseHelper
            .getUserExtraInfoTableRef()
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

    fun fetchUserExtraInfoAndNotify(
        uuid: UUID,
        uid: String?,
        onSuccessListener: OnSuccessListener<UserExtraInfo>,
        onFailureListener: OnFailureListener<DatabaseError>
    ) {
        val userExtraInfoRef = DatabaseHelper.getUserExtraInfoTableRef()

        userExtraInfoRef.child(uid ?: UserUseCase.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (isActive(uuid)) {
                        val userExtraInfo = snapshot.getValue<UserExtraInfo>()!!
                        onSuccessListener.onSuccess(userExtraInfo)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (isActive(uuid)) {
                        onFailureListener.onFailure(error)
                    }
                }
            })
    }

    fun fetchUsersByNameAndNotify(
        uuid: UUID,
        name: String,
        onSuccessListener: OnSuccessListener<List<User>>,
        onFailureListener: OnFailureListener<DatabaseError>
    ) {
        val ref = DatabaseHelper.getUserTableRef()

        ref.orderByChild(UserFields.username)
            .startAt(name)
            .endAt("$name\uf8ff")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!isActive(uuid)) return

                    val users = snapshot.children
                        .map { it.getValue<User>()!! }
//                        .filter { it.id != uid }

                    onSuccessListener.onSuccess(users)
                }

                override fun onCancelled(error: DatabaseError) {
                    if (isActive(uuid)) {
                        onFailureListener.onFailure(error)
                    }
                }
            })
    }

    fun getFollowingUsers(
        uuid: UUID,
        uid: String,
        onSuccessListener: OnSuccessListener<List<User>>,
        onFailureListener: OnFailureListener<Exception>,
    ) {
        FunctionsHelper.getFollowingUsers(uid) { task ->
            if (isActive(uuid)) {
                if (task.isSuccessful) {
                    val listOfUsers = (task.result!!.data as List<*>).map { item ->
                        FunctionsObjectsDeserializer.get<User>(item as HashMap<*, *>)
                    }
                    onSuccessListener.onSuccess(listOfUsers)
                } else {
                    onFailureListener.onFailure(task.exception ?: UnknownFunctionException())
                }
            }
        }
    }
}