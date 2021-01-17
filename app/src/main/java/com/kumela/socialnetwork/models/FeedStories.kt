package com.kumela.socialnetwork.models

/**
 * Created by Toko on 07,November,2020
 **/

data class FeedStoriesModel(
    val userId: String = "",
    val stories: List<FeedStoryModel> = emptyList()
)

data class FeedStoryModel(
    val storyId: String = "",
    val imageUri: String = "",
    val timestamp: Long = 0
)