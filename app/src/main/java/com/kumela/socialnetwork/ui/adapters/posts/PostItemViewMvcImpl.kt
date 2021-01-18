package com.kumela.socialnetwork.ui.adapters.posts

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kumela.socialnetwork.ui.views.RoundedImageView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.list.Post
import com.kumela.socialnetwork.ui.common.utils.load
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc

/**
 * Created by Toko on 05,October,2020
 **/

class PostItemViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<PostItemViewMvc.Listener>(
    inflater,
    parent,
    R.layout.item_post
), PostItemViewMvc {

    private var post: Post? = null

    private val image: RoundedImageView = findViewById(R.id.image)

    init {
        rootView.setOnClickListener { listener?.onPostClicked(post!!) }
    }

    override fun bindPostModel(post: Post) {
        this.post = post

        image.load(post.imageUrl)
    }
}