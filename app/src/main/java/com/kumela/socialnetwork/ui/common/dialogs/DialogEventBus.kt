package com.kumela.socialnetwork.ui.common.dialogs

import com.kumela.socialnetwork.common.BaseObservable

/**
 * Created by Toko on 14,October,2020
 **/

class DialogEventBus : BaseObservable<DialogEventBus.Listener>() {

    interface Listener {
        fun onDialogEvent(event: Any)
    }

    fun postEvent(event: Any) {
        for (listener in listeners) {
            listener.onDialogEvent(event)
        }
    }
}