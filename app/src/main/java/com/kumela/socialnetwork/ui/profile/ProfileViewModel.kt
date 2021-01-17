package com.kumela.socialnetwork.ui.profile

import android.util.Log
import com.kumela.socialnetwork.models.Story
import com.kumela.socialnetwork.models.UserExtraInfo
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.models.list.Post
import com.kumela.socialnetwork.network.firebase.PostUseCase
import com.kumela.socialnetwork.network.firebase.StoryUseCase
import com.kumela.socialnetwork.network.firebase.UserUseCase
import com.kumela.socialnetwork.network.firebase.helpers.QueryPager
import com.kumela.socialnetwork.ui.common.viewmodels.ObservableViewModel

/**
 * Created by Toko on 27,October,2020
 **/

class ProfileViewModel(
    private val storyQueryPager: QueryPager<Story>
) : ObservableViewModel<ProfileViewModel.Listener>() {

    interface Listener {
        fun onUserFetched(user: User)
        fun onUserExtraInfoFetched(userExtraInfo: UserExtraInfo)
        fun onPostsFetched(posts: List<Post>)
        fun onStoriesFetched(stories: List<Story>)
    }

    // cached data
    private var user: User? = null
    private var userExtraInfo: UserExtraInfo? = null
    private var postList: List<Post>? = null
    private val storyList = ArrayList<Story>()

    fun getUser(): User? = user
    fun getUserExtraInfo(): UserExtraInfo? = userExtraInfo
    fun getUserPosts(): List<Post>? = postList
    fun getUserStories(): List<Story> = storyList

    init {
        storyQueryPager.registerListener(uuid)
        UserUseCase.registerListener(uuid)
        PostUseCase.registerListener(uuid)
    }

    override fun onCleared() {
        super.onCleared()

        storyQueryPager.unregisterListener(uuid)
        UserUseCase.unregisterListener(uuid)
        PostUseCase.unregisterListener(uuid)
    }

    fun fetchUserAndNotify() {
        UserUseCase.fetchUserAndNotify(uuid, null,
            { userModel ->
                this.user = userModel
                for (listener in listeners) {
                    listener.onUserFetched(userModel)
                }
            },
            { databaseError ->
                Log.e(javaClass.simpleName, "fetchUserAndNotify: ", databaseError.toException())
            })
    }

    fun fetchUserExtraInfoAndNotify() {
        UserUseCase.fetchUserExtraInfoAndNotify(uuid, null,
            { userExtraInfoModel ->
                this.userExtraInfo = userExtraInfoModel
                for (listener in listeners) {
                    listener.onUserExtraInfoFetched(userExtraInfoModel)
                }
            },
            { databaseError ->
                Log.e(
                    javaClass.simpleName,
                    "fetchUserExtraInfoAndNotify: ",
                    databaseError.toException()
                )
            })
    }

    fun fetchUserPostsAndNotify() {
        PostUseCase.fetchUserPosts(uuid,
            onSuccessListener = { postModels ->
                this.postList = postModels
                for (listener in listeners) {
                    listener.onPostsFetched(postModels)
                }
            },
            onFailureListener = { databaseError ->
                Log.e(javaClass.simpleName, "fetchUserPostsAndNotify: ", databaseError.toException())
            })
    }

    fun fetchNextStoriesPageAndNotify() {
        storyQueryPager.fetchNextPageAndNotify(uuid, StoryUseCase.getUserStoriesRef(),
            onSuccessListener = { storyModels ->
                if (storyModels != null && storyModels.isNotEmpty()) {
                    this.storyList.addAll(storyModels)
                    for (listener in listeners) {
                        listener.onStoriesFetched(storyModels)
                    }
                }
            },
            onFailureListener = { databaseError ->
                Log.e(javaClass.simpleName, "fetchNextStoriesPageAndNotify: ", databaseError.toException())
            })
    }
}