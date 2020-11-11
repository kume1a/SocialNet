package com.kumela.socialnet.ui.splash

import android.util.Log
import com.kumela.socialnet.network.firebase.UserUseCase
import com.kumela.socialnet.ui.common.viewmodels.ObservableViewModel

/**
 * Created by Toko on 11,November,2020
 **/

class SplashViewModel : ObservableViewModel<SplashViewModel.Listener>() {

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