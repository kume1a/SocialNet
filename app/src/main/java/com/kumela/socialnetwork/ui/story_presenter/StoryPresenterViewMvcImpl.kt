package com.kumela.socialnetwork.ui.story_presenter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.utils.load
import com.kumela.socialnetwork.ui.views.RoundedImageView
import com.kumela.socialnetwork.ui.views.StoryProgressView

/**
 * Created by Toko on 09,November,2020
 **/

class StoryPresenterViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseObservableViewMvc<StoryPresenterViewMvc.Listener>(
    inflater, parent, R.layout.fragment_story_presenter
), StoryPresenterViewMvc {

    private val image: ImageView = findViewById(R.id.image)
    private val storyProgress: StoryProgressView = findViewById(R.id.story_progress_view)
    private val viewReverse: View = findViewById(R.id.view_reverse)
    private val viewSkip: View = findViewById(R.id.view_skip)
    private val buttonClose: ImageButton = findViewById(R.id.button_close)
    private val imageProfile: RoundedImageView = findViewById(R.id.image_profile)
    private val textUsername: TextView = findViewById(R.id.text_username)

    init {
        buttonClose.setOnClickListener { listener?.onCloseClicked() }
        imageProfile.setOnClickListener { listener?.onUserClicked() }
        textUsername.setOnClickListener { listener?.onUserClicked() }

        @SuppressLint("ClickableViewAccessibility")
        val onTouchListener = View.OnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    return@OnTouchListener listener?.onPressDown() ?: false
                }
                MotionEvent.ACTION_UP -> {
                    return@OnTouchListener listener?.onPressUp() ?: false
                }
            }
            false
        }
        viewReverse.setOnClickListener { listener?.onReverse() }
        viewReverse.setOnTouchListener(onTouchListener)
        viewSkip.setOnClickListener { listener?.onSkip() }
        viewSkip.setOnTouchListener(onTouchListener)

        storyProgress.listener = object : StoryProgressView.Listener {
            override fun onNext() {
                listener?.onNext()
            }

            override fun onPrevious() {
                listener?.onPrevious()
            }

            override fun onBack() {
                listener?.onBack()
            }

            override fun onComplete() {
                listener?.onComplete()
            }
        }
    }

    override fun bindUser(user: User) {
        imageProfile.load(user.imageUrl)
        textUsername.text = user.name
    }

    override fun start() {
        storyProgress.start()
    }

    override fun bindImage(imageUrl: String) {
        image.load(imageUrl)
    }

    override fun pause() {
        storyProgress.pause()
    }

    override fun resume() {
        storyProgress.resume()
    }

    override fun bindCount(count: Int) {
        storyProgress.bindCount(count)
    }

    override fun reverse() {
        storyProgress.reverse()
    }

    override fun skip() {
        storyProgress.skip()
    }
}