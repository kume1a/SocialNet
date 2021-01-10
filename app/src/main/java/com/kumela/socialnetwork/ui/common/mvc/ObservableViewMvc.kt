package com.kumela.socialnetwork.ui.common.mvc

/**
 * Created by Toko on 08,September,2020
 **/

interface ObservableViewMvc<Listener> : ViewMvc {
    fun registerListener(listener: Listener)
    fun unregisterListener()
}