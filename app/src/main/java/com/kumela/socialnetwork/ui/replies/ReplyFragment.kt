package com.kumela.socialnetwork.ui.replies

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kumela.socialnetwork.models.Comment
import com.kumela.socialnetwork.models.Reply
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.network.common.fold
import com.kumela.socialnetwork.ui.common.EventViewModel
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import com.kumela.socialnetwork.ui.common.viewmodels.ViewModelFactory
import javax.inject.Inject


class ReplyFragment : BaseFragment(), ReplyViewMvc.Listener {

    private lateinit var mViewMvc: ReplyViewMvc
    private lateinit var mViewModel: ReplyViewModel
    private lateinit var mEventViewModel: EventViewModel

    private var argPostId: Int? = null
    private var argCommentId: Int? = null
    private var argCommentUserId: Int? = null
    private lateinit var argCommentUserName: String
    private lateinit var argCommentUserImageUrl: String

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mViewModelFactory: ViewModelFactory
    @Inject lateinit var mBottomNavHelper: BottomNavHelper
    @Inject lateinit var mScreensNavigator: ReplyScreensNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        injector.inject(this)

        mBottomNavHelper.hideBottomNav()

        mViewMvc = mViewMvcFactory.newInstance(ReplyViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = ReplyFragmentArgs.fromBundle(requireArguments())

        argPostId = args.postId
        argCommentId = args.commentId
        argCommentUserId = args.commentUserId
        argCommentUserName = args.commentUserName
        argCommentUserImageUrl = args.commentUserImageUrl

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(ReplyViewModel::class.java)
        mEventViewModel = ViewModelProvider(requireActivity()).get(EventViewModel::class.java)

        mViewMvc.registerListener(this)

        mViewMvc.bindComment(
            Comment(
                args.commentId,
                args.commentUserId,
                args.postId,
                args.commentUserName,
                args.commentUserImageUrl,
                args.commentCreatedAt,
                args.commentBody,
                null,
                null
            )
        )

        val cachedReplies = mViewModel.getCachedReplies()
        lifecycleScope.launchWhenStarted {
            if (cachedReplies != null) {
                mViewMvc.addReplies(cachedReplies.data)
            } else {
                fetchReplies()
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

    override fun onCommentUserClicked() {
        mScreensNavigator.toUserProfile(argCommentUserId!!, argCommentUserImageUrl, argCommentUserName)
    }

    override fun onCommentLikeClicked() {
        Log.d(javaClass.simpleName, "onCommentLikeClicked() called")
    }

    override fun onCommentReplyClicked() {
        mViewMvc.requestInputFocus()

        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    override fun onUserClicked(user: User) {
        mScreensNavigator.toUserProfile(user.id, user.imageUrl, user.name)
    }

    override fun onLikeClicked(reply: Reply) {
        Log.d(javaClass.simpleName, "onLikeClicked() called with: comment = $reply")
    }

    override fun onLastReplyBound() {
        lifecycleScope.launchWhenStarted { fetchReplies() }
    }

    override fun onPostClicked() {
        val reply = mViewMvc.getBody().trim()

        if (reply.isBlank()) return

        mViewMvc.scrollToBottom()

        lifecycleScope.launchWhenStarted {
            val result = mViewModel.createReply(argPostId!!, argCommentId!!, reply)
            result.fold(
                onSuccess = { reply ->
                    mViewMvc.clearInputField()
                    mViewModel.getCachedReplies()?.data?.add(reply)
                    mViewMvc.addReply(reply)
                    mEventViewModel.addNewReply(reply)
                    mEventViewModel.newCommentAdded(argPostId!!)
                },
                onFailure = { error ->
                    Log.e(javaClass.simpleName, "onPostClicked: error = $error")
                }
            )
        }
    }

    private suspend fun fetchReplies() {
        val result = mViewModel.getReplies(argPostId!!, argCommentId!!)
        result.fold(
            onSuccess = { response ->
                if (response == null) return@fold

                mViewMvc.addReplies(response.data)
            },
            onFailure = { error ->
                Log.e(javaClass.simpleName, "fetchReplies: $error")
            }
        )
    }
}