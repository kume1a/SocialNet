package com.kumela.socialnetwork.ui.messages

import android.util.Log
import com.kumela.socialnetwork.common.Constants
import com.kumela.socialnetwork.models.UserChat
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.models.list.ChatList
import com.kumela.socialnetwork.network.firebase.ChatUseCase
import com.kumela.socialnetwork.network.firebase.UserUseCase
import com.kumela.socialnetwork.network.firebase.helpers.QueryPager
import com.kumela.socialnetwork.ui.common.utils.isOnline
import com.kumela.socialnetwork.ui.common.viewmodels.ObservableViewModel

/**
 * Created by Toko on 27,October,2020
 **/

class MessagesViewModel(
    private val followingUserIdsQueryPager: QueryPager<String>,
    private val userChatsQueryPager: QueryPager<UserChat>
) : ObservableViewModel<MessagesViewModel.Listener>() {

    interface Listener {
        fun onFollowingUsersFetched(users: List<User>)
        fun onChatFetched(chatList: ChatList?)
    }

    private var mUsers: List<User>? = null
    private var mChats = ArrayList<ChatList>()

    private var fetchingChats = false
    private var fetchingUsers = false

    init {
        ChatUseCase.registerListener(uuid)
        UserUseCase.registerListener(uuid)
        followingUserIdsQueryPager.registerListener(uuid)
        userChatsQueryPager.registerListener(uuid)
    }

    override fun onCleared() {
        super.onCleared()

        ChatUseCase.unregisterListener(uuid)
        UserUseCase.unregisterListener(uuid)
        followingUserIdsQueryPager.unregisterListener(uuid)
        userChatsQueryPager.unregisterListener(uuid)
        for (chatListModel in mChats) {
            chatListModel.id?.let {
                ChatUseCase.unregisterChatUpdateListener(it)
            }
        }
    }

    fun getChats(): List<ChatList> = mChats
    fun getFollowingUsers(): List<User>? = mUsers

    fun fetchFollowingUsersAndNotify() {
        UserUseCase.getFollowingUsers(uuid, UserUseCase.uid,
            { userModels ->
                mUsers = userModels
                for (listener in listeners) {
                    listener.onFollowingUsersFetched(userModels)
                }
            },
            { exception ->
                Log.e(javaClass.simpleName, "fetchFollowingUsersAndNotify: ", exception)
            }
        )
    }


    fun fetchNextChatsPageAndNotify() {
        if (fetchingChats) return

        userChatsQueryPager.fetchNextPageAndNotify(uuid, ChatUseCase.getUserChatsRef(),
            { userChats ->
                if (userChats != null && userChats.isNotEmpty()) {
                    fetchingChats = true
                    userChats.forEachIndexed { index, userChatModel ->
                        registerChatUpdateListener(index, userChatModel)
                    }
                } else {
                    fetchingChats = false
                    for (listener in listeners) {
                        listener.onChatFetched(null)
                    }
                }
            },
            { databaseError ->
                Log.e(javaClass.simpleName, "fetchNextChatsPageAndNotify: ", databaseError.toException())
            })
    }

    fun fetchNextFollowingUsersPageAndNotify() {
        if (fetchingUsers) return

        followingUserIdsQueryPager.fetchNextPageAndNotify(uuid, UserUseCase.getFollowingUsersRef(),
            { followingUserIds ->
                if (followingUserIds != null && followingUserIds.isNotEmpty()) {
                    fetchingUsers = true
                    for (uid in followingUserIds) {
                        UserUseCase.fetchUserAndNotify(uuid, uid,
                            { userModel ->
//                                val chatListModel = ChatList(
//                                    targetUid = userModel.id,
//                                    targetUsername = userModel.name,
//                                    targetImageUri = userModel.imageUrl,
//                                    targetIsOnline = userModel.isOnline()
//                                )
//
//                                mChats.add(chatListModel)
//                                for (listener in listeners) {
//                                    listener.onChatFetched(chatListModel)
//                                }
//
//                                if (followingUserIds.indexOf(uid) == followingUserIds.size - 1) {
//                                    fetchingChats = false
//                                }
                            },
                            { databaseError ->
                                fetchingUsers = false
                                Log.e(
                                    javaClass.simpleName,
                                    "fetchNextFollowingUsersPageAndNotify: ",
                                    databaseError.toException()
                                )
                            })
                    }
                } else {
                    fetchingUsers = false
                }
            },
            { databaseError ->
                Log.e(
                    javaClass.simpleName,
                    "fetchNextFollowingUsersPage: ",
                    databaseError.toException()
                )
            })
    }

    private fun registerChatUpdateListener(index: Int, userChat: UserChat) {
        ChatUseCase.registerChatUpdateListener(uuid, userChat.chatId,
            { chatModel ->
                val oldChatModelIndex = mChats.indexOfFirst { it.id == chatModel.id }

                if (oldChatModelIndex == -1) {
                    UserUseCase.fetchUserAndNotify(uuid, userChat.targetId,
                        { userModel ->
//                            val chatListModel = ChatList(
//                                chatModel.id,
//                                chatModel.lastMessage,
//                                chatModel.lastUpdated,
//                                userModel.id,
//                                userModel.name,
//                                userModel.imageUrl,
//                                userChat.unseenMessageCount,
//                                userModel.isOnline()
//                            )
//
//                            mChats.add(chatListModel)
//                            notifyListenerAndCheckIfLastChatFetched(index, chatListModel)
                        },
                        { databaseError ->
                            fetchingChats = false
                            Log.e(javaClass.simpleName, "registerChatUpdateListener: ", databaseError.toException())
                        })
                } else {
                    mChats[oldChatModelIndex] = mChats[oldChatModelIndex].copy(
                        lastMessage = chatModel.lastMessage,
                        lastUpdated = chatModel.lastUpdated,
                    )
                    notifyListenerAndCheckIfLastChatFetched(index, mChats[oldChatModelIndex])
                }
            },
            { databaseError ->
                fetchingChats = false
                Log.e(javaClass.simpleName, "registerChatUpdateListener: ", databaseError.toException())
            })
    }

    private fun notifyListenerAndCheckIfLastChatFetched(index: Int, chatList: ChatList) {
        for (listener in listeners) {
            listener.onChatFetched(chatList)
        }
        if (index == Constants.PAGE_SIZE_USER_CHATS - 1) {
            fetchingChats = false
        }
    }

}