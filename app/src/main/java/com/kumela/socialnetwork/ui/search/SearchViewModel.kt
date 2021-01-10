package com.kumela.socialnetwork.ui.search

import android.util.Log
import com.kumela.socialnetwork.models.firebase.UserModel
import com.kumela.socialnetwork.network.firebase.UserUseCase
import com.kumela.socialnetwork.ui.common.viewmodels.ObservableViewModel

/**
 * Created by Toko on 27,October,2020
 **/

class SearchViewModel : ObservableViewModel<SearchViewModel.Listener>() {
    interface Listener {
        fun onUsersFetched(users: List<UserModel>)
    }

    var users: List<UserModel>? = null

    init {
        UserUseCase.registerListener(uuid)
    }

    override fun onCleared() {
        super.onCleared()

        UserUseCase.unregisterListener(uuid)
    }

    fun fetchUsersByNameAndNotify(name: String) {
        UserUseCase.fetchUsersByNameAndNotify(uuid, name,
            { users ->
                this.users = users
                for (listener in listeners) {
                    listener.onUsersFetched(users)
                }
            },
            { databaseError ->
                Log.e(
                    javaClass.simpleName,
                    "fetchUsersByNameAndNotify: ",
                    databaseError.toException()
                )
            })
    }
}