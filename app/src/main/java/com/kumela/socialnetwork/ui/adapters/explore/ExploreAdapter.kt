package com.kumela.socialnet.ui.adapters.explore

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnet.models.list.PostModel
import com.kumela.socialnet.ui.common.ViewMvcFactory
import kotlin.math.ceil

/**
 * Created by Toko on 03,October,2020
 **/

class ExploreAdapter(
    private val viewMvcFactory: ViewMvcFactory,
    private val onScrolledToBottomListener: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = ArrayList<PostModel>()

    fun bindPosts(postModels: List<PostModel>) {
        items.clear()
        items.addAll(postModels)
        notifyDataSetChanged()
    }

    fun addPosts(postModels: List<PostModel>) {
        val oldItemsSize = items.size
        items.addAll(postModels)
        notifyItemRangeInserted(oldItemsSize, ceil(postModels.size.toFloat() / 3).toInt())
    }

    override fun getItemViewType(position: Int): Int {
        if (position == items.size / 3 - 1) {
            onScrolledToBottomListener.invoke()
        }

        return if ((position + 1) % 3 == 0) {
            VIEW_TYPE_2
        } else VIEW_TYPE_1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_1 -> {
                val viewMvc = viewMvcFactory.newInstance(ExploreItemTIRViewMvc::class, parent)
                ExploreViewHolder1(viewMvc)
            }
            VIEW_TYPE_2 -> {
                val viewMvc = viewMvcFactory.newInstance(ExploreItemDIRViewMvc::class, parent)
                ExploreViewHolder2(viewMvc)
            }
            else -> throw RuntimeException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val getSublist: () -> List<PostModel> = {
            try {
                items.subList(position * 3, (position + 1) * 3)
            } catch (e: IndexOutOfBoundsException) {
                items.subList(position * 3, items.size)
            }
        }

        when (holder.itemViewType) {
            VIEW_TYPE_1 -> {
                val viewMvc = (holder as ExploreViewHolder1).viewMvc
                viewMvc.bindExploreModels(getSublist())
            }
            VIEW_TYPE_2 -> {
                val viewMvc = (holder as ExploreViewHolder2).viewMvc
                viewMvc.bindExploreModels(getSublist())
            }
        }
    }

    override fun getItemCount(): Int = ceil(items.size.toFloat() / 3).toInt()

    class ExploreViewHolder1(val viewMvc: ExploreItemTIRViewMvc) :
        RecyclerView.ViewHolder(viewMvc.rootView)

    class ExploreViewHolder2(val viewMvc: ExploreItemDIRViewMvc) :
        RecyclerView.ViewHolder(viewMvc.rootView)

    companion object {
        private const val VIEW_TYPE_1 = 0
        private const val VIEW_TYPE_2 = 1
    }
}