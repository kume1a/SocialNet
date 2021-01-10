package com.kumela.socialnetwork.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kumela.socialnetwork.models.firebase.UserModel
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import javax.inject.Inject

/**
 * Created by Toko on 24,September,2020
 **/

class SearchFragment : BaseFragment(), SearchViewMvc.Listener,
    SearchViewModel.Listener {

    private lateinit var mViewMvc: SearchViewMvc
    private lateinit var mViewModel: SearchViewModel

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        mViewMvc.registerListener(this)
        mViewModel.registerListener(this)

        mViewModel.users?.let { mViewMvc.bindSearchItems(it) }
    }

    override fun onStop() {
        super.onStop()
        mViewMvc.unregisterListener()
        mViewModel.unregisterListener(this)
    }

    override fun onBackPressed() {
        mScreensNavigator.navigateUp()
    }

    override fun onQueryTextChanged(query: String) {
        if (query.isBlank()) {
            mViewMvc.bindSearchItems(emptyList())
            return
        }

        mViewModel.fetchUsersByNameAndNotify(query)
    }

    override fun onSearchItemClicked(user: UserModel) {
        mScreensNavigator.toUserProfile(user.id, user.imageUri, user.username)
    }

    override fun onUsersFetched(users: List<UserModel>) {
        Log.d(javaClass.simpleName, "onUsersFetched() called with: users = $users")
        mViewMvc.bindSearchItems(users)
    }
}