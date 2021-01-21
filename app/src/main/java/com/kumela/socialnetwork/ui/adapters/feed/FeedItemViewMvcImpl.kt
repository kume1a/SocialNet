package com.kumela.socialnetwork.ui.adapters.feed

import android.graphics.drawable.Animatable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.kumela.socialnetwork.ui.views.RoundedImageView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.Feed
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.utils.load
import com.kumela.socialnetwork.ui.common.utils.setOnDoubleClickListener
import com.kumela.socialnetwork.ui.common.utils.setTimePassed


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

    private var feedModel: Feed? = null
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
            listener?.onProfileImageClicked(position!!, feedModel!!)
        }
        textUsername.setOnClickListener {
            listener?.onUsernameClicked(position!!, feedModel!!)
        }
        imageHeart.setOnClickListener {
            listener?.onLikeClicked(position!!, feedModel!!)
        }
        textLikesCount.setOnClickListener {
            listener?.onLikeCountClicked(position!!, feedModel!!)
        }
        imageComments.setOnClickListener {
            listener?.onCommentsClicked(position!!, feedModel!!)
        }
        textCommentsCount.setOnClickListener {
            listener?.onCommentCountClicked(position!!, feedModel!!)
        }
        imageButtonMenu.setOnClickListener {
            listener?.onMenuClicked()
        }
        imagePost.setOnDoubleClickListener {
            val drawable = if (feedModel!!.isLiked) {
                avdDislike.visibility = View.VISIBLE
                avdDislike.drawable
            } else {
                avdLike.visibility = View.VISIBLE
                avdLike.drawable
            }

            (drawable as Animatable).start()
            listener?.onPostDoubleClicked(position!!, feedModel!!)
        }
    }

    override fun bindPost(position: Int, feed: Feed) {
        this.position = position
        this.feedModel = feed

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