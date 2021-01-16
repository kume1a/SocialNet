package com.kumela.socialnetwork.ui.common.utils

import androidx.annotation.StringRes
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.network.NetworkError

/**
 * Created by Toko on 16,January,2021
 **/

@StringRes
fun getAuthError(networkError: NetworkError): Int {
    return when (networkError) {
        is NetworkError.HttpError -> {
            when (networkError.statusCode) {
                401 -> R.string.password_or_email_invalid_error
                409 -> R.string.user_conflict_error
                422 -> R.string.invalid_value_error
                else -> R.string.generic_error
            }
        }
        NetworkError.Error -> R.string.connection_timed_out
    }
}