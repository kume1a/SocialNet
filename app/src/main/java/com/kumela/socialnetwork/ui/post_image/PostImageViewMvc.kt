package com.kumela.socialnetwork.ui.post_image

import android.graphics.Bitmap
import android.net.Uri
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

interface PostImageViewMvc : ObservableViewMvc<PostImageViewMvc.Listener> {
    interface Listener {
        fun onBackPressed()
        fun onPostClicked()
    }

    fun bindProfileImage(imageUri: String)
    fun bindPostImage(imageUri: Uri)
    fun bindPostImage(imageBitmap: Bitmap)
    fun bindUsername(username: String)

    fun getHeader(): String
    fun getDescription(): String

    fun showProgressIndicatorAndDisableInteractivity()
    fun hideProgressIndicatorAndEnableInteractivity()
}