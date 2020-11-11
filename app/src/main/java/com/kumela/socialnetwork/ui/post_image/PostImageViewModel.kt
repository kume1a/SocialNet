package com.kumela.socialnet.ui.post_image

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.kumela.socialnet.models.firebase.UserModel
import com.kumela.socialnet.models.list.PostModel
import com.kumela.socialnet.network.firebase.*
import com.kumela.socialnet.ui.common.viewmodels.ObservableViewModel
import kotlinx.coroutines.launch

/**
 * Created by Toko on 29,October,2020
 **/

class PostImageViewModel : ObservableViewModel<PostImageViewModel.Listener>() {
    interface Listener {
        fun onUserFetched(userModel: UserModel)

        fun onCreatePost()
        fun onCreatePostFailed(e: Exception)
        fun onImageUploaded(uri: String)
        fun onImageUploadFailed(e: Exception)
    }

    // cached data
    private var userModel: UserModel? = null

    init {
        UserUseCase.registerListener(uuid)
        ImageUseCase.registerListener(uuid)
        PostUseCase.registerListener(uuid)
    }

    override fun onCleared() {
        super.onCleared()

        UserUseCase.unregisterListener(uuid)
        ImageUseCase.unregisterListener(uuid)
        PostUseCase.unregisterListener(uuid)
    }

    fun fetchUserAndNotify() {
        if (userModel == null) {
            UserUseCase.fetchUserAndNotify(uuid, UserUseCase.uid,
                { user ->
                    userModel = user
                    for (listener in listeners) {
                        listener.onUserFetched(user)
                    }
                },
                { error ->
                    Log.e(javaClass.simpleName, "fetchUserAndNotify: ", error.toException())
                })
        } else {
            for (listener in listeners) {
                listener.onUserFetched(userModel!!)
            }
        }
    }

    fun uploadImageAndNotify(imageType: ImageType, imageUri: Uri) {
        ImageUseCase.uploadImageAndNotify(uuid, imageType, imageUri,
            onSuccessListener = { downloadUri ->
                for (listener in listeners) {
                    listener.onImageUploaded(downloadUri)
                }
            },
            onFailureListener = { exception ->
                for (listener in listeners) {
                    listener.onImageUploadFailed(exception)
                }
            })
    }

    fun uploadImageAndNotify(imageType: ImageType, imageBitmap: Bitmap) {
        viewModelScope.launch {
            val result: Result<String, Exception> = ImageUseCase.uploadImageAndNotify(imageType, imageBitmap)
            result.fold(
                onSuccess = { value ->
                    for (listener in listeners) {
                        listener.onImageUploaded(value)
                    }
                },
                onFailure = { exception ->
                    for (listener in listeners) {
                        listener.onImageUploadFailed(exception)
                    }
                }
            )
        }
    }

    fun createPostAndNotify(post: PostModel) {
        PostUseCase.createPost(uuid, post,
            {
                for (listener in listeners) {
                    listener.onCreatePost()
                }
            },
            { exception ->
                for (listener in listeners) {
                    listener.onCreatePostFailed(exception)
                }
            })
    }
}