package com.kumela.socialnetwork.ui.adapters.posts

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.models.list.Post
import com.kumela.socialnetwork.ui.common.ViewMvcFactory

/**
 * Created by Toko on 05,October,2020
 **/

class PostsAdapter(
    private val viewMvcFactory: ViewMvcFactory,
    private val listener: (Post) -> Unit
) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>(), PostItemViewMvc.Listener {

    private val items = arrayListOf<Post>()

    fun bindPosts(posts: List<Post>) {
        items.clear()
        items.addAll(posts)
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

    override fun onPostClicked(post: Post) {
        listener.invoke(post)
    }
}