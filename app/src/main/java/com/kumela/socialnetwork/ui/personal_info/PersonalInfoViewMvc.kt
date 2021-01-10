package com.kumela.socialnetwork.ui.personal_info

import android.net.Uri
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

interface PersonalInfoViewMvc : ObservableViewMvc<PersonalInfoViewMvc.Listener> {
    interface Listener {
        fun onFinishClicked()
        fun onProfileImageClicked()
    }

    fun showProgressIndication()
    fun hideProgressIndication()

    fun showUsernameError()

    fun getUsername(): String
    fun loadProfileImage(imageUri: Uri)
}