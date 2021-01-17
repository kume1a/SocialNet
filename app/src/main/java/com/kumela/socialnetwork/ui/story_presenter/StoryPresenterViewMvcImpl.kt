package com.kumela.socialnetwork.ui.story_presenter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.ui.views.RoundedImageView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.FeedStoryModel
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.adapters.story_images.ImagePagerAdapter
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.utils.OnRecyclerSnapListener
import com.kumela.socialnetwork.ui.common.utils.load
import com.kumela.socialnetwork.ui.views.PagerIndexIndicator

/**
 * Created by Toko on 09,November,2020
 **/

class StoryPresenterViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<StoryPresenterViewMvc.Listener>(
    inflater, parent, R.layout.fragment_story_presenter
), StoryPresenterViewMvc {

    private val pagerIndicator: PagerIndexIndicator = findViewById(R.id.pager_indicator)
    private val buttonClose: ImageButton = findViewById(R.id.button_close)
    private val recyclerImages: RecyclerView = findViewById(R.id.recycler_images)
    private val imageProfile: RoundedImageView = findViewById(R.id.image_profile)
    private val textUsername: TextView = findViewById(R.id.text_username)

    private val imagePagerAdapter = ImagePagerAdapter()

    init {
        buttonClose.setOnClickListener { listener?.onCloseClicked() }

        recyclerImages.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = imagePagerAdapter

            val snapHelper = PagerSnapHelper()
            addOnScrollListener(OnRecyclerSnapListener(snapHelper) { position ->
                listener?.onPageChanged(position)
            })
            snapHelper.attachToRecyclerView(this)
        }
    }

    override fun bindStoryAuthor(user: User) {
        imageProfile.load(user.imageUri)
        textUsername.text = user.username
    }

    override fun bindStories(feedStories: List<FeedStoryModel>) {
        imagePagerAdapter.bindImages(feedStories)
    }

    override fun bindImageCount(count: Int, initialIndex: Int) {
        pagerIndicator.setItemCount(count, initialIndex)
    }

    override fun nextIndex() {
        pagerIndicator.next()
    }

    override fun previousIndex() {
        pagerIndicator.previous()
    }

    override fun imageIndexTo(index: Int) {
        recyclerImages.scrollToPosition(index)
    }
}