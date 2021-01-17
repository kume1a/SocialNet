package com.kumela.socialnetwork.ui.adapters.story

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.common.ViewMvcFactory

/**
 * Created by Toko on 02,October,2020
 **/

class StoryAdapter(
    private val viewMvcFactory: ViewMvcFactory,
    private val listener: (Int, User) -> Unit
) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>(), StoryItemViewMvc.Listener {

    private val items = arrayListOf<User>()

    fun addStory(user: User) {
        items.add(user)
        notifyItemInserted(items.size - 1)
    }

    fun bindStories(users: List<User>) {
        items.clear()
        items.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val viewMvc = viewMvcFactory.newInstance(StoryItemViewMvc::class, parent)
        viewMvc.registerListener(this)
        return StoryViewHolder(viewMvc)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.viewMvc.bindStoryModel(items[position])

        if (position == 0) {
            holder.viewMvc.revealAddIcon()
        } else {
            holder.viewMvc.hideAddIcon()
        }
    }

    override fun getItemCount(): Int = items.size

    class StoryViewHolder(val viewMvc: StoryItemViewMvc) : RecyclerView.ViewHolder(viewMvc.rootView)

    override fun onStoryClicked(user: User) {
        listener.invoke(items.indexOf(user), user)
    }
}