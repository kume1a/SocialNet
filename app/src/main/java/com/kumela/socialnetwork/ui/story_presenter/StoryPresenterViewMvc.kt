package com.kumela.socialnetwork.ui.story_presenter

import com.kumela.socialnetwork.models.FeedStoryModel
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 09,November,2020
 **/

interface StoryPresenterViewMvc : ObservableViewMvc<StoryPresenterViewMvc.Listener> {
    interface Listener {
        fun onCloseClicked()
        fun onPageChanged(position: Int)
    }

    fun bindStories(feedStories: List<FeedStoryModel>)
    fun bindStoryAuthor(user: User)
    fun bindImageCount(count: Int, initialIndex: Int)
    fun nextIndex()
    fun previousIndex()
    fun imageIndexTo(index: Int)
}