package com.kumela.socialnetwork.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.network.common.fold
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import com.kumela.socialnetwork.ui.common.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Toko on 24,September,2020
 **/

class SearchFragment : BaseFragment(), SearchViewMvc.Listener {

    private lateinit var mViewMvc: SearchViewMvc
    private lateinit var mViewModel: SearchViewModel

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mViewModelFactory: ViewModelFactory
    @Inject lateinit var mBottomNavHelper: BottomNavHelper
    @Inject lateinit var mScreensNavigator: SearchScreensNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        injector.inject(this)

        mBottomNavHelper.hideBottomNav()

        mViewMvc = mViewMvcFactory.newInstance(SearchViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(SearchViewModel::class.java)
        val users = mViewModel.getCachedUsers()
        if (users != null) {
            mViewMvc.bindSearchItems(users)
        }
    }

    override fun onStart() {
        super.onStart()
        mViewMvc.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        mViewMvc.unregisterListener()
    }

    override fun onBackPressed() {
        mScreensNavigator.navigateUp()
    }

    override fun onQueryTextChanged(query: String) {
        if (query.isBlank()) {
            mViewMvc.bindSearchItems(emptyList())
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val result = mViewModel.searchUsers(query)

            result.fold(
                onSuccess = { users ->
                    mViewMvc.bindSearchItems(users)
                },
                onFailure = { error ->
                    Log.e(javaClass.simpleName, "onQueryTextChanged: $error")
                }
            )
        }
    }

    override fun onSearchItemClicked(user: User) {
        mScreensNavigator.toUserProfile(user.id, user.imageUrl, user.name, user.bio)
    }
}