package com.kumela.socialnetwork.ui.adapters.posts

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.models.list.PostModel
import com.kumela.socialnetwork.ui.common.ViewMvcFactory

/**
 * Created by Toko on 05,October,2020
 **/

class PostsAdapter(
    private val viewMvcFactory: ViewMvcFactory,
    private val listener: (PostModel) -> Unit
) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>(), PostItemViewMvc.Listener {

    private val items = arrayListOf<PostModel>()

    fun bindPosts(postModels: List<PostModel>) {
        items.clear()
        items.addAll(postModels)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val viewMvc = viewMvcFactory.newInstance(PostItemViewMvc::class, parent)
        viewMvc.registerListener(this)
        return PostViewHolder(viewMvc)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.viewMvc.bindPostModel(items[position])
    }

    override fun getItemCount(): Int = items.size

    class PostViewHolder(val viewMvc: PostItemViewMvc) : RecyclerView.ViewHolder(viewMvc.rootView)

    override fun onPostClicked(postModel: PostModel) {
        listener.invoke(postModel)
    }
}