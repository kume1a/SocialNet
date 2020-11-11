package com.kumela.socialnet.ui.adapters.story

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnet.models.firebase.UserModel
import com.kumela.socialnet.ui.common.ViewMvcFactory

/**
 * Created by Toko on 02,October,2020
 **/

class StoryAdapter(
    private val viewMvcFactory: ViewMvcFactory,
    private val listener: (Int, UserModel) -> Unit
) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>(), StoryItemViewMvc.Listener {

    private val items = arrayListOf<UserModel>()

    fun addStory(user: UserModel) {
        items.add(user)
        notifyItemInserted(items.size - 1)
    }

    fun bindStories(users: List<UserModel>) {
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

    override fun onStoryClicked(userModel: UserModel) {
        listener.invoke(items.indexOf(userModel), userModel)
    }
}