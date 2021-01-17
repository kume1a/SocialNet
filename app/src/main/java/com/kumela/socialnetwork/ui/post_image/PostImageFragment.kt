package com.kumela.socialnetwork.ui.post_image

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.models.list.Post
import com.kumela.socialnetwork.network.firebase.ImageType
import com.kumela.socialnetwork.network.firebase.UserUseCase
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import com.kumela.socialnetwork.ui.common.dialogs.DialogManager
import javax.inject.Inject

/**
 * Created by Toko on 24,September,2020
 **/

class PostImageFragment : BaseFragment(), PostImageViewMvc.Listener,
    PostImageViewModel.Listener {

    private lateinit var mViewMvc: PostImageViewMvc
    private lateinit var mViewModel: PostImageViewModel

    private var argImageUri: String? = null
    private var argImageBitmap: Bitmap? = null

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mBottomNavHelper: BottomNavHelper
    @Inject lateinit var mScreensNavigator: PostImageScreensNavigator
    @Inject lateinit var mDialogManager: DialogManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        injector.inject(this)

        mBottomNavHelper.hideBottomNav()

        mViewMvc = mViewMvcFactory.newInstance(PostImageViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = PostImageFragmentArgs.fromBundle(requireArguments())

        argImageUri = args.imageUri
        argImageBitmap = args.imageBitmap

        mViewModel = ViewModelProvider(this).get(PostImageViewModel::class.java)

        if (argImageUri == null && argImageBitmap == null) {
            throw IllegalStateException("both imageUri and imageBitmap should not be null")
        }

        if (argImageUri != null) {
            mViewMvc.bindPostImage(Uri.parse(argImageUri))
        } else {
            mViewMvc.bindPostImage(argImageBitmap!!)
        }
    }

    override fun onStart() {
        super.onStart()
        mViewMvc.registerListener(this)
        mViewModel.registerListener(this)

        mViewModel.fetchUserAndNotify()
    }

    override fun onStop() {
        super.onStop()
        mViewMvc.unregisterListener()
        mViewModel.unregisterListener(this)
    }

    override fun onBackPressed() {
        mScreensNavigator.navigateUp()
    }

    override fun onPostClicked() {
        mViewMvc.showProgressIndicatorAndDisableInteractivity()
        if (argImageUri != null) {
            mViewModel.uploadImageAndNotify(ImageType.POST, Uri.parse(argImageUri))
        } else {
            mViewModel.uploadImageAndNotify(ImageType.POST, argImageBitmap!!)
        }
    }

    override fun onUserFetched(user: User) {
        mViewMvc.bindProfileImage(user.imageUrl)
        mViewMvc.bindUsername(user.name)
    }

    // image use case
    override fun onImageUploaded(uri: String) {
        val uid = UserUseCase.uid
        val timeStamp = System.currentTimeMillis()
        val header = mViewMvc.getHeader().trim()
        val description = mViewMvc.getDescription().trim()

        val post = Post("", uid, timeStamp, uri, 0, 0, header, description)

        mViewModel.createPostAndNotify(post)
    }

    override fun onImageUploadFailed(e: Exception) {
        Log.e(javaClass.simpleName, "onImageUploadFailed: ", e)
        mViewMvc.hideProgressIndicatorAndEnableInteractivity()
        mDialogManager.showInfoDialog("Error occurred", "Image upload failed")
    }

    // post use case
    override fun onCreatePost() {
        mScreensNavigator.navigateUp()
    }

    override fun onCreatePostFailed(e: Exception) {
        Log.e(javaClass.simpleName, "onCreatePostFailed: ", e)
        mViewMvc.hideProgressIndicatorAndEnableInteractivity()
        mDialogManager.showInfoDialog("Error occurred", "Please try again later")
    }
}