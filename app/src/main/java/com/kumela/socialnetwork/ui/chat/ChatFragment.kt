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
import com.kumela.socialnetwork.ui.common.utils.getUniqueId
import com.kumela.socialnetwork.ui.common.viewmodels.ViewModelFactory
import javax.inject.Inject

/**
 * Created by Toko on 24,September,2020
 **/

class ChatFragment : BaseFragment(), ChatViewMvc.Listener, ChatViewModel.Listener {

    private lateinit var mViewMvc: ChatViewMvc
    private lateinit var mViewModel: ChatViewModel

    private lateinit var argUserId: String
    private lateinit var argUserImageUri: String
    private lateinit var argUserUsername: String

    private lateinit var currentChatId: String
    private val pendingMessageQueue = hashSetOf<Long>()
    private var messagesWereEmpty = false

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
        argUserImageUri = args.userImageUri
        argUserUsername = args.userUsername

        currentChatId = getUniqueId(UserUseCase.uid, argUserId)
        mViewMvc.bindToolbarText(argUserUsername)

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(ChatViewModel::class.java)

        mViewModel.registerListener(this)
        mViewModel.registerMessageListener(currentChatId)
        mViewMvc.registerListener(this)

        val messages = mViewModel.getMessages()
        if (messages.isNotEmpty()) {
            mViewMvc.bindMessages(messages.map {
                MessageList(it.id, it.senderId, it.message, it.timestamp, it.liked, true)
            })
        } else {
            mViewModel.fetchNextMessagesPageAndNotify(currentChatId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mViewModel.unregisterMessageListener(currentChatId)
        mViewModel.unregisterListener(this)
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
        pendingMessageQueue.add(messageModel.timestamp)

        mViewMvc.clearMessageField()
        messageModel.apply {
            Log.d(javaClass.simpleName, "onSendClicked: messagesWereEmpty = $messagesWereEmpty")
            mViewMvc.addMessage(MessageList(id, senderId, message, timestamp, liked, messagesWereEmpty))
            messagesWereEmpty = false
        }
        mViewModel.sendMessage(currentChatId, messageModel)
    }

    override fun onMessageBoxFocused() {
        mViewMvc.transitionToEnd()
    }

    override fun onMessageBoxFocusLost() {
        mViewMvc.transitionToStart()
    }

    override fun onScrolledToTop() {
        mViewModel.fetchNextMessagesPageAndNotify(currentChatId)
    }

    override fun onHeartClicked(message: MessageList) {
        mViewModel.likeMessage(currentChatId, message.messageId, message.liked)
    }

    // view model callbacks
    override fun onMessagesFetched(messages: List<Message>) {
        messagesWereEmpty = messages.isEmpty()
        if (!messagesWereEmpty) {
            mViewMvc.addMessages(messages.map {
                MessageList(it.id, it.senderId, it.message, it.timestamp, it.liked, true)
            })
        }
    }

    override fun onMessageReceived(message: Message) {
        Log.d(javaClass.simpleName, "onMessageReceived() called with: messageModel = $message")
        val messageListModel = MessageList(
            message.id,
            message.senderId,
            message.message,
            message.timestamp,
            message.liked,
            true
        )

        if (pendingMessageQueue.contains(message.timestamp)) {
            pendingMessageQueue.remove(message.timestamp)
            mViewMvc.updateMessage(messageListModel)
        } else {
            mViewMvc.addMessage(messageListModel)
        }
    }
}