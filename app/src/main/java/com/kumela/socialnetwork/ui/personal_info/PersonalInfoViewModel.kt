package com.kumela.socialnetwork.ui.personal_info

import android.net.Uri
import android.util.Log
import com.kumela.socialnetwork.models.firebase.UserModel
import com.kumela.socialnetwork.network.firebase.ImageType
import com.kumela.socialnetwork.network.firebase.ImageUseCase
import com.kumela.socialnetwork.network.firebase.UserUseCase
import com.kumela.socialnetwork.ui.common.viewmodels.ObservableViewModel

/**
 * Created by Toko on 29,October,2020
 **/

class PersonalInfoViewModel : ObservableViewModel<PersonalInfoViewModel.Listener>() {
    interface Listener {
        fun onImageUploaded(uri: String)
        fun onImageUploadFailed(e: Exception)

        fun onUserCreated()
        fun onUserCreateFailed(e: Exception)
    }

    init {
        ImageUseCase.registerListener(uuid)
        UserUseCase.registerListener(uuid)
    }

    override fun onCleared() {
        super.onCleared()

        ImageUseCase.unregisterListener(uuid)
        UserUseCase.unregisterListener(uuid)
    }

    fun uploadImageAndNotify(uri: Uri, imageType: ImageType) {
        ImageUseCase.uploadImageAndNotify(uuid, imageType, uri,
            { downloadUri ->
                for (listener in listeners) {
                    listener.onImageUploaded(downloadUri)
                }
            },
            { exception ->
                for (listener in listeners) {
                    listener.onImageUploadFailed(exception)
                }
            })
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