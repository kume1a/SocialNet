package com.kumela.socialnetwork.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.models.Story
import com.kumela.socialnetwork.models.list.Post
import com.kumela.socialnetwork.ui.adapters.posts.PostsAdapter
import com.kumela.socialnetwork.ui.adapters.story_profile.ProfileStoryAdapter
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc
import com.kumela.socialnetwork.ui.common.toolbar.ToolbarViewMvc
import com.kumela.socialnetwork.ui.common.utils.load
import com.kumela.socialnetwork.ui.views.RoundedImageView

/**
 * Created by Toko on 24,September,2020
 **/

class ProfileViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<ProfileViewMvc.Listener>(
    inflater, parent, R.layout.fragment_profile
), ProfileViewMvc {

    private val toolbar: Toolbar = findViewById(R.id.toolbar)
    private val toolbarViewMvc = viewMvcFactory.newInstance(ToolbarViewMvc::class, toolbar)

    private val imageProfile: RoundedImageView = findViewById(R.id.image_profile)
    private val textUsername: TextView = findViewById(R.id.text_username)
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

    private val storyAdapter = ProfileStoryAdapter(
        viewMvcFactory = viewMvcFactory,
        listener = { storyModel -> listener?.onStoryItemClicked(storyModel) },
        onLastItemBound = { listener?.onLastStoryBound() }
    )

    private val postsAdapter = PostsAdapter(
        viewMvcFactory = viewMvcFactory,
        listener = { post ->
            listener?.onPostItemClicked(post)
        },
        onLastItemBound = {
            listener?.onLastPostBound()
        }
    )

    init {
        toolbarViewMvc.enableMenuButtonAndListen(R.menu.menu_profile) menu@{ menuItem ->
            return@menu when (menuItem.itemId) {
                R.id.menu_settings -> {
                    Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_sign_out -> {
                    listener?.onSignOutClicked()
                    true
                }
                else -> false
            }
        }
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

    override fun bindPostCount(postCount: Int) {
        textPostCount.text = postCount.toString()
    }

    override fun bindFollowerCount(followerCount: Int) {
        textFollowerCount.text = followerCount.toString()
    }

    override fun bindFollowingCount(followingCount: Int) {
        textFollowingCount.text = followingCount.toString()
    }

    override fun insetPostsAtTop(posts: List<Post>) {
        showPosts()
        recyclerPosts.post { postsAdapter.insertPosts(posts) }
    }

    override fun addPosts(posts: List<Post>) {
        showPosts()
        recyclerPosts.post { postsAdapter.addPosts(posts) }
    }

    override fun bindStories(stories: List<Story>) {
        showStories()
        storyAdapter.bindStories(stories)
    }

    override fun addStories(stories: List<Story>) {
        showStories()
        storyAdapter.addStories(stories)
    }

    override fun showNoPostsAvailable() {
        imageCamera.visibility = View.VISIBLE
        textNoPostsYet.visibility = View.VISIBLE
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