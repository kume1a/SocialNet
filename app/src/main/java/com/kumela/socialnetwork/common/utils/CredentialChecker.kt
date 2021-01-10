package com.kumela.socialnetwork.common.utils


/**
 * Created by Toko on 26,September,2020
 **/

class CredentialChecker {
    companion object {
        private const val EMAIL_REGEX_STRING = "^[A-Za-z0-9._%+-]+@[A-Zaa-z0-9.-]+\\.[A-Za-z]{2,6}$"
        private const val VALID_ASCII_REGEX_STRING = "\\A\\p{ASCII}*\\z"

        private val REGEX_EMAIL = Regex(EMAIL_REGEX_STRING)
        private val REGEX_ASCII = Regex(VALID_ASCII_REGEX_STRING)
    }

    fun validName(name: String) : Boolean =
        name.length > 2 && name.matches(REGEX_ASCII)

    fun validEmail(email: String): Boolean =
        email.matches(REGEX_EMAIL) && email.matches(REGEX_ASCII)

    fun validPassword(password: String): Boolean =
        password.length > 6 && password.matches(REGEX_ASCII)
}