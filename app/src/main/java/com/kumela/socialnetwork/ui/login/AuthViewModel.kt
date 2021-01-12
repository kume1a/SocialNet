package com.kumela.socialnetwork.ui.login

import android.util.Log
import com.kumela.socialnetwork.models.firebase.UserModel
import com.kumela.socialnetwork.network.firebase.UserUseCase
import com.kumela.socialnetwork.ui.common.viewmodels.ObservableViewModel

/**
 * Created by Toko on 12,January,2021
 **/

class AuthViewModel : ObservableViewModel<AuthViewModel.Listener>() {
    interface Listener {
        fun onUserCreated()
        fun onUserCreateFailed(e: Exception)
    }

    init {
        UserUseCase.registerListener(uuid)
    }

    override fun onCleared() {
        super.onCleared()

        UserUseCase.unregisterListener(uuid)
    }

    fun createUserAndNotify(user: UserModel) {
        UserUseCase.createUserAndNotify(uuid, user,
            onSuccessListener = {
                for (listener in listeners) {
                    listener.onUserCreated()
                }
            },
            onFailureListener = { exception ->
                Log.e(javaClass.simpleName, "createUserAndNotify: ", exception)
                for (listener in listeners) {
                    listener.onUserCreateFailed(exception)
                }
            }
        )
    }
}