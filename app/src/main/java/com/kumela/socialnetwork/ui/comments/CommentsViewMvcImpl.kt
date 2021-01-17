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
import com.kumela.socialnetwork.models.list.CommentList
import com.kumela.socialnetwork.ui.adapters.comments.CommentsAdapter
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.toolbar.ToolbarViewMvc

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


    private val commentsAdapter = CommentsAdapter(viewMvcFactory) { listener?.onUserClicked(it) }

    init {
        toolbarViewMvc.setTitle("Comments")
        toolbarViewMvc.setTitleStyle(18f, R.color.primary_text, true, View.TEXT_ALIGNMENT_CENTER)

        toolbarViewMvc.enableUpButtonAndListen { listener?.onNavigateUpClicked() }

        toolbar.addView(toolbarViewMvc.rootView)

        recyclerComments.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            adapter = commentsAdapter
        }

        buttonPost.setOnClickListener { listener?.onPostClicked() }
    }

    override fun bindComments(comments: List<CommentList>) {
        commentsAdapter.bindComments(comments)
    }

    override fun addComment(comment: CommentList) {
        commentsAdapter.addComment(comment)
    }

    override fun getComment(): String = etInput.text.toString()

    override fun clearInputField() {
        etInput.text = null
    }
}