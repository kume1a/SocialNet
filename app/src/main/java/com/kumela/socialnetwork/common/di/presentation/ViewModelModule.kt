package com.kumela.socialnetwork.common.di.presentation

import androidx.lifecycle.ViewModel
import com.kumela.socialnetwork.common.di.annotations.ViewModelKey
import com.kumela.socialnetwork.models.firebase.MessageModel
import com.kumela.socialnetwork.models.firebase.StoryModel
import com.kumela.socialnetwork.models.firebase.UserChatModel
import com.kumela.socialnetwork.models.list.PostModel
import com.kumela.socialnetwork.network.firebase.helpers.QueryPager
import com.kumela.socialnetwork.ui.chat.ChatViewModel
import com.kumela.socialnetwork.ui.common.viewmodels.ViewModelFactory
import com.kumela.socialnetwork.ui.explore.ExploreViewModel
import com.kumela.socialnetwork.ui.home.HomeViewModel
import com.kumela.socialnetwork.ui.messages.MessagesViewModel
import com.kumela.socialnetwork.ui.profile.ProfileViewModel
import com.kumela.socialnetwork.ui.user_list.UserListViewModel
import com.kumela.socialnetwork.ui.user_profile.UserProfileViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named
import javax.inject.Provider

@Module
class ViewModelModule {

    @Provides
    fun providesViewModelFactory(
        providerMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>,
    ): ViewModelFactory = ViewModelFactory(providerMap)

    @Provides
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun providesHomeViewModel(
        @Named("feedPostIdsQueryPager") feedPostIdsQueryPager: QueryPager<String>
    ): ViewModel = HomeViewModel(feedPostIdsQueryPager)

    @Provides
    @IntoMap
    @ViewModelKey(MessagesViewModel::class)
    fun providesMessagesViewModel(
        @Named("userIdsQueryPager") followingUserIdsQueryPager: QueryPager<String>,
        userChatsQueryPagerModel: QueryPager<UserChatModel>
    ): ViewModel = MessagesViewModel(followingUserIdsQueryPager, userChatsQueryPagerModel)

    @Provides
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    fun providesChatViewModel(messageQueryPager: QueryPager<MessageModel>): ViewModel =
        ChatViewModel(messageQueryPager)

    @Provides
    @IntoMap
    @ViewModelKey(UserListViewModel::class)
    fun providesUsersListViewModel(
        @Named("userIdsQueryPager") userIdsQueryPager: QueryPager<String>
    ): ViewModel = UserListViewModel(userIdsQueryPager)

    @Provides
    @IntoMap
    @ViewModelKey(ExploreViewModel::class)
    fun providesExploreViewModel(
        postsQueryPager: QueryPager<PostModel>
    ): ViewModel = ExploreViewModel(postsQueryPager)

    @Provides
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    fun providesProfileViewModel(
        storyQueryPager: QueryPager<StoryModel>
    ): ViewModel = ProfileViewModel(storyQueryPager)

    @Provides
    @IntoMap
    @ViewModelKey(UserProfileViewModel::class)
    fun providesUserProfileViewModel(
        storyQueryPager: QueryPager<StoryModel>
    ): ViewModel = UserProfileViewModel(storyQueryPager)
}