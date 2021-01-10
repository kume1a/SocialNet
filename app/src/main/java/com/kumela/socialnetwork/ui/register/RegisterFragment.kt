package com.kumela.socialnetwork.ui.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import com.kumela.socialnetwork.common.utils.CredentialChecker
import com.kumela.socialnetwork.network.authentication.RegisterUseCase
import com.kumela.socialnetwork.network.firebase.UserUseCase
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.RequestResultDispatcher
import com.kumela.socialnetwork.ui.common.controllers.ActivityResultListener
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import com.kumela.socialnetwork.ui.common.dialogs.DialogManager
import javax.inject.Inject

/**
 * Created by Toko on 24,September,2020
 **/

class RegisterFragment : BaseFragment(), RegisterViewMvc.Listener,
    ActivityResultListener,
    RegisterUseCase.Listener, RegisterViewModel.Listener {

    private lateinit var mViewMvc: RegisterViewMvc
    private lateinit var mViewModel: RegisterViewModel

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mScreensNavigator: RegisterScreensNavigator
    @Inject lateinit var mCredentialChecker: CredentialChecker
    @Inject lateinit var mRegisterUseCase: RegisterUseCase
    @Inject lateinit var mRequestResultDispatcher: RequestResultDispatcher
    @Inject lateinit var mDialogManager: DialogManager
    @Inject lateinit var mBottomNavHelper: BottomNavHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        injector.inject(this)

        mBottomNavHelper.hideBottomNav()

        mViewMvc = mViewMvcFactory.newInstance(RegisterViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        mViewMvc.registerListener(this)
        mViewModel.registerListener(this)
        mRegisterUseCase.registerListener(this)
        mRequestResultDispatcher.registerRequestResultListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mViewMvc.unregisterListener()
        mViewModel.unregisterListener(this)
        mRegisterUseCase.registerListener(this)
        mRequestResultDispatcher.unregisterRequestResultListener(this)
    }

    override fun onSignInClicked() {
        mScreensNavigator.toLogin()
    }

    override fun onSignUpClicked() {
        if (shouldShowInternetDialog()) return

        val email = mViewMvc.getEmail()
        val password = mViewMvc.getPassword()

        var anyErrorsPresent = false

        if (mCredentialChecker.validEmail(email)) {
            mViewMvc.removeEmailError()
        } else {
            mViewMvc.showEmailError()
            anyErrorsPresent = true
        }

        if (mCredentialChecker.validPassword(password)) {
            mViewMvc.removePasswordError()
        } else {
            mViewMvc.showPasswordError()
            anyErrorsPresent = true
        }

        if (!anyErrorsPresent) {
            mViewMvc.showProgressIndication()
            mRegisterUseCase.registerAndNotify(email, password)
        } else return
    }

    override fun onSignInWithGoogleClicked() {
        if (shouldShowInternetDialog()) return
        mRegisterUseCase.signInWithGoogleAndNotify()
    }

    override fun onSignInWithFacebookClicked() {
        if (shouldShowInternetDialog()) return
        mRegisterUseCase.signInWithFacebookAndNotify()
    }

    // register use case
    override fun onRegisterCompleted(firebaseUser: FirebaseUser) {
        mViewMvc.dismissProgressIndication()
        mScreensNavigator.toPersonalInfo(firebaseUser.uid)
    }

    override fun onGoogleSignInCompleted(firebaseUser: FirebaseUser) {
        mViewModel.fetchIfUserExtraInfoExistsAndNotify()
    }

    override fun onFacebookSignInCompleted(firebaseUser: FirebaseUser) {
        mViewModel.fetchIfUserExtraInfoExistsAndNotify()
    }

    override fun onRegisterFailed() {
        Toast.makeText(context, "Failed to register", Toast.LENGTH_SHORT).show()
        mViewMvc.dismissProgressIndication()
    }

    override fun onRequestResultReceived(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RC_GOOGLE_SIGN_IN -> mRegisterUseCase.onGoogleSignInResult(data)
                else -> mRegisterUseCase.onFacebookSignInResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onUserExtraInfoExistsResult(exists: Boolean) {
        mViewMvc.dismissProgressIndication()
        if (exists) {
            mScreensNavigator.toHome()
        } else {
            mScreensNavigator.toPersonalInfo(UserUseCase.uid)
        }
    }

    private fun shouldShowInternetDialog(): Boolean {
        if (!isInternetAvailable()) {
            mDialogManager.showNetworkUnavailableDialog()
            return true
        }
        return false
    }

    companion object {
        const val RC_GOOGLE_SIGN_IN = 1
    }
}