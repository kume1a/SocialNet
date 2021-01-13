package com.kumela.socialnetwork.ui.auth

import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 10,September,2020
 **/

interface AuthViewMvc : ObservableViewMvc<AuthViewMvc.Listener> {
    interface Listener {
        fun onAuthClicked()

        fun onGoToSignUpClicked()
        fun onForgotPasswordClicked()

        fun onGoToSignInClicked()
    }

    fun showProgressIndication()
    fun dismissProgressIndication()

    fun getSigninEmail(): String
    fun getSigninPassword(): String
    fun showSigninEmailError()
    fun showSigninPasswordError()
    fun removeSigninEmailError()
    fun removeSigninPasswordError()

    fun getSignupName(): String
    fun getSignupEmail(): String
    fun getSignupPassword(): String
    fun showSignupNameError()
    fun showSignupEmailError()
    fun showSignupPasswordError()
    fun removeSignupNameError()
    fun removeSignupEmailError()
    fun removeSignupPasswordError()

    fun forwardBackgroundAnimation()
    fun reverseBackgroundAnimation()
    fun showLoginGroup()
    fun hideLoginGroup()
    fun showSignUpGroup()
    fun hideSignUpGroup()
}