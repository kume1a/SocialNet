package com.kumela.socialnetwork.ui.login

import android.util.Log
import com.kumela.socialnetwork.network.firebase.UserUseCase
import com.kumela.socialnetwork.ui.common.viewmodels.ObservableViewModel

/**
 * Created by Toko on 11,November,2020
 **/

class LoginViewModel : ObservableViewModel<LoginViewModel.Listener>() {
    interface Listener {
        fun onUserExtraInfoExistsResult(exists: Boolean)
    }

    init {
        UserUseCase.registerListener(uuid)
    }

    override fun onCleared() {
        super.onCleared()

        UserUseCase.unregisterListener(uuid)
    }

    fun fetchIfUserExtraInfoExistsAndNotify() {
        UserUseCase.getIfUserExtraInfoExists(uuid,
            onSuccessListener = { exists ->
                for (listener in listeners) {
                    listener.onUserExtraInfoExistsResult(exists)
                }
            },
            onFailureListener = { databaseError ->
                Log.e(javaClass.simpleName, "fetchUserExtraInfoAndNotify: ", databaseError.toException())
            })
    }
}