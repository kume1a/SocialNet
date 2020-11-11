package com.kumela.socialnet.ui.user_profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.kumela.roundimageview.RoundedImageView
import com.kumela.socialnet.R
import com.kumela.socialnet.models.firebase.StoryModel
import com.kumela.socialnet.models.list.PostModel
import com.kumela.socialnet.ui.adapters.posts.PostsAdapter
import com.kumela.socialnet.ui.adapters.story_profile.ProfileStoryAdapter
import com.kumela.socialnet.ui.common.ViewMvcFactory
import com.kumela.socialnet.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnet.ui.common.toolbar.ToolbarViewMvc
import com.kumela.socialnet.ui.common.utils.load

/**
 * Created by Toko on 17,October,2020
 **/

class UserProfileViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<UserProfileViewMvc.Listener>(
    inflater, parent, R.layout.fragment_user_profile
), UserProfileViewMvc {

    private val toolbar: Toolbar = findViewById(R.id.toolbar)
    private val toolbarViewMvc = viewMvcFactory.newInstance(ToolbarViewMvc::class, toolbar)

    private val imageProfile: RoundedImageView = findViewById(R.id.image_profile)
    private val textUsername: TextView = findViewById(R.id.text_username)
    private val textBio: TextView = findViewById(R.id.text_bio)
    private val buttonFollow: MaterialButton = findViewById(R.id.button_follow)
    private val buttonSendMessage: ImageButton = findViewById(R.id.button_send_message)
    private val textPostCount: TextView = findViewById(R.id.text_post_count)
    private val textFollowerCount: TextView = findViewById(R.id.text_follower_count)
    private val textFollowerHeader: TextView = findViewById(R.id.text_followers)
    private val textFollowingCount: TextView = findViewById(R.id.text_following_count)
    private val textFollowingHeader: TextView = findViewById(R.id.text_following)
    private val textStoriesHeader: TextView = findViewById(R.id.text_stories_header)
    private val recyclerStories: RecyclerView = findViewById(R.id.recycler_stories)
    private val textPostsHeader: TextView = findViewById(R.id.text_posts_header)
    private val recyclerPosts: RecyclerView = findViewById(R.id.recycler_posts)
    private val imageCamera: ImageView = findViewById(R.id.image_camera)
    private val textNoPostsYet: TextView = findViewById(R.id.text_no_posts_yet)

    private val storyAdapter = ProfileStoryAdapter(viewMvcFactory,
        listener = { listener?.onStoryItemClicked(it) },
        onLastItemBound = { listener?.onLastStoryBound() }
    )
    private val postsAdapter = PostsAdapter(viewMvcFactory) { postModel ->
        listener?.onPostItemClicked(postModel)
    }

    init {
        toolbarViewMvc.enableUpButtonAndListen { listener?.onNavigateUpClicked() }
        toolbar.addView(toolbarViewMvc.rootView)

        recyclerStories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = storyAdapter
        }

        recyclerPosts.apply {
            layoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
            adapter = postsAdapter
        }

        buttonFollow.setOnClickListener { listener?.onFollowClicked() }
        buttonSendMessage.setOnClickListener { listener?.onSendMessageClicked() }
        textFollowerCount.setOnClickListener { listener?.onFollowerClicked() }
        textFollowerHeader.setOnClickListener { listener?.onFollowerClicked() }
        textFollowingCount.setOnClickListener { listener?.onFollowingClicked() }
        textFollowingHeader.setOnClickListener { listener?.onFollowingClicked() }
    }

    override fun bindProfileImage(imageUri: String) {
        imageProfile.load(imageUri)
    }

    override fun bindUsername(username: String) {
        textUsername.text = username
    }

    override fun bindBio(bio: String) {
        if (bio.isEmpty()) return

        textBio.visibility = View.VISIBLE
        textBio.text = bio
    }

    override fun bindPostCount(postCount: Int) {
        textPostCount.text = postCount.toString()
    }

    override fun bindFollowerCount(followerCount: Int) {
        textFollowerCount.text = followerCount.toString()
    }

    override fun bindFollowingCount(followingCount: Int) {
        textFollowingCount.text = followingCount.toString()
    }

    override fun bindPosts(posts: List<PostModel>) {
        showPosts()
        postsAdapter.bindPosts(posts)
    }

    override fun bindStories(stories: List<StoryModel>) {
        showStories()
        storyAdapter.bindStories(stories)
    }

    override fun addStories(storyModels: List<StoryModel>) {
        showStories()
        storyAdapter.addStories(storyModels)
    }

    override fun showNoPostsAvailable() {
        imageCamera.visibility = View.VISIBLE
        textNoPostsYet.visibility = View.VISIBLE
    }

    override fun setFollowingButtonText(text: String) {
        buttonFollow.text = text
    }

    private fun showPosts() {
        if (!textPostsHeader.isVisible) {
            textPostsHeader.visibility = View.VISIBLE
        }
        if (!recyclerPosts.isVisible) {
            recyclerPosts.visibility = View.VISIBLE
        }
    }

    private fun showStories() {
        if (!textStoriesHeader.isVisible) {
            textStoriesHeader.visibility = View.VISIBLE
        }
        if (!recyclerStories.isVisible) {
            recyclerStories.visibility = View.VISIBLE
        }
    }
}