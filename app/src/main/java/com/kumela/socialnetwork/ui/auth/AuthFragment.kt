package com.kumela.socialnetwork.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kumela.socialnetwork.common.Constants
import com.kumela.socialnetwork.common.utils.CredentialChecker
import com.kumela.socialnetwork.models.firebase.UserModel
import com.kumela.socialnetwork.network.authentication.AuthUseCase
import com.kumela.socialnetwork.network.firebase.fold
import com.kumela.socialnetwork.ui.common.ViewMvcFactory
import com.kumela.socialnetwork.ui.common.bottomnav.BottomNavHelper
import com.kumela.socialnetwork.ui.common.controllers.BaseFragment
import com.kumela.socialnetwork.ui.common.dialogs.DialogManager
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Toko on 10,September,2020
 **/

class AuthFragment : BaseFragment(), AuthViewMvc.Listener,
    AuthViewModel.Listener {

    private lateinit var mViewMvc: AuthViewMvc
    private lateinit var mViewModel: AuthViewModel

    @Inject lateinit var mViewMvcFactory: ViewMvcFactory
    @Inject lateinit var mScreensNavigator: AuthScreensNavigator
    @Inject lateinit var mAuthUseCase: AuthUseCase
    @Inject lateinit var mCredentialChecker: CredentialChecker
    @Inject lateinit var mDialogManager: DialogManager
    @Inject lateinit var mBottomNavHelper: BottomNavHelper

    private enum class AuthPage { SIGN_IN, SIGN_UP }

    private var authPage = AuthPage.SIGN_IN
        set(value) {
            onAuthPageChanged(field, value)
            field = value
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        injector.inject(this)

        mBottomNavHelper.hideBottomNav()

        mViewMvc = mViewMvcFactory.newInstance(AuthViewMvc::class, container)
        return mViewMvc.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authPage =
            savedInstanceState?.getSerializable(KEY_AUTH_PAGE) as? AuthPage ?: AuthPage.SIGN_IN

        mViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        mViewMvc.registerListener(this)
        mViewModel.registerListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mViewMvc.unregisterListener()
        mViewModel.unregisterListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_AUTH_PAGE, authPage)
    }

    override fun onGoToSignUpClicked() {
        authPage = AuthPage.SIGN_UP
    }

    override fun onForgotPasswordClicked() {
        Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show()
    }

    override fun onAuthClicked() {
        when (authPage) {
            AuthPage.SIGN_IN -> {
                if (shouldShowInternetDialog()) return

                val email = mViewMvc.getSigninEmail()
                val password = mViewMvc.getSigninPassword()

                var anyErrorsPresent = false

                if (!mCredentialChecker.validEmail(email)) {
                    mViewMvc.showSigninEmailError()
                    anyErrorsPresent = true
                } else mViewMvc.removeSigninEmailError()

                if (!mCredentialChecker.validPassword(password)) {
                    mViewMvc.showSigninPasswordError()
                    anyErrorsPresent = true
                } else mViewMvc.removeSigninPasswordError()

                if (!anyErrorsPresent) {
                    mViewMvc.showProgressIndication()
                    signin(email, password)
                } else return
            }
            AuthPage.SIGN_UP -> {
                if (shouldShowInternetDialog()) return

                val name = mViewMvc.getSignupName()
                val email = mViewMvc.getSignupEmail()
                val password = mViewMvc.getSignupPassword()

                var anyErrorsPresent = false

                if (!mCredentialChecker.validName(name)) {
                    mViewMvc.showSignupNameError()
                    anyErrorsPresent = true
                } else mViewMvc.removeSignupNameError()

                if (!mCredentialChecker.validEmail(email)) {
                    mViewMvc.showSignupEmailError()
                    anyErrorsPresent = true
                } else mViewMvc.removeSignupEmailError()

                if (!mCredentialChecker.validPassword(password)) {
                    mViewMvc.showSignupPasswordError()
                    anyErrorsPresent = true
                } else mViewMvc.removeSignupPasswordError()

                if (!anyErrorsPresent) {
                    mViewMvc.showProgressIndication()
                    signup(name, email, password)
                } else return
            }
        }
    }

    private fun signin(email: String, password: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            val result = mAuthUseCase.signin(email, password)
            result.fold(
                onSuccess = { userId ->
                    mViewMvc.dismissProgressIndication()
                    mScreensNavigator.toHome()
                },
                onFailure = { exception ->
                    Log.e(javaClass.simpleName, "signin: ", exception)
                    mViewMvc.dismissProgressIndication()
                    mDialogManager.showInfoDialog("Error occurred", "Failed to sign in")
                }
            )

        }
    }

    private fun signup(name: String, email: String, password: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            val result = mAuthUseCase.signup(name, email, password)
            result.fold(
                onSuccess = { userId ->
                    mViewMvc.dismissProgressIndication()
                    val user = UserModel(
                        userId,
                        mViewMvc.getSignupName(),
                        Constants.DEFAULT_IMAGE_URI,
                        System.currentTimeMillis()
                    )
                    mViewModel.createUserAndNotify(user)
                },
                onFailure = { exception ->
                    Log.e(javaClass.simpleName, "signup: ", exception)
                    mViewMvc.dismissProgressIndication()
                    mDialogManager.showInfoDialog("Error occurred", "Failed to sign up")
                }
            )
        }
    }

    override fun onGoToSignInClicked() {
        authPage = AuthPage.SIGN_IN
    }

    override fun onUserCreated() {
        mViewMvc.dismissProgressIndication()
        mScreensNavigator.toHome()
    }

    override fun onUserCreateFailed(e: Exception) {
        mViewMvc.dismissProgressIndication()
        mDialogManager.showInfoDialog("Error occurred", "Failed to connect to server")
    }

    private fun onAuthPageChanged(prev: AuthPage, curr: AuthPage) {
        if (prev != curr) {
            when (curr) {
                AuthPage.SIGN_IN -> {
                    mViewMvc.reverseBackgroundAnimation()
                    mViewMvc.showLoginGroup()
                    mViewMvc.hideSignUpGroup()
                }
                AuthPage.SIGN_UP -> {
                    mViewMvc.forwardBackgroundAnimation()
                    mViewMvc.hideLoginGroup()
                    mViewMvc.showSignUpGroup()
                }
            }
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
        private const val KEY_AUTH_PAGE = "KEY_AUTH_PAGE"
    }
}