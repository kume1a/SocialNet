package com.kumela.socialnet.common

import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Toko on 26,September,2020
 **/

abstract class BaseObservable<LISTENER_CLASS> {
    // thread-safe set of listeners
    protected val listeners: MutableSet<LISTENER_CLASS> =
        Collections.newSetFromMap(ConcurrentHashMap(1))

    fun registerListener(listener: LISTENER_CLASS) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: LISTENER_CLASS) {
        listeners.remove(listener)
    }
}
