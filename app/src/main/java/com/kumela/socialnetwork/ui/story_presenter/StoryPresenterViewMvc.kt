package com.kumela.socialnetwork.ui.story_presenter

import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 09,November,2020
 **/

interface StoryPresenterViewMvc : ObservableViewMvc<StoryPresenterViewMvc.Listener> {
    interface Listener {
        fun onCloseClicked()
        fun onUserClicked()

        fun onReverse()
        fun onSkip()
        fun onPressDown(): Boolean
        fun onPressUp(): Boolean

        fun onNext()
        fun onPrevious()
        fun onBack()
        fun onComplete()
    }

    fun bindUser(user: User)
    fun bindImage(imageUrl: String)
    fun bindCount(count: Int)
    fun start()

    fun pause()
    fun resume()

    fun reverse()
    fun skip()
}