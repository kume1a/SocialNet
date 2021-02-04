package com.kumela.socialnetwork.ui.replies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.Comment
import com.kumela.socialnetwork.models.Reply
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.adapters.reply.ReplyAdapter
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.toolbar.ToolbarViewMvc
import com.kumela.socialnetwork.ui.common.utils.WrapContentLinearLayoutManager
import com.kumela.socialnetwork.ui.common.utils.load
import com.kumela.socialnetwork.ui.common.utils.setTimePassed
import com.kumela.socialnetwork.ui.views.RoundedImageView

class ReplyViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<ReplyViewMvc.Listener>(inflater, parent, R.layout.fragment_reply),
    ReplyViewMvc {

    private val toolbar: Toolbar = findViewById(R.id.toolbar)
    private val toolbarViewMvc = viewMvcFactory.newInstance(ToolbarViewMvc::class, toolbar)

    private val commenterImageProfile: RoundedImageView = findViewById(R.id.image_profile)
    private val commenterTextUserName: TextView = findViewById(R.id.text_username)
    private val commentBody: TextView = findViewById(R.id.text_comment)
    private val commentTime: TextView = findViewById(R.id.text_time)
    private val commentTextLike: TextView = findViewById(R.id.text_like)
    private val commentTextReply: TextView = findViewById(R.id.text_reply)

    private val recyclerReplies: RecyclerView = findViewById(R.id.recycler_replies)
    private val etInput: EditText = findViewById(R.id.et_input)
    private val buttonPost: MaterialButton = findViewById(R.id.button_post)

    private val replyAdapter = ReplyAdapter(viewMvcFactory, object : ReplyAdapter.Listener {
        override fun onUserClicked(user: User) {
            listener?.onUserClicked(user)
        }

        override fun onLikeClicked(reply: Reply) {
            listener?.onLikeClicked(reply)
        }

        override fun onLastReplyBound() {
            listener?.onLastReplyBound()
        }
    })

    init {
        toolbarViewMvc.setTitle("Replies")
        toolbarViewMvc.setTitleStyle(18f, R.color.primary_text, true, View.TEXT_ALIGNMENT_CENTER)
        toolbarViewMvc.enableUpButtonAndListen { listener?.onNavigateUpClicked() }

        toolbar.addView(toolbarViewMvc.rootView)

        recyclerReplies.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                recyclerReplies.postDelayed({ listener?.onKeyboardUp() }, 100)
            }
        }

        recyclerReplies.apply {
            val lm = WrapContentLinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            lm.stackFromEnd = true
            layoutManager = lm
            adapter = replyAdapter
        }

        commenterTextUserName.setOnClickListener { listener?.onCommentUserClicked() }
        commenterImageProfile.setOnClickListener { listener?.onCommentUserClicked() }
        commentTextLike.setOnClickListener { listener?.onCommentLikeClicked() }
        commentTextReply.setOnClickListener { listener?.onCommentReplyClicked() }
        buttonPost.setOnClickListener { listener?.onPostClicked() }
    }

    override fun scrollToBottom() {
        recyclerReplies.postDelayed(
            { recyclerReplies.smoothScrollToPosition(0) },
            100
        )
    }

    override fun bindComment(comment: Comment) {
        commenterImageProfile.load(comment.userImageUrl)
        commenterTextUserName.text = comment.userName
        commentTime.setTimePassed(comment.createdAt, shorten = true)
        commentBody.text = comment.body
    }

    override fun requestInputFocus() {
        etInput.requestFocus()
    }

    override fun addReplies(replies: List<Reply>) {
        replyAdapter.addReplies(replies)
    }

    override fun addReply(reply: Reply) {
        replyAdapter.addReply(reply)
    }

    override fun getBody(): String = etInput.text.toString()

    override fun clearInputField() {
        etInput.text = null
    }
}