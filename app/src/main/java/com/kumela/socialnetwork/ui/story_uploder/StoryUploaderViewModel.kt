package com.kumela.socialnetwork.ui.story_uploder

import android.net.Uri
import android.util.Log
import com.kumela.socialnetwork.models.Story
import com.kumela.socialnetwork.network.firebase.ImageType
import com.kumela.socialnetwork.network.firebase.ImageUseCase
import com.kumela.socialnetwork.network.firebase.StoryUseCase
import com.kumela.socialnetwork.ui.common.viewmodels.ObservableViewModel

/**
 * Created by Toko on 07,November,2020
 **/

class StoryUploaderViewModel : ObservableViewModel<StoryUploaderViewModel.Listener>() {

    interface Listener {
        fun onStoryUploaded()
        fun onStoryUploadFailed()
    }

    init {
        StoryUseCase.registerListener(uuid)
        ImageUseCase.registerListener(uuid)
    }

    override fun onCleared() {
        super.onCleared()

        StoryUseCase.unregisterListener(uuid)
        ImageUseCase.unregisterListener(uuid)
    }

    fun uploadStoryAndNotify(imageUri: Uri) {
        ImageUseCase.uploadImageAndNotify(uuid, ImageType.STORY, imageUri,
            onSuccessListener = { downloadUri ->
                StoryUseCase.createStory(uuid, Story(System.currentTimeMillis(), downloadUri),
                    onSuccessListener = {
                        for (listener in listeners) {
                            listener.onStoryUploaded()
                        }
                    },
                    onFailureListener = { exception ->
                        Log.e(javaClass.simpleName, "uploadStoryAndNotify: ", exception)
                        for (listener in listeners) {
                            listener.onStoryUploadFailed()
                        }
                    })
            },
            onFailureListener = { exception ->
                Log.e(javaClass.simpleName, "uploadStoryAndNotify: ", exception)
            })
    }
}