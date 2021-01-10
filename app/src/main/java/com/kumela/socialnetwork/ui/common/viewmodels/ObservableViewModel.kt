package com.kumela.socialnetwork.ui.common.viewmodels

import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.collections.HashSet

/**
 * Created by Toko on 23,October,2020
 **/

abstract class ObservableViewModel<Listener> : ViewModel() {
    protected val listeners = HashSet<Listener>()
    protected val uuid: UUID = UUID.randomUUID()

    fun registerListener(listener: Listener) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: Listener) {
        listeners.remove(listener)
    }
}