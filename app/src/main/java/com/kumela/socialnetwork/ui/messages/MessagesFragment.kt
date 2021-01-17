package com.kumela.socialnetwork.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.models.list.ChatList
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import com.kumela.socialnetwork.ui.common.viewmodels.ViewModelFactory
import javax.inject.Inject

/**
 * Created by Toko on 24,September,2020
 **/

class MessagesFragment : BaseFragment(), MessagesViewMvc.Listener, MessagesViewModel.Listener {

    private lateinit var mViewMvc: MessagesViewMvc
    private lateinit var mViewModel: MessagesViewModel

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mBottomNavHelper: BottomNavHelper
    @Inject lateinit var mScreensNavigator: MessagesScreensNavigator
    @Inject lateinit var mViewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBottomNavHelper.showBottomNav()

        mViewMvc = mViewMvcFactory.newInstance(MessagesViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(MessagesViewModel::class.java)

        // register listeners
        mViewMvc.registerListener(this)
        mViewModel.registerListener(this)

        // bind data if it is cached in view model
        if (mViewModel.getChats().isNotEmpty()) {
            mViewMvc.bindChats(mViewModel.getChats())
        } else {
            mViewModel.fetchNextChatsPageAndNotify()
        }

        if (mViewModel.getFollowingUsers()?.isNotEmpty() == true) {
            mViewMvc.bindFriends(mViewModel.getFollowingUsers()!!)
        } else {
            mViewModel.fetchFollowingUsersAndNotify()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mViewMvc.unregisterListener()
        mViewModel.unregisterListener(this)
    }

    override fun onChatClicked(chatList: ChatList) {
        mScreensNavigator.toChat(chatList.targetUid, chatList.targetImageUri, chatList.targetUsername)
    }

    override fun onFriendClicked(user: User) {
//        mScreensNavigator.toChat(user.id, user.imageUrl, user.name)
    }

    override fun onSearchClicked() {
        mScreensNavigator.toSearch()
    }

    // view model callbacks
    override fun onFollowingUsersFetched(users: List<User>) {
        mViewMvc.bindFriends(users)
    }

    override fun onChatFetched(chatList: ChatList?) {
        if (chatList != null) {
            mViewMvc.addOrUpdateChat(chatList)
        } else {
            mViewModel.fetchNextFollowingUsersPageAndNotify()
        }
    }
}