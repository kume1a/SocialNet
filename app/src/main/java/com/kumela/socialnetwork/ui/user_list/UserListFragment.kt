package com.kumela.socialnet.ui.user_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kumela.socialnet.models.firebase.UserModel
import com.kumela.socialnet.network.firebase.UserUseCase
import com.kumela.socialnet.ui.common.ViewMvcFactory
import com.kumela.socialnet.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnet.ui.common.controllers.BaseFragment
import com.kumela.socialnet.ui.common.viewmodels.ViewModelFactory
import javax.inject.Inject

/**
 * Created by Toko on 05,November,2020
 **/

class UserListFragment : BaseFragment(), UserListViewMvc.Listener,
    UserListViewModel.Listener {

    private lateinit var mViewMvc: UserListViewMvc
    private lateinit var mViewModel: UserListViewModel

    private lateinit var argDataType: DataType
    private lateinit var argKey: String

    @Inject lateinit var mBottomNavHelper: BottomNavHelper
    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mViewModelFactory: ViewModelFactory
    @Inject lateinit var mScreensNavigator: UserListScreensNavigator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        injector.inject(this)

        mBottomNavHelper.hideBottomNav()

        mViewMvc = mViewMvcFactory.newInstance(UserListViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = UserListFragmentArgs.fromBundle(requireArguments())

        argDataType = args.dataType
        argKey = args.key

        val header = when (argDataType) {
            DataType.FOLLOWERS -> "Followers"
            DataType.FOLLOWING -> "Following"
            DataType.LIKES -> "Liked by"
        }
        mViewMvc.bindToolbarHeader(header)

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(UserListViewModel::class.java)

        mViewMvc.registerListener(this)
        mViewModel.registerListener(this)

        val userModels = mViewModel.getUserModels()
        if (userModels.isNotEmpty()) {
            mViewMvc.bindUsers(userModels)
        } else {
            fetchNextDataPage()
        }
    }

    private fun fetchNextDataPage() {
        when (argDataType) {
            DataType.FOLLOWERS -> mViewModel.fetchFollowersNextPageAndNotify(argKey)
            DataType.FOLLOWING -> mViewModel.fetchFollowingNextPageAndNotify(argKey)
            DataType.LIKES -> mViewModel.fetchPostLikersAndNotify(argKey)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mViewMvc.unregisterListener()
        mViewModel.unregisterListener(this)
    }

    override fun onNavigateUpClicked() {
        mScreensNavigator.navigateUp()
    }

    override fun onUserClicked(userModel: UserModel) {
        if (userModel.id != UserUseCase.uid) {
            mScreensNavigator.toUserProfile(userModel.id, userModel.imageUri, userModel.username)
        }
    }

    override fun onLastUserBound() {
        fetchNextDataPage()
    }

    // view model callbacks
    override fun onUserFetched(userModel: UserModel) {
        mViewMvc.addUser(userModel)
    }
}