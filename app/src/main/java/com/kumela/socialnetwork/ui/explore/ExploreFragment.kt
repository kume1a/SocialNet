package com.kumela.socialnet.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kumela.socialnet.models.list.PostModel
import com.kumela.socialnet.ui.common.ViewMvcFactory
import com.kumela.socialnet.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnet.ui.common.controllers.BaseFragment
import com.kumela.socialnet.ui.common.viewmodels.ViewModelFactory
import javax.inject.Inject

/**
 * Created by Toko on 24,September,2020
 **/

class ExploreFragment : BaseFragment(), ExploreViewMvc.Listener,
    ExploreViewModel.Listener {

    private lateinit var mViewMvc: ExploreViewMvc
    private lateinit var mViewModel: ExploreViewModel

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mViewModelFactory: ViewModelFactory
    @Inject lateinit var mScreensNavigator: ExploreScreensNavigator
    @Inject lateinit var mBottomNavHelper: BottomNavHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        injector.inject(this)

        mBottomNavHelper.showBottomNav()

        mViewMvc = mViewMvcFactory.newInstance(ExploreViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(requireActivity(), mViewModelFactory).get(ExploreViewModel::class.java)

        mViewMvc.registerListener(this)
        mViewModel.registerListener(this)

        val postModels = mViewModel.getPostModels()
        if (postModels.isNotEmpty()) {
            mViewMvc.bindPosts(postModels)
        } else {
            mViewModel.fetchNextPostPageAndNotify()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mViewMvc.unregisterListener()
        mViewModel.unregisterListener(this)
    }

    override fun onSearchClicked() {
        mScreensNavigator.toSearch()
    }

    override fun onScrolledToBottom() {
        mViewModel.fetchNextPostPageAndNotify()
    }

    // view model callbacks
    override fun onPostsFetched(postModels: List<PostModel>) {
        mViewMvc.addPosts(postModels)
    }
}