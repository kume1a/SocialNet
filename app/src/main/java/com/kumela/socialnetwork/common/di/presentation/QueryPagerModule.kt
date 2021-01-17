package com.kumela.socialnetwork.common.di.presentation

import com.kumela.socialnetwork.common.Constants
import com.kumela.socialnetwork.models.FeedStoriesModel
import com.kumela.socialnetwork.models.Message
import com.kumela.socialnetwork.models.Story
import com.kumela.socialnetwork.models.UserChat
import com.kumela.socialnetwork.models.list.Post
import com.kumela.socialnetwork.network.firebase.helpers.QueryPager
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
    fun providesUserChatsQueryPager(): QueryPager<UserChat> =
        QueryPager(
            Constants.PAGE_SIZE_USER_CHATS,
            UserChat::class.java
        )

    @Provides
    fun providesMessageQueryPager(): QueryPager<Message> =
        QueryPager(
            Constants.PAGE_SIZE_MESSAGES,
            Message::class.java
        )

    @Provides
    fun providesPostsQueryPager(): QueryPager<Post> =
        QueryPager(
            Constants.PAGE_SIZE_POST,
            Post::class.java
        )

    @Provides
    fun providesFeedStoryQueryPager(): QueryPager<FeedStoriesModel> =
        QueryPager(
            Constants.PAGE_SIZE_STORIES,
            FeedStoriesModel::class.java
        )

    @Provides
    fun providesStoryQueryPager(): QueryPager<Story> =
        QueryPager(
            Constants.PAGE_SIZE_STORIES,
            Story::class.java
        )

    @Provides
    fun providesGenericQueryPager(): QueryPager<Map<*, *>> =
        QueryPager(
            2,
            Map::class.java
        )
}