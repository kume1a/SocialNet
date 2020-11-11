package com.kumela.socialnet.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kumela.socialnet.ui.adapters.messages.MessageLeftItemViewMvc
import com.kumela.socialnet.ui.adapters.messages.MessageRightItemViewMvc
import com.kumela.socialnet.ui.adapters.explore.ExploreItemDIRViewMvc
import com.kumela.socialnet.ui.adapters.explore.ExploreItemTIRViewMvc
import com.kumela.socialnet.ui.adapters.feed.FeedItemViewMvc
import com.kumela.socialnet.ui.adapters.feed.FeedItemViewMvcImpl
import com.kumela.socialnet.ui.adapters.friends.FriendsItemViewMvc
import com.kumela.socialnet.ui.adapters.friends.FriendsItemViewMvcImpl
import com.kumela.socialnet.ui.adapters.chats.ChatItemViewMvc
import com.kumela.socialnet.ui.adapters.chats.ChatItemViewMvcImpl
import com.kumela.socialnet.ui.adapters.comments.CommentsItemViewMvc
import com.kumela.socialnet.ui.adapters.posts.PostItemViewMvc
import com.kumela.socialnet.ui.adapters.posts.PostItemViewMvcImpl
import com.kumela.socialnet.ui.adapters.users.UserItemViewMvc
import com.kumela.socialnet.ui.adapters.story.StoryItemViewMvc
import com.kumela.socialnet.ui.adapters.story_profile.ProfileStoryItemViewMvc
import com.kumela.socialnet.ui.chat.ChatViewMvc
import com.kumela.socialnet.ui.chat.ChatViewMvcImpl
import com.kumela.socialnet.ui.comments.CommentsViewMvc
import com.kumela.socialnet.ui.comments.CommentsViewMvcImpl
import com.kumela.socialnet.ui.common.bottomnav.BottomNavViewMvc
import com.kumela.socialnet.ui.common.bottomnav.BottomNavViewMvcImpl
import com.kumela.socialnet.ui.common.mvc.ViewMvc
import com.kumela.socialnet.ui.common.toolbar.ToolbarViewMvc
import com.kumela.socialnet.ui.user_list.UserListViewMvc
import com.kumela.socialnet.ui.user_list.UserListViewMvcImpl
import com.kumela.socialnet.ui.explore.ExploreViewMvc
import com.kumela.socialnet.ui.explore.ExploreViewMvcImpl
import com.kumela.socialnet.ui.home.HomeViewMvc
import com.kumela.socialnet.ui.home.HomeViewMvcImpl
import com.kumela.socialnet.ui.login.LoginViewMvc
import com.kumela.socialnet.ui.login.LoginViewMvcImpl
import com.kumela.socialnet.ui.messages.MessagesViewMvc
import com.kumela.socialnet.ui.messages.MessagesViewMvcImpl
import com.kumela.socialnet.ui.personal_info.PersonalInfoViewMvc
import com.kumela.socialnet.ui.personal_info.PersonalInfoViewMvcImpl
import com.kumela.socialnet.ui.post_image.PostImageViewMvc
import com.kumela.socialnet.ui.post_image.PostImageViewMvcImpl
import com.kumela.socialnet.ui.profile.ProfileViewMvc
import com.kumela.socialnet.ui.profile.ProfileViewMvcImpl
import com.kumela.socialnet.ui.register.RegisterViewMvc
import com.kumela.socialnet.ui.register.RegisterViewMvcImpl
import com.kumela.socialnet.ui.search.SearchViewMvc
import com.kumela.socialnet.ui.search.SearchViewMvcImpl
import com.kumela.socialnet.ui.splash.SplashViewMvc
import com.kumela.socialnet.ui.splash.SplashViewMvcImpl
import com.kumela.socialnet.ui.story_presenter.StoryPresenterViewMvc
import com.kumela.socialnet.ui.story_presenter.StoryPresenterViewMvcImpl
import com.kumela.socialnet.ui.story_uploder.StoryUploaderViewMvc
import com.kumela.socialnet.ui.story_uploder.StoryUploaderViewMvcImpl
import com.kumela.socialnet.ui.user_profile.UserProfileViewMvc
import com.kumela.socialnet.ui.user_profile.UserProfileViewMvcImpl
import kotlin.reflect.KClass


/**
 * Created by Toko on 10,September,2020
 **/

class ViewMvcFactory(private val inflater: LayoutInflater) {

    @Suppress("UNCHECKED_CAST")
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
            LoginViewMvc::class -> LoginViewMvcImpl(inflater, parent)
            MessagesViewMvc::class -> MessagesViewMvcImpl(inflater, parent, this)
            PersonalInfoViewMvc::class -> PersonalInfoViewMvcImpl(inflater, parent)
            ProfileViewMvc::class -> ProfileViewMvcImpl(inflater, parent, this)
            RegisterViewMvc::class -> RegisterViewMvcImpl(inflater, parent)
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

        return viewMvc as T
    }
}