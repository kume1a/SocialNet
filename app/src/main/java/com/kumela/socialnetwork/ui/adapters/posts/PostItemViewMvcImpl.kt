package com.kumela.socialnetwork.ui.adapters.posts

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kumela.socialnetwork.ui.common.views.RoundedImageView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.list.PostModel
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

    private var postModel: PostModel? = null

    private val image: RoundedImageView = findViewById(R.id.image)

    init {
        rootView.setOnClickListener { listener?.onPostClicked(postModel!!) }
    }

    override fun bindPostModel(postModel: PostModel) {
        this.postModel = postModel

        image.load(postModel.postImageUri)
    }
}