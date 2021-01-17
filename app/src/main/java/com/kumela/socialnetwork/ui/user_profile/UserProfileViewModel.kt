package com.kumela.socialnetwork.ui.user_profile

import android.util.Log
import com.kumela.socialnetwork.models.Story
import com.kumela.socialnetwork.models.UserExtraInfo
import com.kumela.socialnetwork.models.list.Post
import com.kumela.socialnetwork.network.firebase.FollowUseCase
import com.kumela.socialnetwork.network.firebase.PostUseCase
import com.kumela.socialnetwork.network.firebase.StoryUseCase
import com.kumela.socialnetwork.network.firebase.UserUseCase
import com.kumela.socialnetwork.network.firebase.helpers.QueryPager
import com.kumela.socialnetwork.ui.common.viewmodels.ObservableViewModel

/**
 * Created by Toko on 27,October,2020
 **/

class UserProfileViewModel(
    private val storyQueryPager: QueryPager<Story>
) : ObservableViewModel<UserProfileViewModel.Listener>() {

    interface Listener {
        fun onUserExtraInfoFetched(userExtraInfo: UserExtraInfo)
        fun onPostsFetched(posts: List<Post>)
        fun onFollowResultReceived(following: Boolean)
        fun onFollowUnFollowCompleted()
        fun onStoriesFetched(stories: List<Story>)
    }

    // cached data
    private var userExtraInfo: UserExtraInfo? = null
    private var userPosts: List<Post>? = null
    private val storyList = ArrayList<Story>()

    fun getUserExtraInfo(): UserExtraInfo? = userExtraInfo
    fun getUserPosts(): List<Post>? = userPosts
    fun getUserStories(): List<Story> = storyList

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