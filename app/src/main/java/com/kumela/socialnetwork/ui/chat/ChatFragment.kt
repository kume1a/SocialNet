package com.kumela.socialnetwork.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kumela.socialnetwork.models.Message
import com.kumela.socialnetwork.models.list.MessageList
import com.kumela.socialnetwork.network.firebase.UserUseCase
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import com.kumela.socialnetwork.ui.common.viewmodels.ViewModelFactory
import javax.inject.Inject

/**
 * Created by Toko on 24,September,2020
 **/

class ChatFragment : BaseFragment(), ChatViewMvc.Listener {

    private lateinit var mViewMvc: ChatViewMvc
    private lateinit var mViewModel: ChatViewModel

    private var argUserId: Int = -1
    private lateinit var argUserImageUrl: String
    private lateinit var argUserName: String

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mScreensNavigator: ChatScreensNavigator
    @Inject lateinit var mViewModelFactory: ViewModelFactory
    @Inject lateinit var mBottomNavHelper: BottomNavHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        injector.inject(this)

        mBottomNavHelper.hideBottomNav()

        mViewMvc = mViewMvcFactory.newInstance(ChatViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = ChatFragmentArgs.fromBundle(requireArguments())
        argUserId = args.userId
        argUserImageUrl = args.userImageUrl
        argUserName = args.userName

        mViewMvc.bindToolbarText(argUserName)

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(ChatViewModel::class.java)

        mViewMvc.registerListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        mViewMvc.unregisterListener()
    }

    override fun onBackPressed() {
        mScreensNavigator.navigateUp()
    }

    override fun onEmojiClicked() {
        Log.d(javaClass.simpleName, "onEmojiClicked() called")
    }

    override fun onShareFileClicked() {
        Log.d(javaClass.simpleName, "onShareFileClicked() called")
    }

    override fun onSendClicked() {
        val message = mViewMvc.getCurrentMessage().trim()

        if (message.isBlank()) return

        val messageModel = Message("", UserUseCase.uid, message, System.currentTimeMillis(), false)
        mViewMvc.clearMessageField()
    }

    override fun onMessageBoxFocused() {
        mViewMvc.transitionToEnd()
    }

    override fun onMessageBoxFocusLost() {
        mViewMvc.transitionToStart()
    }

    override fun onScrolledToTop() {
    }

    override fun onHeartClicked(message: MessageList) {
    }

    // view model callbacks
//    override fun onMessagesFetched(messages: List<Message>) {
//    }
//
//    override fun onMessageReceived(message: Message) {
//        Log.d(javaClass.simpleName, "onMessageReceived() called with: messageModel = $message")
//        val messageListModel = MessageList(
//            message.id,
//            message.senderId,
//            message.message,
//            message.timestamp,
//            message.liked,
//            true
//        )
//
//        if (pendingMessageQueue.contains(message.timestamp)) {
//            pendingMessageQueue.remove(message.timestamp)
//            mViewMvc.updateMessage(messageListModel)
//        } else {
//            mViewMvc.addMessage(messageListModel)
//        }
//    }
}