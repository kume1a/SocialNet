package com.kumela.socialnetwork.ui.common.dialogs.alertdialog

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kumela.socialnetwork.ui.common.dialogs.BaseDialog
import com.kumela.socialnetwork.ui.common.dialogs.DialogEventBus
import javax.inject.Inject

/**
 * Created by Toko on 14,October,2020
 **/

class AlertDialog : BaseDialog() {

    enum class Button {
        POSITIVE, NEGATIVE
    }

    @Inject
    lateinit var dialogEventBus: DialogEventBus

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        injector.inject(this)

        checkNotNull(arguments) { "arguments mustn't be null" }

        val args = requireArguments()

        val title = args.getString(ARG_TITLE)
        val message = args.getString(ARG_MESSAGE)
        val positiveButtonCaption = args.getString(ARG_POSITIVE_BUTTON_CAPTION)
        val negativeButtonCaption = args.getString(ARG_NEGATIVE_BUTTON_CAPTION)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)

        if (positiveButtonCaption != null) {
            dialog.setPositiveButton(positiveButtonCaption) { instance, _ ->
                dialogEventBus.postEvent(Button.POSITIVE)
                instance.dismiss()
            }
        }

        if (negativeButtonCaption != null) {
            dialog.setNegativeButton(negativeButtonCaption) { instance, _ ->
                dialogEventBus.postEvent(Button.NEGATIVE)
                instance.dismiss()
            }
        }

        return dialog.create()
    }

    companion object {
        private const val ARG_TITLE = "ARG_TITLE"
        private const val ARG_MESSAGE = "ARG_MESSAGE"
        private const val ARG_POSITIVE_BUTTON_CAPTION = "ARG_POSITIVE_BUTTON_CAPTION"
        private const val ARG_NEGATIVE_BUTTON_CAPTION = "ARG_NEGATIVE_BUTTON_CAPTION"

        @JvmStatic
        fun newInstance(
            title: String?,
            message: String,
            negativeButtonCaption: String?,
            positiveButtonCaption: String?
        ): AlertDialog {
            val alertDialog = AlertDialog()

            val args = Bundle(3)
            args.putString(ARG_TITLE, title)
            args.putString(ARG_MESSAGE, message)
            args.putString(ARG_NEGATIVE_BUTTON_CAPTION, negativeButtonCaption)
            args.putString(ARG_POSITIVE_BUTTON_CAPTION, positiveButtonCaption)

            alertDialog.arguments = args

            return alertDialog
        }
    }
}