package com.kumela.socialnet.ui.adapters.story_images

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnet.R
import com.kumela.socialnet.models.firebase.FeedStoryModel
import com.kumela.socialnet.ui.common.utils.load

/**
 * Created by Toko on 07,November,2020
 **/

class ImagePagerAdapter : RecyclerView.Adapter<ImagePagerAdapter.ImagePagerViewHolder>() {

    private val items = ArrayList<FeedStoryModel>()

    fun bindImages(feedStories: List<FeedStoryModel>) {
        this.items.clear()
        this.items.addAll(feedStories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImagePagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImagePagerViewHolder, position: Int) {
//        holder.textTime.setTimePassed(items[position].timestamp, shorten = true)
        holder.image.load(items[position].imageUri)
    }

    override fun getItemCount(): Int = items.size

    class ImagePagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
//        val textTime: TextView = itemView.findViewById(R.id.text_time)
    }
}