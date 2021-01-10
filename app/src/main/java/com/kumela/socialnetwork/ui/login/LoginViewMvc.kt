package com.kumela.socialnetwork.ui.login

import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 10,September,2020
 **/

interface LoginViewMvc : ObservableViewMvc<LoginViewMvc.Listener> {
    interface Listener {
        fun onSignUpClicked()
        fun onForgotPasswordClicked()
        fun onSignInClicked()
        fun onSignInWithGoogleClicked()
        fun onSignInWithFacebookClicked()
    }

    fun showProgressIndication()
    fun dismissProgressIndication()

    fun getEmail() : String
    fun getPassword(): String

    fun showEmailError()
    fun showPasswordError()

    fun removeEmailError()
    fun removePasswordError()
}