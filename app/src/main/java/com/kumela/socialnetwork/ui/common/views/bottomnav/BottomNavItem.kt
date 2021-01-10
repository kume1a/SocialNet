package com.kumela.socialnetwork.ui.common.views.bottomnav

import android.graphics.RectF
import android.graphics.drawable.Drawable

data class BottomNavItem (
    var title: String,
    val icon: Drawable,
    var rect: RectF = RectF(),
    var alpha: Int
)
