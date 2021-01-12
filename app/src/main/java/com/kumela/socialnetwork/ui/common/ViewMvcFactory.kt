package com.kumela.socialnetwork.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kumela.socialnetwork.ui.adapters.messages.MessageLeftItemViewMvc
import com.kumela.socialnetwork.ui.adapters.messages.MessageRightItemViewMvc
import com.kumela.socialnetwork.ui.adapters.explore.ExploreItemDIRViewMvc
import com.kumela.socialnetwork.ui.adapters.explore.ExploreItemTIRViewMvc
import com.kumela.socialnetwork.ui.adapters.feed.FeedItemViewMvc
import com.kumela.socialnetwork.ui.adapters.feed.FeedItemViewMvcImpl
import com.kumela.socialnetwork.ui.adapters.friends.FriendsItemViewMvc
import com.kumela.socialnetwork.ui.adapters.friends.FriendsItemViewMvcImpl
import com.kumela.socialnetwork.ui.adapters.chats.ChatItemViewMvc
import com.kumela.socialnetwork.ui.adapters.chats.ChatItemViewMvcImpl
import com.kumela.socialnetwork.ui.adapters.comments.CommentsItemViewMvc
import com.kumela.socialnetwork.ui.adapters.posts.PostItemViewMvc
import com.kumela.socialnetwork.ui.adapters.posts.PostItemViewMvcImpl
import com.kumela.socialnetwork.ui.adapters.users.UserItemViewMvc
import com.kumela.socialnetwork.ui.adapters.story.StoryItemViewMvc
import com.kumela.socialnetwork.ui.adapters.story_profile.ProfileStoryItemViewMvc
import com.kumela.socialnetwork.ui.chat.ChatViewMvc
import com.kumela.socialnetwork.ui.chat.ChatViewMvcImpl
import com.kumela.socialnetwork.ui.comments.CommentsViewMvc
import com.kumela.socialnetwork.ui.comments.CommentsViewMvcImpl
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavViewMvc
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavViewMvcImpl
import com.kumela.socialnetwork.ui.common.mvc.ViewMvc
import com.kumela.socialnetwork.ui.common.toolbar.ToolbarViewMvc
import com.kumela.socialnetwork.ui.user_list.UserListViewMvc
import com.kumela.socialnetwork.ui.user_list.UserListViewMvcImpl
import com.kumela.socialnetwork.ui.explore.ExploreViewMvc
import com.kumela.socialnetwork.ui.explore.ExploreViewMvcImpl
import com.kumela.socialnetwork.ui.home.HomeViewMvc
import com.kumela.socialnetwork.ui.home.HomeViewMvcImpl
import com.kumela.socialnetwork.ui.login.AuthViewMvc
import com.kumela.socialnetwork.ui.login.AuthViewMvcImpl
import com.kumela.socialnetwork.ui.messages.MessagesViewMvc
import com.kumela.socialnetwork.ui.messages.MessagesViewMvcImpl
import com.kumela.socialnetwork.ui.post_image.PostImageViewMvc
import com.kumela.socialnetwork.ui.post_image.PostImageViewMvcImpl
import com.kumela.socialnetwork.ui.profile.ProfileViewMvc
import com.kumela.socialnetwork.ui.profile.ProfileViewMvcImpl
import com.kumela.socialnetwork.ui.search.SearchViewMvc
import com.kumela.socialnetwork.ui.search.SearchViewMvcImpl
import com.kumela.socialnetwork.ui.splash.SplashViewMvc
import com.kumela.socialnetwork.ui.splash.SplashViewMvcImpl
import com.kumela.socialnetwork.ui.story_presenter.StoryPresenterViewMvc
import com.kumela.socialnetwork.ui.story_presenter.StoryPresenterViewMvcImpl
import com.kumela.socialnetwork.ui.story_uploder.StoryUploaderViewMvc
import com.kumela.socialnetwork.ui.story_uploder.StoryUploaderViewMvcImpl
import com.kumela.socialnetwork.ui.user_profile.UserProfileViewMvc
import com.kumela.socialnetwork.ui.user_profile.UserProfileViewMvcImpl
import kotlin.reflect.KClass


/**
 * Created by Toko on 10,September,2020
 **/

class ViewMvcFactory(private val inflater: LayoutInflater) {

    fun <T : ViewMvc> newInstance(viewMvcClass: KClass<T>, parent: ViewGroup?): T {
        val viewMvc: ViewMvc = when (viewMvcClass) {
            // view elements
            BottomNavViewMvc::class -> BottomNavViewMvcImpl(inflater, parent)
            ToolbarViewMvc::class -> ToolbarViewMvc(inflater, parent)

            // fragments
            ChatViewMvc::class -> ChatViewMvcImpl(inflater, parent, this)
            CommentsViewMvc::class -> CommentsViewMvcImpl(inflater, parent, this)
            ExploreViewMvc::class -> ExploreViewMvcImpl(inflater, parent, this)
            HomeViewMvc::class -> HomeViewMvcImpl(inflater, parent, this)
            AuthViewMvc::class -> AuthViewMvcImpl(inflater, parent)
            MessagesViewMvc::class -> MessagesViewMvcImpl(inflater, parent, this)
            ProfileViewMvc::class -> ProfileViewMvcImpl(inflater, parent, this)
            SearchViewMvc::class -> SearchViewMvcImpl(inflater, parent, this)
            SplashViewMvc::class -> SplashViewMvcImpl(inflater, parent)
            PostImageViewMvc::class -> PostImageViewMvcImpl(inflater, parent, this)
            UserProfileViewMvc::class -> UserProfileViewMvcImpl(inflater, parent, this)
            UserListViewMvc::class -> UserListViewMvcImpl(inflater, parent, this)
            StoryUploaderViewMvc::class -> StoryUploaderViewMvcImpl(inflater, parent)
            StoryPresenterViewMvc::class -> StoryPresenterViewMvcImpl(inflater, parent)

            // recycler view items
            FeedItemViewMvc::class -> FeedItemViewMvcImpl(inflater, parent)
            StoryItemViewMvc::class -> StoryItemViewMvc(inflater, parent)
            ExploreItemDIRViewMvc::class -> ExploreItemDIRViewMvc(inflater, parent)
            ExploreItemTIRViewMvc::class -> ExploreItemTIRViewMvc(inflater, parent)
            ChatItemViewMvc::class -> ChatItemViewMvcImpl(inflater, parent)
            FriendsItemViewMvc::class -> FriendsItemViewMvcImpl(inflater, parent)
            ProfileStoryItemViewMvc::class -> ProfileStoryItemViewMvc(inflater, parent)
            PostItemViewMvc::class -> PostItemViewMvcImpl(inflater, parent)
            MessageRightItemViewMvc::class -> MessageRightItemViewMvc(inflater, parent)
            MessageLeftItemViewMvc::class -> MessageLeftItemViewMvc(inflater, parent)
            UserItemViewMvc::class -> UserItemViewMvc(inflater, parent)
            CommentsItemViewMvc::class -> CommentsItemViewMvc(inflater, parent)

            else -> throw RuntimeException("unsupported mvc class (${viewMvcClass.qualifiedName})")
        }

        @Suppress("UNCHECKED_CAST")
        return viewMvc as T
    }
}