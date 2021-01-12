package com.kumela.socialnetwork.ui.adapters.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kumela.socialnetwork.ui.views.RoundedImageView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.list.PostModel
import com.kumela.socialnetwork.ui.common.mvc.BaseViewMvc
import com.kumela.socialnetwork.ui.common.utils.load

/**
 * Created by Toko on 04,October,2020
 **/

class ExploreItemTIRViewMvc(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseViewMvc(
    inflater,
    parent,
    R.layout.item_explore_triple_image_row
) {

    private var postModels: List<PostModel>? = null

    private val image1: RoundedImageView = findViewById(R.id.image1)
    private val image2: RoundedImageView = findViewById(R.id.image2)
    private val image3: RoundedImageView = findViewById(R.id.image3)

    fun bindExploreModels(postModels: List<PostModel>) {
        postModels.getOrNull(0)?.let { image1.load(it.postImageUri) }
        postModels.getOrNull(1)?.let { image2.load(it.postImageUri) }
        postModels.getOrNull(2)?.let { image3.load(it.postImageUri) }

        this.postModels = postModels
    }
}