package com.kumela.socialnet.ui.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnet.R
import com.kumela.socialnet.models.firebase.UserModel
import com.kumela.socialnet.models.list.ChatListModel
import com.kumela.socialnet.ui.adapters.friends.FriendsAdapter
import com.kumela.socialnet.ui.adapters.chats.ChatsAdapter
import com.kumela.socialnet.ui.common.ViewMvcFactory
import com.kumela.socialnet.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnet.ui.common.toolbar.ToolbarViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

class MessagesViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<MessagesViewMvc.Listener>(
    inflater, parent, R.layout.fragment_messages
), MessagesViewMvc {

    private val toolbar: Toolbar = findViewById(R.id.toolbar)
    private val toolbarViewMvc = viewMvcFactory.newInstance(ToolbarViewMvc::class, toolbar)

    private val recyclerFriends: RecyclerView = findViewById(R.id.recycler_friends)
    private val recyclerChats: RecyclerView = findViewById(R.id.recycler_messages)
    private val etSearch: EditText = findViewById(R.id.et_search)

    private val friendsAdapter = FriendsAdapter(viewMvcFactory) { userModel ->
        listener?.onFriendClicked(userModel)
    }
    private val chatAdapter = ChatsAdapter(viewMvcFactory) { chatModel ->
        listener?.onChatClicked(chatModel)
    }

    init {
        toolbarViewMvc.setTitle("Chats")
        toolbar.addView(toolbarViewMvc.rootView)

        etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                listener?.onSearchClicked()
            }
        }

        recyclerFriends.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = friendsAdapter
        }

        recyclerChats.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun bindFriends(users: List<UserModel>) {
        friendsAdapter.bindUsers(users)
    }

    override fun bindChats(chats: List<ChatListModel>) {
        chatAdapter.bindChats(chats)
    }

    override fun addOrUpdateChat(chat: ChatListModel) {
        chatAdapter.addOrUpdateChat(chat)
    }
}