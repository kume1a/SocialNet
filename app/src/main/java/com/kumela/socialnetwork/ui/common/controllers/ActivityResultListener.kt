package com.kumela.socialnet.ui.common.controllers

import android.content.Intent

/**
 * Created by Toko on 27,September,2020
 **/

interface ActivityResultListener {
    fun onRequestResultReceived(requestCode: Int, resultCode: Int, data: Intent?)
}