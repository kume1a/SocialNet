package com.kumela.socialnetwork.common.di.presentation

import androidx.lifecycle.ViewModel
import com.kumela.socialnetwork.common.di.annotations.ViewModelKey
import com.kumela.socialnetwork.models.Message
import com.kumela.socialnetwork.models.UserChat
import com.kumela.socialnetwork.network.authentication.KeyStore
import com.kumela.socialnetwork.network.firebase.helpers.QueryPager
import com.kumela.socialnetwork.network.repositories.*
import com.kumela.socialnetwork.ui.chat.ChatViewModel
import com.kumela.socialnetwork.ui.comments.CommentsViewModel
import com.kumela.socialnetwork.ui.common.viewmodels.ViewModelFactory
import com.kumela.socialnetwork.ui.explore.ExploreViewModel
import com.kumela.socialnetwork.ui.home.HomeViewModel
import com.kumela.socialnetwork.ui.messages.MessagesViewModel
import com.kumela.socialnetwork.ui.profile.ProfileViewModel
import com.kumela.socialnetwork.ui.replies.ReplyViewModel
import com.kumela.socialnetwork.ui.search.SearchViewModel
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
        postRepository: PostRepository,
        keyStore: KeyStore
    ): ViewModel = HomeViewModel(postRepository, keyStore)

    @Provides
    @IntoMap
    @ViewModelKey(MessagesViewModel::class)
    fun providesMessagesViewModel(
        @Named("userIdsQueryPager") followingUserIdsQueryPager: QueryPager<String>,
        userChatsQueryPager: QueryPager<UserChat>
    ): ViewModel = MessagesViewModel(followingUserIdsQueryPager, userChatsQueryPager)

    @Provides
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    fun providesChatViewModel(messageQueryPager: QueryPager<Message>): ViewModel =
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
        postRepository: PostRepository
    ): ViewModel = ExploreViewModel(postRepository)

    @Provides
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    fun providesProfileViewModel(
        userRepository: UserRepository,
        postRepository: PostRepository
    ): ViewModel = ProfileViewModel(userRepository, postRepository)

    @Provides
    @IntoMap
    @ViewModelKey(UserProfileViewModel::class)
    fun providesUserProfileViewModel(
        followRepository: FollowRepository,
        postRepository: PostRepository
    ): ViewModel = UserProfileViewModel(followRepository, postRepository)

    @Provides
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun providesSearchViewModel(
        searchRepository: SearchRepository
    ): ViewModel = SearchViewModel(searchRepository)

    @Provides
    @IntoMap
    @ViewModelKey(CommentsViewModel::class)
    fun provideCommentViewModel(
        commentRepository: CommentRepository
    ): ViewModel = CommentsViewModel(commentRepository)

    @Provides
    @IntoMap
    @ViewModelKey(ReplyViewModel::class)
    fun provideReplyViewModel(
        commentRepository: CommentRepository
    ): ViewModel = ReplyViewModel(commentRepository)
}