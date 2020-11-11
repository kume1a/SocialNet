package com.kumela.socialnet.ui.story_uploder

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kumela.socialnet.ui.common.ViewMvcFactory
import com.kumela.socialnet.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnet.ui.common.controllers.BaseFragment
import javax.inject.Inject

/**
 * Created by Toko on 06,November,2020
 **/

class StoryUploaderFragment : BaseFragment(), StoryUploaderViewMvc.Listener,
    StoryUploaderViewModel.Listener {

    private lateinit var mViewMvc: StoryUploaderViewMvc
    private lateinit var mViewModel: StoryUploaderViewModel

    private lateinit var argImageUri: Uri

    @Inject lateinit var mScreensNavigator: StoryUploaderScreensNavigator
    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mBottomNavHelper: BottomNavHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        injector.inject(this)

        mBottomNavHelper.hideBottomNav()

        mViewMvc = mViewMvcFactory.newInstance(StoryUploaderViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = StoryUploaderFragmentArgs.fromBundle(requireArguments())

        argImageUri = args.imageUri

        mViewModel = ViewModelProvider(this).get(StoryUploaderViewModel::class.java)

        mViewMvc.registerListener(this)
        mViewModel.registerListener(this)

        mViewMvc.bindImage(argImageUri)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mViewMvc.unregisterListener()
        mViewModel.unregisterListener(this)
    }

    override fun onCloseClicked() {
        mScreensNavigator.navigateUp()
    }

    override fun onUploadClicked() {
        mViewMvc.showProgressIndicationAndDisableButtons()
        mViewModel.uploadStoryAndNotify(argImageUri)
    }

    // view model callbacks
    override fun onStoryUploaded() {
        mScreensNavigator.navigateUp()
    }

    override fun onStoryUploadFailed() {
        mViewMvc.hideProgressIndicationAndEnableButtons()
    }
}