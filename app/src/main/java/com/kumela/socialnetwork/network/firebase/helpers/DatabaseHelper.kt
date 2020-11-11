package com.kumela.socialnet.network.firebase.helpers

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * Created by Toko on 10,October,2020
 **/

object DatabaseHelper {
    const val DB_KEY_USERS = "users"
    const val DB_KEY_USER_EXTRA_INFO = "user_extra_info"
    private const val DB_KEY_POSTS = "posts"
    private const val DB_KEY_LIKES = "likes"
    private const val DB_KEY_COMMENTS = "comments"
    private const val DB_KEY_FOLLOWING = "following"
    private const val DB_KEY_FOLLOWERS = "followers"
    private const val DB_KEY_USER_FEED_POSTS = "user_feed_posts"
    private const val DB_KEY_CHATS = "chats"
    private const val DB_KEY_MESSAGES = "messages"
    private const val DB_KEY_USER_CHATS = "user_chats"
    private const val DB_KEY_STORIES = "stories"
    private const val DB_KEY_FEED_STORIES = "feed_stories"

    val databaseRef = Firebase.database.reference

    fun getUserTableRef(): DatabaseReference =
        databaseRef.child(DB_KEY_USERS)

    fun getUserExtraInfoTableRef(): DatabaseReference =
        databaseRef.child(DB_KEY_USER_EXTRA_INFO)

    fun getPostTableRef(): DatabaseReference =
        databaseRef.child(DB_KEY_POSTS)

    fun getLikesTableRef(): DatabaseReference =
        databaseRef.child(DB_KEY_LIKES)

    fun getCommentsTableRef(): DatabaseReference =
        databaseRef.child(DB_KEY_COMMENTS)

    fun getFollowingTableRef(): DatabaseReference =
        databaseRef.child(DB_KEY_FOLLOWING)

    fun getFollowersTableRef(): DatabaseReference =
        databaseRef.child(DB_KEY_FOLLOWERS)

    fun getUserFeedPostsRef(): DatabaseReference =
        databaseRef.child(DB_KEY_USER_FEED_POSTS)

    fun getChatTableRef(): DatabaseReference =
        databaseRef.child(DB_KEY_CHATS)

    fun getUserChatsTableRef(): DatabaseReference =
        databaseRef.child(DB_KEY_USER_CHATS)

    fun getMessageTableRef(): DatabaseReference =
        databaseRef.child(DB_KEY_MESSAGES)

    fun getStoriesTableRef(): DatabaseReference =
        databaseRef.child(DB_KEY_STORIES)

    fun getFeedStoriesTableRef(): DatabaseReference =
        databaseRef.child(DB_KEY_FEED_STORIES)
}