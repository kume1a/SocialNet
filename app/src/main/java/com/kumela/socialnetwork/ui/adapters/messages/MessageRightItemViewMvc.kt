package com.kumela.socialnetwork.ui.adapters.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.list.MessageList
import com.kumela.socialnetwork.ui.common.mvc.BaseViewMvc
import com.kumela.socialnetwork.ui.common.utils.setTime

/**
 * Created by Toko on 08,October,2020
 **/

class MessageRightItemViewMvc(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseViewMvc(
    inflater, parent, R.layout.item_chat_msg_right
) {

    private val textTime: TextView = findViewById(R.id.text_time)
    private val textMessage: TextView = findViewById(R.id.text_message)
    private val cardBgr: CardView = findViewById(R.id.card_bgr_message)

    fun bindMessageModel(message: MessageList) {
        textTime.setTime(message.timestamp)
        textMessage.text = message.message

        if (message.sent) {
            cardBgr.setCardBackgroundColor(getColor(R.color.primary_light50))
        }
    }
}