package com.kumela.socialnetwork.ui.comments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.Comment
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.adapters.comments.CommentsAdapter
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.toolbar.ToolbarViewMvc
import com.kumela.socialnetwork.ui.common.utils.WrapContentLinearLayoutManager


/**
 * Created by Toko on 24,September,2020
 **/

class CommentsViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<CommentsViewMvc.Listener>(
    inflater, parent, R.layout.fragment_comments
), CommentsViewMvc {

    private val toolbar: Toolbar = findViewById(R.id.toolbar)
    private val toolbarViewMvc = viewMvcFactory.newInstance(ToolbarViewMvc::class, toolbar)

    private val etInput: EditText = findViewById(R.id.et_input)
    private val buttonPost: MaterialButton = findViewById(R.id.button_post)
    private val recyclerComments: RecyclerView = findViewById(R.id.recycler_comments)

    private val commentsAdapter = CommentsAdapter(
        viewMvcFactory,
        object : CommentsAdapter.Listener {
            override fun onUserClicked(user: User) {
                listener?.onUserClicked(user)
            }

            override fun onLikeClicked(comment: Comment) {
                listener?.onLikeClicked(comment)
            }

            override fun onReplyClicked(comment: Comment) {
                listener?.onReplyClicked(comment)
            }

            override fun onReplierClicked(comment: Comment) {
                listener?.onReplierClicked(comment)
            }

            override fun onLastCommentBound() {
                listener?.onLastCommentBound()
            }
        })

    init {
        toolbarViewMvc.setTitle("Comments")
        toolbarViewMvc.setTitleStyle(18f, R.color.primary_text, true, View.TEXT_ALIGNMENT_CENTER)
        toolbarViewMvc.enableUpButtonAndListen { listener?.onNavigateUpClicked() }
        toolbar.addView(toolbarViewMvc.rootView)

        recyclerComments.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                recyclerComments.postDelayed({
                    listener?.onKeyboardUp()
                }, 100)
            }
        }

        recyclerComments.apply {
            val lm = WrapContentLinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            lm.stackFromEnd = true
            layoutManager = lm
            adapter = commentsAdapter
        }

        buttonPost.setOnClickListener { listener?.onPostClicked() }
    }

    override fun scrollToBottom() {
        recyclerComments.postDelayed({
            recyclerComments.smoothScrollToPosition(0)
        }, 100)
    }

    override fun addComments(comments: List<Comment>) {
        commentsAdapter.addComments(comments)
    }

    override fun addComment(comment: Comment) {
        commentsAdapter.addComment(comment)
    }

    override fun updateComment(comment: Comment) {
        commentsAdapter.updateComment(comment)
    }

    override fun getComment(): String = etInput.text.toString()

    override fun clearInputField() {
        etInput.text = null
    }
}