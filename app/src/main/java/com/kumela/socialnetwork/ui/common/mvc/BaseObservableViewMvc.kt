package com.kumela.socialnet.ui.common.mvc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread

/**
 * Created by Toko on 08,September,2020
 **/

abstract class BaseObservableViewMvc<Listener>(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    @LayoutRes layoutId: Int
) : BaseViewMvc(inflater, parent, layoutId), ObservableViewMvc<Listener> {

    protected var listener: Listener? = null

    @MainThread
    override fun registerListener(listener: Listener) {
        this.listener = listener
    }

    @MainThread
    override fun unregisterListener() {
        this.listener = null
    }
}