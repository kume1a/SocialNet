package com.kumela.socialnetwork.ui.common.utils

import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.bumptech.glide.Glide
import com.kumela.socialnetwork.common.Constants
import com.kumela.socialnetwork.models.User
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Toko on 03,October,2020
 **/

private val calendar = Calendar.getInstance()

fun ImageView.load(uri: String) {
    Glide.with(context)
        .load(uri)
        .centerCrop()
        .into(this)
}

fun TextView.setTimePassed(timestamp: Long, shorten: Boolean = false) {
    val now = System.currentTimeMillis()
    val ago = DateUtils.getRelativeTimeSpanString(timestamp, now, DateUtils.MINUTE_IN_MILLIS).toString()
    if (shorten) {
        this.text = ago.replace(" minutes ago", "m")
            .replace(" hours ago", "hrs")
            .replace(" days ago", "d")
            .replace("Yesterday", "1d")
        return
    }
    this.text = ago
}

fun TextView.setTime(timestamp: Long) {
    val diff: Long = System.currentTimeMillis() - timestamp

    val dateFormatString: String = when {
        diff < 86_400_000L -> "hh:mm"
        diff in 86400001..31535999999 -> "d MMM"
        else -> "d MMM, yyyy"
    }
    val sdf = SimpleDateFormat(dateFormatString, Locale.getDefault())
    sdf.timeZone = calendar.timeZone
    val time: String = sdf.format(Date(timestamp))

    this.text = time
}

inline fun View.setOnDoubleClickListener(crossinline onDoubleClickListener: () -> Unit) {
    setOnClickListener(object : DoubleClickListener() {
        override fun onDoubleClick(v: View) {
            onDoubleClickListener.invoke()
        }
    })
}

fun NavController.navigateSafely(navDirections: NavDirections) {
    try {
        navigate(navDirections)
    } catch (e: IllegalArgumentException) {
        Log.w(javaClass.simpleName, "navigateSafely: ", e)
    }
}

fun getUniqueId(id1: String, id2: String): String {
    return if (id1 > id2) {
        "${id1}_${id2}"
    } else {
        "${id2}_${id1}"
    }
}

fun User.isOnline(): Boolean {
    return false
//    return System.currentTimeMillis() - lastOnline <= Constants.INTERVAL_ONLINE_UPDATE
}

fun Long.isOutdated() : Boolean {
    calendar.time = Date(this)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    calendar.time = Date(System.currentTimeMillis())
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    return day != currentDay
}