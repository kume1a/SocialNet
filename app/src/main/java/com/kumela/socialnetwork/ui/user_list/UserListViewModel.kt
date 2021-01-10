package com.kumela.socialnetwork.ui.user_list

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.kumela.socialnetwork.models.firebase.UserModel
import com.kumela.socialnetwork.network.firebase.FollowUseCase
import com.kumela.socialnetwork.network.firebase.PostUseCase
import com.kumela.socialnetwork.network.firebase.UserUseCase
import com.kumela.socialnetwork.network.firebase.helpers.QueryPager
import com.kumela.socialnetwork.ui.common.viewmodels.ObservableViewModel

/**
 * Created by Toko on 05,November,2020
 **/

class UserListViewModel(
    private val usersListQueryPager: QueryPager<String>
) : ObservableViewModel<UserListViewModel.Listener>() {

    interface Listener {
        fun onUserFetched(userModel: UserModel)
    }

    // cached data
    private val userModels = ArrayList<UserModel>()
    private val userIds = ArrayList<String>()

    private var userIdOffset = 0

    init {
        usersListQueryPager.registerListener(uuid)
        UserUseCase.registerListener(uuid)
    }

    override fun onCleared() {
        super.onCleared()

        usersListQueryPager.unregisterListener(uuid)
        UserUseCase.unregisterListener(uuid)
    }

    fun getUserModels() = userModels

    fun fetchFollowingNextPageAndNotify(userId: String) {
        fetchNextPage(FollowUseCase.getFollowingUsersRef(userId))
    }

    fun fetchFollowersNextPageAndNotify(userId: String) {
        fetchNextPage(FollowUseCase.getFollowersRef(userId))
    }

    fun fetchPostLikersAndNotify(postId: String) {
        fetchNextPage(PostUseCase.getPostLikersRef(postId))
    }

    private fun fetchNextPage(runtimeRef: DatabaseReference) {
        if (userIds.size == userIdOffset) {
            usersListQueryPager.fetchNextPageAndNotify(
                uuid, runtimeRef,
                { userIds ->
                    if (userIds != null && userIds.isNotEmpty()) {
                        this.userIds.addAll(userIds)
                        for (userId in userIds) {
                            fetchUserAndNotifyListeners(userId)
                        }
                    }
                },
                { databaseError ->
                    Log.e(javaClass.simpleName, "fetchNextPageAndNotify: ", databaseError.toException())
                },
            )
        } else {
            fetchUserAndNotifyListeners()
        }
    }

    private fun fetchUserAndNotifyListeners(userId: String? = null) {
        UserUseCase.fetchUserAndNotify(uuid, userId ?: userIds[userIdOffset],
            { userModel ->
                userIdOffset++
                userModels.add(userModel)
                for (listener in listeners) {
                    listener.onUserFetched(userModel)
                }
            },
            { databaseError ->
                Log.e(javaClass.simpleName, "fetchUserAndNotifyListeners: ", databaseError.toException())
            })
    }
}