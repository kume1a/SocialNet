package com.kumela.socialnetwork.ui.story_uploder

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.kumela.socialnetwork.models.Story
import com.kumela.socialnetwork.network.NetworkError
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.repositories.ImageRepository
import com.kumela.socialnetwork.network.repositories.ImageType
import com.kumela.socialnetwork.network.repositories.StoryRepository

/**
 * Created by Toko on 07,November,2020
 **/

class StoryUploaderViewModel(
    private val storyRepository: StoryRepository,
    private val imageRepository: ImageRepository
) : ViewModel() {
    suspend fun createStory(imageUrl: String): Result<Story, NetworkError> {
        return storyRepository.createStory(imageUrl)
    }

    suspend fun uploadImage(imageUri: Uri): Result<String, Exception> {
       return imageRepository.uploadImage(ImageType.STORY, imageUri)
    }
}