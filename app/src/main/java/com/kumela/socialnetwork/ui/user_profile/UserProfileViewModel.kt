package com.kumela.socialnet.ui.user_profile

import android.util.Log
import com.kumela.socialnet.models.firebase.StoryModel
import com.kumela.socialnet.models.firebase.UserExtraInfoModel
import com.kumela.socialnet.models.list.PostModel
import com.kumela.socialnet.network.firebase.FollowUseCase
import com.kumela.socialnet.network.firebase.PostUseCase
import com.kumela.socialnet.network.firebase.StoryUseCase
import com.kumela.socialnet.network.firebase.UserUseCase
import com.kumela.socialnet.network.firebase.helpers.QueryPager
import com.kumela.socialnet.ui.common.viewmodels.ObservableViewModel

/**
 * Created by Toko on 27,October,2020
 **/

class UserProfileViewModel(
    private val storyQueryPager: QueryPager<StoryModel>
) : ObservableViewModel<UserProfileViewModel.Listener>() {

    interface Listener {
        fun onUserExtraInfoFetched(userExtraInfoModel: UserExtraInfoModel)
        fun onPostsFetched(posts: List<PostModel>)
        fun onFollowResultReceived(following: Boolean)
        fun onFollowUnFollowCompleted()
        fun onStoriesFetched(storyModels: List<StoryModel>)
    }

    // cached data
    private var userExtraInfo: UserExtraInfoModel? = null
    private var userPosts: List<PostModel>? = null
    private val storyList = ArrayList<StoryModel>()

    fun getUserExtraInfo(): UserExtraInfoModel? = userExtraInfo
    fun getUserPosts(): List<PostModel>? = userPosts
    fun getUserStories(): List<StoryModel> = storyList

    init {
        storyQueryPager.registerListener(uuid)
        UserUseCase.registerListener(uuid)
        PostUseCase.registerListener(uuid)
        FollowUseCase.registerListener(uuid)
    }

    override fun onCleared() {
        super.onCleared()

        storyQueryPager.unregisterListener(uuid)
        UserUseCase.unregisterListener(uuid)
        PostUseCase.unregisterListener(uuid)
        FollowUseCase.unregisterListener(uuid)
    }

    fun fetchUserExtraInfoAndNotify(uid: String) {
        UserUseCase.fetchUserExtraInfoAndNotify(uuid, uid,
            onSuccessListener = { userExtraInfoModel ->
                userExtraInfo = userExtraInfoModel
                for (listener in listeners) {
                    listener.onUserExtraInfoFetched(userExtraInfoModel)
                }
            },
            onFailureListener = { databaseError ->
                Log.e(
                    javaClass.simpleName,
                    "fetchUserExtraInfoAndNotify: ",
                    databaseError.toException()
                )
            })
    }


    fun fetchUserPostsAndNotify(uid: String) {
        PostUseCase.fetchUserPosts(uuid, uid,
            onSuccessListener = { postModels ->
                this.userPosts = postModels
                for (listener in listeners) {
                    listener.onPostsFetched(postModels)
                }
            },
            onFailureListener = { databaseError ->
                Log.e(javaClass.simpleName, "fetchUserPostsAndNotify: ", databaseError.toException())

            })
    }


    fun fetchIfUserFollowsAndNotify(uid: String) {
        FollowUseCase.getIfUserFollows(uuid, uid,
            onSuccessListener = { following ->
                for (listener in listeners) {
                    listener.onFollowResultReceived(following)
                }
            },
            onFailureListener = { databaseError ->
                Log.e(javaClass.simpleName, "fetchIfUserFollowsAndNotify: ", databaseError.toException())
            })
    }

    fun followUserAndNotify(uid: String) {
        FollowUseCase.followUserAndNotify(uuid, uid,
            onSuccessListener = {
                for (listener in listeners) {
                    listener.onFollowUnFollowCompleted()
                }
            },
            onFailureListener = { exception ->
                Log.e(javaClass.simpleName, "followUserAndNotify: ", exception)
            })
    }

    fun unFollowUserAndNotify(uid: String) {
        FollowUseCase.unFollowUserAndNotify(uuid, uid,
            onSuccessListener = {
                for (listener in listeners) {
                    listener.onFollowUnFollowCompleted()
                }
            },
            onFailureListener = { exception ->
                Log.e(javaClass.simpleName, "unFollowUserAndNotify: ", exception)
            })
    }

    fun fetchNextStoriesPageAndNotify(uid: String) {
        storyQueryPager.fetchNextPageAndNotify(uuid, StoryUseCase.getUserStoriesRef(uid),
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