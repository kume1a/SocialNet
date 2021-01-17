package com.kumela.socialnetwork.ui.home

import android.util.Log
import com.kumela.socialnetwork.models.Feed
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.network.firebase.PostUseCase
import com.kumela.socialnetwork.network.firebase.UserUseCase
import com.kumela.socialnetwork.network.firebase.helpers.QueryPager
import com.kumela.socialnetwork.ui.common.viewmodels.ObservableViewModel

/**
 * Created by Toko on 23,October,2020
 **/

class HomeViewModel(
    private val feedPostIdsQueryPager: QueryPager<String>
) :
    ObservableViewModel<HomeViewModel.Listener>() {

    interface Listener {
        fun onUserFetched(user: User)
        fun onPostFetched(feedModel: Feed)
        fun onPostUpdated(position: Int, feedModel: Feed)
    }

    // cached data
    private var user: User? = null
    private val postIds = ArrayList<String>()
    private val posts = ArrayList<Feed>()

    // variable for pagination
    private var postOffset = 0

    // control var for preventing double trigger
    private val pendingLikeResults = HashSet<String>()

    init {
        feedPostIdsQueryPager.registerListener(uuid)
        UserUseCase.registerListener(uuid)
        PostUseCase.registerListener(uuid)
    }

    override fun onCleared() {
        feedPostIdsQueryPager.unregisterListener(uuid)
        UserUseCase.unregisterListener(uuid)
        PostUseCase.unregisterListener(uuid)
    }

    fun getPosts(): List<Feed> = posts
    fun getUser(): User? = user

    fun fetchUserAndNotify(uid: String? = null) {
        UserUseCase.fetchUserAndNotify(uuid, uid,
            { user ->
                this.user = user
                for (listener in listeners) {
                    listener.onUserFetched(user)
                }
            },
            { error ->
                Log.e(javaClass.simpleName, "fetchUserAndNotify: ", error.toException())
            })
    }

    fun fetchNextPostAndNotify() {
        if (postIds.size == postOffset) {
            feedPostIdsQueryPager.fetchNextPageAndNotify(uuid, PostUseCase.getFeedPostIdsDatabaseRef(),
                { feedPostIds ->
                    if (feedPostIds != null && feedPostIds.isNotEmpty()) {
                        postIds.addAll(feedPostIds)
                        getPostAndNotifyListeners()
                    }
                },
                { databaseError ->
                    Log.e(javaClass.simpleName, "fetchNextPostAndNotify: ", databaseError.toException())
                })
        } else {
            getPostAndNotifyListeners()
        }
    }

    fun likeOrDislikePostAndNotify(position: Int, feedModel: Feed) {
        if (pendingLikeResults.contains(feedModel.postId)) return
        pendingLikeResults.add(feedModel.postId)

        PostUseCase.likeOrDislikePost(uuid, feedModel.postId,
            onSuccessListener = { liked ->
                val currentLikeCount = if (liked) feedModel.likeCount + 1 else feedModel.likeCount - 1

                val newFeedModel = feedModel.copy(
                    likeCount = currentLikeCount,
                    liked = liked
                )

                // update feed model in cache
                val indexOfOldFeedModel = this.posts.indexOfFirst { it.postId == feedModel.postId }
                this.posts[indexOfOldFeedModel] = newFeedModel

                for (listener in listeners) {
                    listener.onPostUpdated(position, newFeedModel)
                }
                pendingLikeResults.remove(feedModel.postId)
            },
            onFailureListener = { exception ->
                pendingLikeResults.remove(feedModel.postId)
                Log.e(javaClass.simpleName, "likeOrDislikePostAndNotify: ", exception)
            })
    }

    private fun getPostAndNotifyListeners() {
        PostUseCase.fetchFeedPost(uuid, postIds[postOffset],
            { feedModel ->
                postOffset++
                posts.add(feedModel)
                for (listener in listeners) {
                    listener.onPostFetched(feedModel)
                }
            },
            { exception ->
                Log.e(javaClass.simpleName, "getPostAndNotifyListeners: ", exception)
            }
        )
    }
}