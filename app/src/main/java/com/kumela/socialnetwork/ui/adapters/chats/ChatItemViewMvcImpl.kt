package com.kumela.socialnetwork.ui.adapters.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kumela.socialnetwork.ui.common.views.RoundedImageView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.list.ChatListModel
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.utils.load
import com.kumela.socialnetwork.ui.common.utils.setTime

/**
 * Created by Toko on 04,October,2020
 **/

class ChatItemViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<ChatItemViewMvc.Listener>(
    inflater,
    parent,
    R.layout.item_chat
), ChatItemViewMvc {

    private var chatListModel: ChatListModel? = null

    private val textSuggestions: TextView = findViewById(R.id.text_suggestions)
    private val imageProfile: RoundedImageView = findViewById(R.id.image)
    private val imageIsOnline: RoundedImageView = findViewById(R.id.image_online)
    private val textHeader: TextView = findViewById(R.id.text_header)
    private val textLastMessage: TextView = findViewById(R.id.text_message)
    private val textTime: TextView = findViewById(R.id.text_time)

    init {
        rootView.setOnClickListener { listener?.onChatClicked(chatListModel!!) }
    }

    override fun bindChatListModel(chatListModel: ChatListModel) {
        this.chatListModel = chatListModel

        imageProfile.load(chatListModel.targetImageUri)
        textHeader.text = chatListModel.targetUsername
        imageIsOnline.visibility = if (chatListModel.targetIsOnline) View.VISIBLE else View.GONE

        chatListModel.lastUpdated?.let { textTime.setTime(it) }

        if (chatListModel.lastMessage != null) {
            textLastMessage.text = chatListModel.lastMessage
        } else {
            textLastMessage.visibility = View.GONE
        }
    }

    override fun showSuggestionsHeader(visible: Boolean) {
        textSuggestions.visibility = if (visible) View.VISIBLE else View.GONE
    }
}