package com.kumela.socialnetwork.ui.explore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kumela.socialnetwork.network.common.fold
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import com.kumela.socialnetwork.ui.common.viewmodels.ViewModelFactory
import javax.inject.Inject

/**
 * Created by Toko on 24,September,2020
 **/

class ExploreFragment : BaseFragment(), ExploreViewMvc.Listener {

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
    ): View {
        injector.inject(this)

        mBottomNavHelper.showBottomNav()

        mViewMvc = mViewMvcFactory.newInstance(ExploreViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(
            this,
            mViewModelFactory
        ).get(ExploreViewModel::class.java)

        mViewMvc.registerListener(this)

        val posts = mViewModel.getCachedPosts()
        lifecycleScope.launchWhenStarted {
            if (posts != null) {
                mViewMvc.addPosts(posts.data)
            } else {
                fetchPosts()
            }
        }
//        val postModels = mViewModel.getPostModels()
//        if (postModels.isNotEmpty()) {
//            mViewMvc.bindPosts(postModels)
//        } else {
//            mViewModel.fetchNextPostPageAndNotify()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewMvc.unregisterListener()
    }

    override fun onSearchClicked() {
        mScreensNavigator.toSearch()
    }

    override fun onScrolledToBottom() {
        lifecycleScope.launchWhenStarted { fetchPosts() }
    }

    private suspend fun fetchPosts() {
        val result = mViewModel.fetchPosts()

        result.fold(
            onSuccess = { response ->
                if (response == null) return@fold

                mViewMvc.addPosts(response.data)
            },
            onFailure = { error ->
                Log.e(javaClass.simpleName, "fetchPosts: $error")
            }
        )
    }
}