package com.kumela.socialnetwork.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.kumela.socialnetwork.models.User
import com.kumela.socialnetwork.network.common.fold
import com.kumela.socialnetwork.network.repositories.SearchRepository
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Toko on 24,September,2020
 **/

class SearchFragment : BaseFragment(), SearchViewMvc.Listener {

    private lateinit var mViewMvc: SearchViewMvc

    @Inject lateinit var mSearchRepository: SearchRepository

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
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
            val result = mSearchRepository.searchUsers(query)
            
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