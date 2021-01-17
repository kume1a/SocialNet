package com.kumela.socialnetwork.ui.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kumela.socialnetwork.models.Comment
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.models.list.CommentList
import com.kumela.socialnetwork.network.firebase.UserUseCase
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import javax.inject.Inject

/**
 * Created by Toko on 24,September,2020
 **/

class CommentsFragment : BaseFragment(), CommentsViewMvc.Listener, CommentsViewModel.Listener {

    private lateinit var mViewMvc: CommentsViewMvc
    private lateinit var mViewModel: CommentsViewModel

    private lateinit var argPostId: String
    private lateinit var argCurrentUserId: String
    private lateinit var argCurrentUserImageUri: String
    private lateinit var argCurrentUserUsername: String

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
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
        argCurrentUserId = args.currentUserId
        argCurrentUserImageUri = args.currentUserProfileImageUri
        argCurrentUserUsername = args.currentUserUsername

        mViewModel = ViewModelProvider(this).get(CommentsViewModel::class.java)

        mViewModel.registerListener(this)
        mViewMvc.registerListener(this)

        val comments = mViewModel.getComments()

        if (comments.isNotEmpty()) {
            mViewMvc.bindComments(comments)
        } else {
            mViewModel.fetchCommentsAndNotify(argPostId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mViewModel.unregisterListener(this)
        mViewMvc.unregisterListener()
    }

    // view callbacks
    override fun onNavigateUpClicked() {
        mScreensNavigator.navigateUp()
    }

    override fun onUserClicked(user: User) {
//        if (user.id != UserUseCase.uid) {
//            mScreensNavigator.toUserProfile(user.id, user.imageUrl, user.name)
//        }
    }

    override fun onPostClicked() {
        val comment = mViewMvc.getComment()

        if (comment.isBlank()) return

        val commentModel = Comment(UserUseCase.uid, System.currentTimeMillis(), comment)
        val commentListModel =  CommentList(
            argCurrentUserId,
            argCurrentUserImageUri,
            argCurrentUserUsername,
            comment,
            System.currentTimeMillis()
        )

        mViewModel.createComment(argPostId, commentModel)
        mViewMvc.clearInputField()
        mViewMvc.addComment(commentListModel)
    }

    // view model callbacks
    override fun onCommentFetched(comment: CommentList) {
        mViewMvc.addComment(comment)
    }
}