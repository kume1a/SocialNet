package com.kumela.socialnet.ui.adapters.story_profile

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnet.models.firebase.StoryModel
import com.kumela.socialnet.ui.common.ViewMvcFactory

/**
 * Created by Toko on 05,October,2020
 **/

class ProfileStoryAdapter(
    private val viewMvcFactory: ViewMvcFactory,
    private val listener: (StoryModel) -> Unit,
    private val onLastItemBound: () -> Unit
) : RecyclerView.Adapter<ProfileStoryAdapter.ProfileStoryViewHolder>(), ProfileStoryItemViewMvc.Listener {

    private val items = mutableListOf<StoryModel>()

    fun bindStories(stories: List<StoryModel>) {
        items.clear()
        items.addAll(stories)
        notifyDataSetChanged()
    }

    fun addStories(stories: List<StoryModel>) {
        val startIndex = items.size
        items.addAll(stories)
        notifyItemRangeInserted(startIndex, stories.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileStoryViewHolder {
        val viewMvc = viewMvcFactory.newInstance(ProfileStoryItemViewMvc::class, parent)
        viewMvc.registerListener(this)
        return ProfileStoryViewHolder(viewMvc)
    }

    override fun onBindViewHolder(holder: ProfileStoryViewHolder, position: Int) {
        if (position == items.size - 1) onLastItemBound.invoke()

        holder.viewMvc.bindStoryModel(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ProfileStoryViewHolder(val viewMvc: ProfileStoryItemViewMvc) :
        RecyclerView.ViewHolder(viewMvc.rootView)

    override fun onStoryClicked(storyModel: StoryModel) {
        listener.invoke(storyModel)
    }
}