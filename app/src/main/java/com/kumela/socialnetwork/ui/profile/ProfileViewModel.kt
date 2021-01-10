package com.kumela.socialnetwork.ui.profile

import android.util.Log
import com.kumela.socialnetwork.models.firebase.StoryModel
import com.kumela.socialnetwork.models.firebase.UserExtraInfoModel
import com.kumela.socialnetwork.models.firebase.UserModel
import com.kumela.socialnetwork.models.list.PostModel
import com.kumela.socialnetwork.network.firebase.PostUseCase
import com.kumela.socialnetwork.network.firebase.StoryUseCase
import com.kumela.socialnetwork.network.firebase.UserUseCase
import com.kumela.socialnetwork.network.firebase.helpers.QueryPager
import com.kumela.socialnetwork.ui.common.viewmodels.ObservableViewModel

/**
 * Created by Toko on 27,October,2020
 **/

class ProfileViewModel(
    private val storyQueryPager: QueryPager<StoryModel>
) : ObservableViewModel<ProfileViewModel.Listener>() {

    interface Listener {
        fun onUserFetched(userModel: UserModel)
        fun onUserExtraInfoFetched(userExtraInfoModel: UserExtraInfoModel)
        fun onPostsFetched(posts: List<PostModel>)
        fun onStoriesFetched(storyModels: List<StoryModel>)
    }

    // cached data
    private var userModel: UserModel? = null
    private var userExtraInfoModel: UserExtraInfoModel? = null
    private var postList: List<PostModel>? = null
    private val storyList = ArrayList<StoryModel>()

    fun getUser(): UserModel? = userModel
    fun getUserExtraInfo(): UserExtraInfoModel? = userExtraInfoModel
    fun getUserPosts(): List<PostModel>? = postList
    fun getUserStories(): List<StoryModel> = storyList

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
                this.userModel = userModel
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
                this.userExtraInfoModel = userExtraInfoModel
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