package com.kumela.socialnetwork.ui.common.bottomnav

import android.widget.FrameLayout
import com.kumela.socialnetwork.ui.common.mvc.ObservableViewMvc


/**
 * Created by Toko on 24,September,2020
 **/

interface BottomNavViewMvc : ObservableViewMvc<BottomNavViewMvc.Listener> {

    interface Listener {
        fun onFabClicked()

        fun onFabMenuImageClicked()
        fun onFabMenuFileClicked()
        fun onFabMenuVideoClicked()

        fun onHomeClicked()
        fun onExploreClicked()
        fun onMessagesClicked()
        fun onProfileClicked()
    }

    fun getFragmentFrame(): FrameLayout

    fun isAnimationInProgress() : Boolean
    fun isFabMenuShowing() : Boolean

    fun openFabMenu()
    fun closeFabMenu()

    fun showBottomNav()
    fun hideBottomNav()
}