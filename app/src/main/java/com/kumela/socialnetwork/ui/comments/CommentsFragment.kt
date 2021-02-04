package com.kumela.socialnetwork.ui.comments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kumela.socialnetwork.models.Comment
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.common.fold
import com.kumela.socialnetwork.ui.common.EventViewModel
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import com.kumela.socialnetwork.ui.common.viewmodels.ViewModelFactory
import javax.inject.Inject

/**
 * Created by Toko on 24,September,2020
 **/

class CommentsFragment : BaseFragment(), CommentsViewMvc.Listener {

    private lateinit var mViewMvc: CommentsViewMvc
    private lateinit var mViewModel: CommentsViewModel
    private lateinit var mEventViewModel: EventViewModel

    private var argPostId: Int? = null

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mViewModelFactory: ViewModelFactory
    @Inject lateinit var mBottomNavHelper: BottomNavHelper
    @Inject lateinit var mScreensNavigator: CommentsScreensNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        injector.inject(this)

        mBottomNavHelper.hideBottomNav()

        mViewMvc = mViewMvcFactory.newInstance(CommentsViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = CommentsFragmentArgs.fromBundle(requireArguments())

        argPostId = args.postId

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(CommentsViewModel::class.java)
        mEventViewModel = ViewModelProvider(requireActivity()).get(EventViewModel::class.java)

        mViewMvc.registerListener(this)

        val cachedComments = mViewModel.getCachedComments()
        lifecycleScope.launchWhenStarted {
            if (cachedComments != null) {
                mViewMvc.addComments(cachedComments.data)
            } else {
                fetchComments()
            }

            for (reply in mEventViewModel.getNewReplies()) {
                mViewModel.getCachedComments()?.data?.let { cached ->
                    val comment = cached.firstOrNull { comment -> comment.id == reply.commentId }
                    if (comment != null) {
                        val newReplies = if (comment.firstReplies != null) {
                            if (comment.firstReplies.size > 2) {
                                comment.firstReplies.removeLast()
                            }
                            comment.firstReplies.add(0, reply)
                            comment.firstReplies
                        } else {
                            mutableListOf(reply)
                        }

                        val newComment = comment.copy(
                            replyCount = (comment.replyCount ?: 0) + 1,
                            firstReplies = newReplies
                        )
                        val index = cached.indexOf(comment)
                        cached.remove(comment)
                        cached.add(index, newComment)

                        mViewMvc.updateComment(newComment)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mViewMvc.unregisterListener()
    }

    // view callbacks
    override fun onNavigateUpClicked() {
        mScreensNavigator.navigateUp()
    }

    override fun onKeyboardUp() {
        mViewMvc.scrollToBottom()
    }

    override fun onUserClicked(user: User) {
        mScreensNavigator.toUserProfile(user.id, user.imageUrl, user.name)
    }

    override fun onLikeClicked(comment: Comment) {
        Log.d(javaClass.simpleName, "onLikeClicked() called with: comment = $comment")
    }

    override fun onReplyClicked(comment: Comment) {
        mScreensNavigator.toReplies(
            argPostId!!,
            comment.id,
            comment.userId,
            comment.userName,
            comment.userImageUrl,
            comment.createdAt,
            comment.body
        )
    }

    override fun onReplierClicked(comment: Comment) {
        mScreensNavigator.toReplies(
            argPostId!!,
            comment.id,
            comment.userId,
            comment.userName,
            comment.userImageUrl,
            comment.createdAt,
            comment.body
        )
    }

    override fun onLastCommentBound() {
        lifecycleScope.launchWhenStarted { fetchComments() }
    }

    override fun onPostClicked() {
        val comment = mViewMvc.getComment().trim()

        if (comment.isBlank()) return

        mViewMvc.scrollToBottom()

        lifecycleScope.launchWhenStarted {
            val result = mViewModel.createComment(argPostId!!, comment)
            result.fold(
                onSuccess = { comment ->
                    mViewMvc.clearInputField()
                    mViewModel.getCachedComments()?.data?.add(comment)
                    mViewMvc.addComment(comment)
                    mEventViewModel.newCommentAdded(argPostId!!)
                },
                onFailure = { error ->
                    Log.e(javaClass.simpleName, "onPostClicked: error = $error")
                }
            )
        }
    }

    private suspend fun fetchComments() {
        val result = mViewModel.getComments(argPostId!!)
        result.fold(
            onSuccess = { response ->
                if (response == null) return@fold

                mViewMvc.addComments(response.data)
                for (comment in response.data) {
                    lifecycleScope.launchWhenStarted {
                        fetchReplies(comment)
                    }
                }
            },
            onFailure = { error ->
                Log.e(javaClass.simpleName, "fetchComments: $error")
            }
        )
    }

    private suspend fun fetchReplies(comment: Comment) {
        val result = mViewModel.getFirstReplies(argPostId!!, comment.id)
        when (result) {
            is Result.Success -> {
                val newComment =
                    comment.copy(firstReplies = result.value.data, replyCount = result.value.total)
                mViewMvc.updateComment(newComment)

                // update cache
                val cachedComments = mViewModel.getCachedComments()
                if (cachedComments?.data != null) {
                    val index = cachedComments.data.indexOf(comment)
                    try {
                        cachedComments.data.remove(comment)
                        cachedComments.data.add(index, newComment)
                    } catch (e: Exception) {
                        Log.e(javaClass.simpleName, "fetchReplies: ", e)
                    }
                }
            }
            is Result.Failure -> Log.e(javaClass.simpleName, "fetchReplies: ${result.failure}")
        }
    }
}