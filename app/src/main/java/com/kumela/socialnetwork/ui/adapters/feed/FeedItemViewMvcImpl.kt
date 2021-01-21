package com.kumela.socialnetwork.ui.adapters.feed

import android.graphics.drawable.Animatable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.Feed
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.utils.load
import com.kumela.socialnetwork.ui.common.utils.setOnDoubleClickListener
import com.kumela.socialnetwork.ui.common.utils.setTimePassed
import com.kumela.socialnetwork.ui.views.RoundedImageView


/**
 * Created by Toko on 01,October,2020
 **/

class FeedItemViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<FeedItemViewMvc.Listener>(
    inflater,
    parent,
    R.layout.item_feed
), FeedItemViewMvc {

    private var feed: Feed? = null
    private var position: Int? = null

    private val imageProfile: RoundedImageView = findViewById(R.id.image_profile)
    private val textUsername: TextView = findViewById(R.id.text_username)
    private val imageButtonMenu: ImageButton = findViewById(R.id.menu_horizontal)
    private val textTime: TextView = findViewById(R.id.text_time)
    private val imagePost: RoundedImageView = findViewById(R.id.image_post)
    private val avdLike: ImageView = findViewById(R.id.avd_like)
    private val avdDislike: ImageView = findViewById(R.id.avd_dislike)
    private val textHeader: TextView = findViewById(R.id.text_header)
    private val imageHeart: ImageView = findViewById(R.id.image_likes)
    private val textLikesCount: TextView = findViewById(R.id.text_count_likes)
    private val imageComments: ImageView = findViewById(R.id.image_comments)
    private val textCommentsCount: TextView = findViewById(R.id.text_count_comments)
    private val textDescription: TextView = findViewById(R.id.text_description)

    init {
        imageProfile.setOnClickListener {
            listener?.onProfileImageClicked(position!!, feed!!)
        }
        textUsername.setOnClickListener {
            listener?.onUsernameClicked(position!!, feed!!)
        }
        imageHeart.setOnClickListener {
            listener?.onLikeClicked(position!!, feed!!)
        }
        textLikesCount.setOnClickListener {
            listener?.onLikeCountClicked(position!!, feed!!)
        }
        imageComments.setOnClickListener {
            listener?.onCommentsClicked(position!!, feed!!)
        }
        textCommentsCount.setOnClickListener {
            listener?.onCommentCountClicked(position!!, feed!!)
        }
        imageButtonMenu.setOnClickListener {
            listener?.onMenuClicked()
        }
        imagePost.setOnDoubleClickListener {
            listener?.onPostDoubleClicked(position!!, feed!!)
        }
    }

    override fun bindPost(position: Int, feed: Feed) {
        if (this.feed != null && this.feed!!.isLiked != feed.isLiked) {
            val drawable = if (!feed.isLiked) {
                avdDislike.visibility = View.VISIBLE
                avdDislike.drawable
            } else {
                avdLike.visibility = View.VISIBLE
                avdLike.drawable
            }

            (drawable as Animatable).start()
        }

        this.position = position
        this.feed = feed

        imageProfile.load(feed.userImageUrl)
        imagePost.load(feed.imageUrl)
        textTime.setTimePassed(feed.createdAt)
        textUsername.text = feed.userName
        textHeader.text = feed.header

        textLikesCount.text = feed.likeCount.toString()
        textCommentsCount.text = feed.commentCount.toString()

        textDescription.text = feed.description

        if (feed.isLiked) {
            imageHeart.setImageResource(R.drawable.ic_heart_filled)
        } else {
            imageHeart.setImageResource(R.drawable.ic_heart)
        }
    }
}