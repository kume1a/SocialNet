package com.kumela.socialnet.ui.adapters.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.kumela.socialnet.R
import com.kumela.socialnet.models.list.MessageListModel
import com.kumela.socialnet.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnet.ui.common.utils.setTime

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
        fun onHeartClicked(messageModel: MessageListModel)
    }

    private var model: MessageListModel? = null

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

    fun bindMessageModel(messageModel: MessageListModel) {
        textTime.setTime(messageModel.timestamp)
        textMessage.text = messageModel.message
        if (messageModel.liked) {
            imageViewHeart.setImageDrawable(getDrawable(R.drawable.ic_heart_filled))
        }

        model = messageModel
    }
}