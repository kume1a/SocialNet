package com.kumela.socialnet.ui.adapters.posts

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kumela.roundimageview.RoundedImageView
import com.kumela.socialnet.R
import com.kumela.socialnet.models.list.PostModel
import com.kumela.socialnet.ui.common.utils.load
import com.kumela.socialnet.ui.common.mvc.BaseObservableViewMvc

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