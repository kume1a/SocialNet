package com.kumela.socialnet.ui.register

import com.kumela.socialnet.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

interface RegisterViewMvc : ObservableViewMvc<RegisterViewMvc.Listener> {
    interface Listener {
        fun onSignInClicked()
        fun onSignUpClicked()
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