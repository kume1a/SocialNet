package com.kumela.socialnet.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import com.kumela.socialnet.common.utils.CredentialChecker
import com.kumela.socialnet.network.authentication.LoginUseCase
import com.kumela.socialnet.network.firebase.UserUseCase
import com.kumela.socialnet.ui.common.ViewMvcFactory
import com.kumela.socialnet.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnet.ui.common.controllers.RequestResultDispatcher
import com.kumela.socialnet.ui.common.controllers.ActivityResultListener
import com.kumela.socialnet.ui.common.controllers.BaseFragment
import com.kumela.socialnet.ui.common.dialogs.DialogManager
import javax.inject.Inject

/**
 * Created by Toko on 10,September,2020
 **/

class LoginFragment : BaseFragment(), LoginViewMvc.Listener,
    ActivityResultListener,
    LoginUseCase.Listener, LoginViewModel.Listener {

    private lateinit var mViewMvc: LoginViewMvc
    private lateinit var mViewModel: LoginViewModel

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mScreensNavigator: LoginScreensNavigator
    @Inject lateinit var mLoginUseCase: LoginUseCase
    @Inject lateinit var mCredentialChecker: CredentialChecker
    @Inject lateinit var mRequestResultDispatcher: RequestResultDispatcher
    @Inject lateinit var mDialogManager: DialogManager
    @Inject lateinit var mBottomNavHelper: BottomNavHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        injector.inject(this)

        mBottomNavHelper.hideBottomNav()

        mViewMvc = mViewMvcFactory.newInstance(LoginViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        mViewMvc.registerListener(this)
        mViewModel.registerListener(this)
        mLoginUseCase.registerListener(this)
        mRequestResultDispatcher.registerRequestResultListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mViewMvc.unregisterListener()
        mViewModel.unregisterListener(this)
        mLoginUseCase.registerListener(this)
        mRequestResultDispatcher.unregisterRequestResultListener(this)
    }

    override fun onSignUpClicked() {
        mScreensNavigator.toRegister()
    }

    override fun onForgotPasswordClicked() {
        Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show()
    }

    /**
     * 1. gets email and password from mvc view
     * 2. checks if input is valid if not notifies user
     * 3. if no errors are present: attempts to login with instance of [LoginUseCase]: [mLoginUseCase]
     */
    override fun onSignInClicked() {
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
            mLoginUseCase.loginAndNotify(email, password)
        } else return
    }

    override fun onSignInWithGoogleClicked() {
        if (shouldShowInternetDialog()) return
        mLoginUseCase.signInWithGoogleAndNotify()
    }

    override fun onSignInWithFacebookClicked() {
        if (shouldShowInternetDialog()) return
        mLoginUseCase.signInWithFacebookAndNotify()
    }

    // login use case
    override fun onSignInCompleted(firebaseUser: FirebaseUser) {
        mViewModel.fetchIfUserExtraInfoExistsAndNotify()
    }

    override fun onGoogleSignInCompleted(firebaseUser: FirebaseUser) {
        mViewModel.fetchIfUserExtraInfoExistsAndNotify()
    }

    override fun onFacebookSignInCompleted(firebaseUser: FirebaseUser) {
        mViewModel.fetchIfUserExtraInfoExistsAndNotify()
    }

    override fun onSignInFailed() {
        mViewMvc.dismissProgressIndication()
        mDialogManager.showInfoDialog("Error occurred", "Failed to Sign in. Please try again later")

        Toast.makeText(context, "Failed to sign in", Toast.LENGTH_LONG).show()
    }

    override fun onRequestResultReceived(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RC_GOOGLE_SIGN_IN -> mLoginUseCase.onGoogleSignInResult(data)
                else -> mLoginUseCase.onFacebookSignInResult(requestCode, resultCode, data)
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
        private const val RC_GOOGLE_SIGN_IN = 1
    }
}