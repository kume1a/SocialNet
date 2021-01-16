package com.kumela.socialnetwork.ui.common.views

import androidx.annotation.IntDef
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Toko on 16,January,2021
 **/

interface SnackBarView {
    fun showSnackBar(@StringRes stringResId: Int, @SnackBarDuration duration: Int = Snackbar.LENGTH_LONG)
}

@IntDef(
    value = [
        Snackbar.LENGTH_SHORT,
        Snackbar.LENGTH_LONG,
        Snackbar.LENGTH_INDEFINITE,
    ]
)
@Retention(AnnotationRetention.SOURCE)
annotation class SnackBarDuration