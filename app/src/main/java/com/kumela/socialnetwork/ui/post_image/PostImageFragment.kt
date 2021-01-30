package com.kumela.socialnetwork.ui.post_image

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.common.fold
import com.kumela.socialnetwork.network.repositories.ImageRepository
import com.kumela.socialnetwork.network.repositories.ImageType
import com.kumela.socialnetwork.network.repositories.PostRepository
import com.kumela.socialnetwork.network.repositories.UserRepository
import com.kumela.socialnetwork.ui.common.EventViewModel
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import com.kumela.socialnetwork.ui.common.dialogs.DialogManager
import javax.inject.Inject

/**
 * Created by Toko on 24,September,2020
 **/

class PostImageFragment : BaseFragment(), PostImageViewMvc.Listener {

    private lateinit var mViewMvc: PostImageViewMvc
    private lateinit var mEventViewModel: EventViewModel

    private var argImageUri: String? = null
    private var argImageBitmap: Bitmap? = null

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mBottomNavHelper: BottomNavHelper
    @Inject lateinit var mScreensNavigator: PostImageScreensNavigator
    @Inject lateinit var mDialogManager: DialogManager
    @Inject lateinit var mPostRepository: PostRepository
    @Inject lateinit var mImageRepository: ImageRepository
    @Inject lateinit var mUserRepository: UserRepository

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

        if (argImageUri == null && argImageBitmap == null) {
            throw IllegalStateException("both imageUri and imageBitmap should not be null")
        }

        mEventViewModel = ViewModelProvider(requireActivity()).get(EventViewModel::class.java)
        mViewMvc.registerListener(this)

        if (argImageUri != null) {
            mViewMvc.bindPostImage(Uri.parse(argImageUri))
        } else {
            mViewMvc.bindPostImage(argImageBitmap!!)
        }

        lifecycleScope.launchWhenStarted {
            val result = mUserRepository.fetchUser()
            result.fold(
                onSuccess = { user ->
                    mViewMvc.bindProfileImage(user.imageUrl)
                    mViewMvc.bindUsername(user.name)
                },
                onFailure = { error ->
                    Log.e(javaClass.simpleName, "onViewCreated: $error")
                    mDialogManager.showInfoDialog(
                        "Error occurred",
                        "please check your network connection"
                    )
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewMvc.unregisterListener()
    }

    override fun onBackPressed() {
        mScreensNavigator.navigateUp()
    }

    override fun onPostClicked() {
        lifecycleScope.launchWhenStarted {
            mViewMvc.showProgressIndicatorAndDisableInteractivity()

            val imageResult = if (argImageUri != null) {
                mImageRepository.uploadImage(ImageType.POST, Uri.parse(argImageUri))
            } else {
                mImageRepository.uploadImage(ImageType.POST, argImageBitmap!!)
            }

            when (imageResult) {
                is Result.Success -> createPost(imageResult.value)
                is Result.Failure -> {
                    Log.e(javaClass.simpleName, "onPostClicked: ", imageResult.failure)
                    mViewMvc.hideProgressIndicatorAndEnableInteractivity()
                    mDialogManager.showInfoDialog("Error occurred", "Image upload failed")
                }
            }
        }
    }

    private suspend fun createPost(imageUrl: String) {
        lifecycleScope.launchWhenStarted {
            val header = mViewMvc.getHeader().trim()
            val description = mViewMvc.getDescription().trim()

            val result = mPostRepository.createPost(imageUrl, header, description)
            result.fold(
                onSuccess = { post ->
                    mEventViewModel.addNewPost(post)
                    mScreensNavigator.navigateUp()
                },
                onFailure = { error ->
                    Log.e(javaClass.simpleName, "onCreatePostFailed: error: $error")
                    mViewMvc.hideProgressIndicatorAndEnableInteractivity()
                    mDialogManager.showInfoDialog("Error occurred", "Please try again later")
                }
            )
        }
    }
}