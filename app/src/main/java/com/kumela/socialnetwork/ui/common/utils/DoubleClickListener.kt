package com.kumela.socialnet.ui.common.utils

import android.view.View

/**
 * Created by Toko on 24,October,2020
 **/

abstract class DoubleClickListener : View.OnClickListener {
    var lastClickTime: Long = 0

    override fun onClick(v: View) {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            onDoubleClick(v)
        }
        lastClickTime = clickTime
    }

    abstract fun onDoubleClick(v: View)

    companion object {
        private const val DOUBLE_CLICK_TIME_DELTA: Long = 300
    }
}