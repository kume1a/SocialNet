package com.kumela.socialnet.ui.adapters.story_profile

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kumela.roundimageview.RoundedImageView
import com.kumela.socialnet.R
import com.kumela.socialnet.models.firebase.StoryModel
import com.kumela.socialnet.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnet.ui.common.utils.load

/**
 * Created by Toko on 05,October,2020
 **/

class ProfileStoryItemViewMvc(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<ProfileStoryItemViewMvc.Listener>(
    inflater, parent, R.layout.item_story_profile
) {

    interface Listener {
        fun onStoryClicked(storyModel: StoryModel)
    }

    private var storyModel: StoryModel? = null

    private val imageStory: RoundedImageView = findViewById(R.id.image)

    init {
        rootView.setOnClickListener { listener?.onStoryClicked(storyModel!!) }
    }

    fun bindStoryModel(storyModel: StoryModel) {
        this.storyModel = storyModel

        imageStory.load(storyModel.imageUri)
    }
}