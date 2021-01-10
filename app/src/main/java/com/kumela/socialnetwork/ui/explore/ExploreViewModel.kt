package com.kumela.socialnetwork.ui.explore

import android.util.Log
import com.kumela.socialnetwork.models.list.PostModel
import com.kumela.socialnetwork.network.firebase.PostUseCase
import com.kumela.socialnetwork.network.firebase.helpers.QueryPager
import com.kumela.socialnetwork.ui.common.viewmodels.ObservableViewModel

/**
 * Created by Toko on 05,November,2020
 **/

class ExploreViewModel(private val postsQueryPager: QueryPager<PostModel>) :
    ObservableViewModel<ExploreViewModel.Listener>() {

    interface Listener {
        fun onPostsFetched(postModels: List<PostModel>)
    }

    // cached data
    private val postModels = ArrayList<PostModel>()

    fun getPostModels(): List<PostModel> = postModels

    init {
        postsQueryPager.registerListener(uuid)
    }

    override fun onCleared() {
        super.onCleared()

        postsQueryPager.unregisterListener(uuid)
    }

    fun fetchNextPostPageAndNotify() {
        postsQueryPager.fetchNextPageAndNotify(uuid, PostUseCase.getPostsRef(),
            onSuccessListener = { postModels ->
                if (postModels != null && postModels.isNotEmpty()) {
                    this.postModels.addAll(postModels)
                    for (listener in listeners) {
                        listener.onPostsFetched(postModels)
                    }
                }
            },
            onFailureListener = { databaseError ->
                Log.e(javaClass.simpleName, "fetchNextPostPageAndNotify: ", databaseError.toException())
            })
    }
}