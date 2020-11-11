package com.kumela.socialnet.common.di.presentation

import com.kumela.socialnet.common.Constants
import com.kumela.socialnet.models.firebase.FeedStoriesModel
import com.kumela.socialnet.models.firebase.MessageModel
import com.kumela.socialnet.models.firebase.StoryModel
import com.kumela.socialnet.models.firebase.UserChatModel
import com.kumela.socialnet.models.list.PostModel
import com.kumela.socialnet.network.firebase.helpers.QueryPager
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by Toko on 31,October,2020
 **/

@Module
class QueryPagerModule {

    @Named("feedPostIdsQueryPager")
    @Provides
    fun providesFeedPostIdsQueryPager(): QueryPager<String> =
        QueryPager(
            Constants.PAGE_SIZE_FEED_IDS,
            String::class.java
        )

    @Named("userIdsQueryPager")
    @Provides
    fun providesFollowingUserIdsQueryPager(): QueryPager<String> =
        QueryPager(
            Constants.PAGE_SIZE_USERS,
            null
        )

    @Provides
    fun providesUserChatsQueryPager(): QueryPager<UserChatModel> =
        QueryPager(
            Constants.PAGE_SIZE_USER_CHATS,
            UserChatModel::class.java
        )

    @Provides
    fun providesMessageQueryPager(): QueryPager<MessageModel> =
        QueryPager(
            Constants.PAGE_SIZE_MESSAGES,
            MessageModel::class.java
        )

    @Provides
    fun providesPostsQueryPager(): QueryPager<PostModel> =
        QueryPager(
            Constants.PAGE_SIZE_POST,
            PostModel::class.java
        )

    @Provides
    fun providesFeedStoryQueryPager(): QueryPager<FeedStoriesModel> =
        QueryPager(
            Constants.PAGE_SIZE_STORIES,
            FeedStoriesModel::class.java
        )

    @Provides
    fun providesStoryQueryPager(): QueryPager<StoryModel> =
        QueryPager(
            Constants.PAGE_SIZE_STORIES,
            StoryModel::class.java
        )

    @Provides
    fun providesGenericQueryPager(): QueryPager<Map<*, *>> =
        QueryPager(
            2,
            Map::class.java
        )
}