package com.kumela.socialnetwork.ui.story_uploder

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kumela.socialnetwork.network.common.Result
import com.kumela.socialnetwork.network.common.fold
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import com.kumela.socialnetwork.ui.common.viewmodels.ViewModelFactory
import javax.inject.Inject

/**
 * Created by Toko on 06,November,2020
 **/

class StoryUploaderFragment : BaseFragment(), StoryUploaderViewMvc.Listener {

    private lateinit var mViewMvc: StoryUploaderViewMvc
    private lateinit var mViewModel: StoryUploaderViewModel

    private lateinit var argImageUri: Uri

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mViewModelFactory: ViewModelFactory
    @Inject lateinit var mScreensNavigator: StoryUploaderScreensNavigator
    @Inject lateinit var mBottomNavHelper: BottomNavHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        injector.inject(this)

        mBottomNavHelper.hideBottomNav()

        mViewMvc = mViewMvcFactory.newInstance(StoryUploaderViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = StoryUploaderFragmentArgs.fromBundle(requireArguments())

        argImageUri = args.imageUri

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(StoryUploaderViewModel::class.java)

        mViewMvc.registerListener(this)

        mViewMvc.bindImage(argImageUri)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewMvc.unregisterListener()
    }

    override fun onCloseClicked() {
        mScreensNavigator.navigateUp()
    }

    override fun onUploadClicked() {
        lifecycleScope.launchWhenStarted {
            mViewMvc.showProgressIndicationAndDisableButtons()

            val uploadResult = mViewModel.uploadImage(argImageUri)
            when (uploadResult) {
                is Result.Success -> {
                    val imageUrl = uploadResult.value
                    val storyResult = mViewModel.createStory(imageUrl)
                    storyResult.fold(
                        onSuccess = {
                            mScreensNavigator.navigateUp()
                        },
                        onFailure = {
                            mViewMvc.hideProgressIndicationAndEnableButtons()
                        }
                    )
                }
                is Result.Failure ->  mViewMvc.hideProgressIndicationAndEnableButtons()
            }
        }
    }
}