package com.kumela.socialnet.ui.story_presenter

import com.kumela.socialnet.models.firebase.FeedStoryModel
import com.kumela.socialnet.models.firebase.UserModel
import com.kumela.socialnet.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 09,November,2020
 **/

interface StoryPresenterViewMvc : ObservableViewMvc<StoryPresenterViewMvc.Listener> {
    interface Listener {
        fun onCloseClicked()
        fun onPageChanged(position: Int)
    }

    fun bindStories(feedStories: List<FeedStoryModel>)
    fun bindStoryAuthor(userModel: UserModel)
    fun bindImageCount(count: Int, initialIndex: Int)
    fun nextIndex()
    fun previousIndex()
    fun imageIndexTo(index: Int)
}