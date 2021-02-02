package com.kumela.socialnetwork.ui.adapters.story_profile

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kumela.socialnetwork.ui.views.RoundedImageView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.Story
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.utils.load

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
        fun onStoryClicked(story: Story)
    }

    private var story: Story? = null

    private val imageStory: RoundedImageView = findViewById(R.id.image)

    init {
        rootView.setOnClickListener { listener?.onStoryClicked(story!!) }
    }

    fun bindStoryModel(story: Story) {
        this.story = story

        imageStory.load(story.imageUrl)
    }
}