package com.kumela.socialnetwork.ui.chat

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.list.MessageListModel
import com.kumela.socialnetwork.ui.adapters.messages.MessageAdapter
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.toolbar.ToolbarViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

class ChatViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<ChatViewMvc.Listener>(
    inflater, parent, R.layout.fragment_chat
), ChatViewMvc, MessageAdapter.Listener {

    private val motionLayout: MotionLayout = rootView as MotionLayout

    private val toolbar: Toolbar = findViewById(R.id.toolbar)
    private val toolbarViewMvc = viewMvcFactory.newInstance(ToolbarViewMvc::class, toolbar)

    private val etMessage: EditText = findViewById(R.id.et_input_message)
    private val buttonEmoji: ImageButton = findViewById(R.id.button_emoji)
    private val buttonShareFile: ImageButton = findViewById(R.id.button_file)
    private val buttonSend: ImageButton = findViewById(R.id.button_send)
    private val recyclerMessages: RecyclerView = findViewById(R.id.recycler_messages)

    private val chatAdapter = MessageAdapter(viewMvcFactory, this)

    init {
        // initialize toolbar
        toolbarViewMvc.setBackgroundColor(R.color.primary)
        toolbarViewMvc.setTitleStyle(22f, R.color.white, true, TextView.TEXT_ALIGNMENT_CENTER)
        toolbarViewMvc.tintIcons(R.color.white)
        toolbarViewMvc.enableUpButtonAndListen {
            listener?.onBackPressed()
        }
        toolbarViewMvc.enableMenuButtonAndListen(R.menu.menu_chat) menu@{
            Log.d(javaClass.simpleName, "enableMenuButtonAndListen called")
            return@menu true
        }
        toolbar.addView(toolbarViewMvc.rootView)

        etMessage.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                listener?.onMessageBoxFocused()
            } else {
                listener?.onMessageBoxFocusLost()
            }
        }
        buttonEmoji.setOnClickListener { listener?.onEmojiClicked() }
        buttonShareFile.setOnClickListener { listener?.onShareFileClicked() }
        buttonSend.setOnClickListener { listener?.onSendClicked() }

        // initialize recycler view
        recyclerMessages.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            adapter = chatAdapter
            setHasFixedSize(true)
        }
    }

    override fun bindToolbarText(text: String) {
        toolbarViewMvc.setTitle(text)
    }

    override fun transitionToEnd() {
        motionLayout.transitionToEnd()
    }

    override fun transitionToStart() {
        motionLayout.transitionToStart()
    }

    override fun addMessage(messageModel: MessageListModel) {
        chatAdapter.addMessage(messageModel)
        recyclerMessages.scrollToPosition(0)
    }

    override fun addMessages(messages: List<MessageListModel>) {
        chatAdapter.addMessages(messages)
    }

    override fun bindMessages(messages: List<MessageListModel>) {
        chatAdapter.bindMessages(messages)
    }

    override fun updateMessage(messageModel: MessageListModel) {
        chatAdapter.updateMessage(messageModel)
    }

    override fun getCurrentMessage(): String = etMessage.text.toString()

    override fun clearMessageField() {
        etMessage.text = null
    }

    // chat recycler callbacks
    override fun onScrolledToTop() {
        listener?.onScrolledToTop()
    }

    override fun onHeartClicked(messageModel: MessageListModel) {
        listener?.onHeartClicked(messageModel)
    }
}