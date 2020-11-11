package com.kumela.socialnet.ui.story_presenter

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.kumela.socialnet.models.firebase.FeedStoriesModel
import com.kumela.socialnet.models.firebase.UserModel
import com.kumela.socialnet.network.firebase.StoryUseCase
import com.kumela.socialnet.network.firebase.UserUseCase
import com.kumela.socialnet.ui.common.utils.isOutdated
import com.kumela.socialnet.ui.common.viewmodels.ObservableViewModel
import kotlinx.coroutines.launch

/**
 * Created by Toko on 09,November,2020
 **/

class StoryViewModel : ObservableViewModel<StoryViewModel.Listener>() {
    interface Listener {
        fun onStoryPosterFetched(user: UserModel)
    }

    private var feedStories: List<FeedStoriesModel>? = null
    private val feedStoryPosters = ArrayList<UserModel>()

    fun getFeedStories(): List<FeedStoriesModel>? = feedStories
    fun getStoryPosters(): List<UserModel> = feedStoryPosters

    init {
        StoryUseCase.registerListener(uuid)
        UserUseCase.registerListener(uuid)
    }

    override fun onCleared() {
        super.onCleared()

        StoryUseCase.unregisterListener(uuid)
        UserUseCase.unregisterListener(uuid)
    }

    fun fetchStoryPostersAndNotify() {
        viewModelScope.launch {
            StoryUseCase.fetchFeedStoriesAndNotify(uuid,
                onSuccessListener = { feedStories ->
                    for (feedStoriesModel in feedStories) {
                        for (story in feedStoriesModel.stories) {
                            if (story.timestamp.isOutdated()) {
                                StoryUseCase.deleteFeedStory(feedStoriesModel.userId, story.storyId)
                            }
                        }
                    }

                    this@StoryViewModel.feedStories = feedStories
                    for (feedStory in feedStories) {
                        fetchUserAndNotifyListener(feedStory.userId)
                    }
                },
                onFailureListener = { databaseError ->
                    Log.e(javaClass.simpleName, "fetchStoryPostersAndNotify: ", databaseError.toException())
                })
        }
    }

    private fun fetchUserAndNotifyListener(userId: String) {
        UserUseCase.fetchUserAndNotify(uuid, userId,
            onSuccessListener = { userModel ->
                feedStoryPosters.add(userModel)
                for (listener in listeners) {
                    listener.onStoryPosterFetched(userModel)
                }
            },
            onFailureListener = { databaseError ->
                Log.e(javaClass.simpleName, "fetchUserAndNotifyListener: ", databaseError.toException())
            })
    }
}