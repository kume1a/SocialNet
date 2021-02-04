package com.kumela.socialnetwork.ui.messages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.models.list.ChatList
import com.kumela.socialnetwork.network.common.fold
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import com.kumela.socialnetwork.ui.common.viewmodels.ViewModelFactory
import javax.inject.Inject

/**
 * Created by Toko on 24,September,2020
 **/

class MessagesFragment : BaseFragment(), MessagesViewMvc.Listener {

    private lateinit var mViewMvc: MessagesViewMvc
    private lateinit var mViewModel: MessagesViewModel

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mBottomNavHelper: BottomNavHelper
    @Inject lateinit var mScreensNavigator: MessagesScreensNavigator
    @Inject lateinit var mViewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        injector.inject(this)

        mBottomNavHelper.showBottomNav()

        mViewMvc = mViewMvcFactory.newInstance(MessagesViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(MessagesViewModel::class.java)

        mViewMvc.registerListener(this)

        val cachedFollowingUsers = mViewModel.getCachedFollowingUsers()
        lifecycleScope.launchWhenStarted {
            if (cachedFollowingUsers != null) {
                mViewMvc.addUsers(cachedFollowingUsers.data)
            } else {
                fetchFollowingUsers()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mViewMvc.unregisterListener()
    }

    override fun onChatClicked(chatList: ChatList) {
//        mScreensNavigator.toChat(
//            chatList.targetUid,
//            chatList.targetImageUri,
//            chatList.targetUsername
//        )
    }

    override fun onFriendClicked(user: User) {
        mScreensNavigator.toChat(user.id, user.imageUrl, user.name)
    }

    override fun onSearchClicked() {
        mScreensNavigator.toSearch()
    }

    private suspend fun fetchFollowingUsers() {
        val result = mViewModel.fetchFollowingUsers()
        result.fold(
            onSuccess = { response ->
                if (response == null) return@fold

                mViewMvc.addUsers(response.data)
            },
            onFailure = { error ->
                Log.e(javaClass.simpleName, "fetchFollowingUsers: $error")
            }
        )
    }
}