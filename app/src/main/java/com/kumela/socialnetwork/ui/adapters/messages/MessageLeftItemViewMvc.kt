package com.kumela.socialnetwork.ui.adapters.messages


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.list.MessageList
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.utils.setTime

/**
 * Created by Toko on 08,October,2020
 **/

class MessageLeftItemViewMvc(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<MessageLeftItemViewMvc.Listener>(
    inflater, parent, R.layout.item_chat_msg_left
) {

    interface Listener {
        fun onLongClick(message: MessageList)
    }

    private var message: MessageList? = null

    private val textTime: TextView = findViewById(R.id.text_time)
    private val textMessage: TextView = findViewById(R.id.text_message)

    init {
        rootView.setOnLongClickListener {
            listener?.onLongClick(message!!)
            true
        }
    }

    fun bindMessageModel(message: MessageList) {
        textTime.setTime(message.timestamp)
        textMessage.text = message.message

        this.message = message
    }
}