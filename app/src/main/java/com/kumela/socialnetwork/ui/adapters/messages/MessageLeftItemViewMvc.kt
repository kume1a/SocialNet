package com.kumela.socialnetwork.ui.adapters.messages


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
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
        fun onHeartClicked(message: MessageList)
    }

    private var model: MessageList? = null

    private val textTime: TextView = findViewById(R.id.text_time)
    private val textMessage: TextView = findViewById(R.id.text_message)
    private val imageViewHeart: ImageButton = findViewById(R.id.image_heart)

    init {
        imageViewHeart.setOnClickListener {
            model!!.liked = !model!!.liked
            if (model!!.liked) {
                imageViewHeart.setImageResource(R.drawable.ic_heart_filled)
            } else {
                imageViewHeart.setImageResource(R.drawable.ic_heart)
            }
            listener?.onHeartClicked(model!!)
        }
    }

    fun bindMessageModel(message: MessageList) {
        textTime.setTime(message.timestamp)
        textMessage.text = message.message
        if (message.liked) {
            imageViewHeart.setImageDrawable(getDrawable(R.drawable.ic_heart_filled))
        }

        model = message
    }
}