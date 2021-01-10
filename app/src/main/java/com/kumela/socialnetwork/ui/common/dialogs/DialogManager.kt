package com.kumela.socialnetwork.ui.common.dialogs

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.kumela.socialnetwork.ui.common.dialogs.alertdialog.AlertDialog


/**
 * Created by Toko on 14,October,2020
 **/

class DialogManager(
    private val context: Context,
    private val fragmentManager: FragmentManager
) {

    fun showNetworkUnavailableDialog() {
        showInfoDialog(
            "Internet unavailable",
            "Please turn on connection to the internet"
        )
    }

    fun showInfoDialog(title: String, message: String) {
        val dialog = AlertDialog.newInstance(title, message, null, null)
        dialog.show(fragmentManager, null)
    }

    fun showAlertDialog(
        title: String?,
        message: String,
        positiveButtonCaption: String?,
        negativeButtonCaption: String?
    ) {
        val dialog =
            AlertDialog.newInstance(title, message, negativeButtonCaption, positiveButtonCaption)
        dialog.show(fragmentManager, null)
    }

    private fun getString(stringId: Int): String {
        return context.getString(stringId)
    }

    fun getShownDialogTag(): String? {
        for (fragment in fragmentManager.fragments) {
            if (fragment is BaseDialog) {
                return fragment.getTag()
            }
        }
        return null
    }
}