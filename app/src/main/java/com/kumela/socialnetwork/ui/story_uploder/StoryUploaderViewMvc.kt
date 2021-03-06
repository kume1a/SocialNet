package com.kumela.socialnetwork.ui.story_uploder

import android.net.Uri
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 06,November,2020
 **/

interface StoryUploaderViewMvc : ObservableViewMvc<StoryUploaderViewMvc.Listener> {
    interface Listener {
        fun onCloseClicked()
        fun onUploadClicked()
    }

    fun bindImage(imageUri: Uri)

    fun showProgressIndicationAndDisableButtons()
    fun hideProgressIndicationAndEnableButtons()
}