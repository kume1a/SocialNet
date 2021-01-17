package com.kumela.socialnetwork.ui.post_image

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.models.list.Post
import com.kumela.socialnetwork.ui.common.viewmodels.ObservableViewModel
import com.kumela.socialnetwork.network.firebase.*
import kotlinx.coroutines.launch

/**
 * Created by Toko on 29,October,2020
 **/

class PostImageViewModel : ObservableViewModel<PostImageViewModel.Listener>() {
    interface Listener {
        fun onUserFetched(user: User)

        fun onCreatePost()
        fun onCreatePostFailed(e: Exception)
        fun onImageUploaded(uri: String)
        fun onImageUploadFailed(e: Exception)
    }

    // cached data
    private var user: User? = null

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
        if (user == null) {
            UserUseCase.fetchUserAndNotify(uuid, UserUseCase.uid,
                { user ->
                    this.user = user
                    for (listener in listeners) {
                        listener.onUserFetched(user)
                    }
                },
                { error ->
                    Log.e(javaClass.simpleName, "fetchUserAndNotify: ", error.toException())
                })
        } else {
            for (listener in listeners) {
                listener.onUserFetched(user!!)
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

    fun createPostAndNotify(post: Post) {
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