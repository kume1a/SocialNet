package com.kumela.socialnetwork.ui.common.controllers

/**
 * Created by Toko on 06,November,2020
 **/

interface IntentDispatcher {
    fun dispatchImagePickerIntent(requestCode: Int)
    fun dispatchTakeImageIntent()
}