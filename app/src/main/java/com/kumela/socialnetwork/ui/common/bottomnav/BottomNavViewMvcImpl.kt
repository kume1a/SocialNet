package com.kumela.socialnetwork.ui.common.bottomnav

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.kumela.socialnetwork.ui.views.bottomnav.BottomNav
import com.kumela.socialnetwork.ui.views.popupmenu.PopupMenu
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.ui.common.mvc.BaseObservableViewMvc

/**
 * Created by Toko on 24,September,2020
 **/

class BottomNavViewMvcImpl(
    inflater: LayoutInflater, parent: ViewGroup?
) : BaseObservableViewMvc<BottomNavViewMvc.Listener>(
    inflater, parent, R.layout.layout_bottom_nav
), BottomNavViewMvc, PopupMenu.Listener {

    private val bottomNavigationView: BottomNav = findViewById(R.id.bottom_nav)
    private val frameContent: FrameLayout = findViewById(R.id.frame_content)
    private val fabShape: ImageView = findViewById(R.id.fab_shape)
    private val fabIcon: ImageView = findViewById(R.id.fab_add)
    private val popupMenu: PopupMenu = findViewById(R.id.popup_menu)

    private var menuShowing = false

    init {
        bottomNavigationView.setOnItemSelectedListener { index ->
            when (index) {
                0 -> listener?.onHomeClicked()
                1 -> listener?.onExploreClicked()
                2 -> listener?.onMessagesClicked()
                3 -> listener?.onProfileClicked()
            }
        }

        popupMenu.addListener(this)
        fabShape.setOnClickListener { listener?.onFabClicked() }
    }

    override fun isAnimationInProgress(): Boolean {
        return popupMenu.animationInProgress
    }

    override fun openFabMenu() {
        animateFab()
        popupMenu.open()
        menuShowing = true
    }

    override fun closeFabMenu() {
        popupMenu.close()
        menuShowing = false
    }

    override fun getFragmentFrame(): FrameLayout {
        return frameContent
    }

    override fun showBottomNav() {
        if (!isBottomNavShowing()) {
            bottomNavigationView.visibility = View.VISIBLE
            popupMenu.visibility = View.VISIBLE
            fabShape.visibility = View.VISIBLE
            fabIcon.visibility = View.VISIBLE
            animateScale(fabShape, show = true)
            animateScale(fabIcon, show = true)
        }
    }

    override fun hideBottomNav() {
        if (isBottomNavShowing()) {
            bottomNavigationView.visibility = View.GONE
            popupMenu.visibility = View.GONE
            if (menuShowing) popupMenu.close()

            animateScale(fabShape, show = false)
            animateScale(fabIcon, show = false)
        }
    }

    // popup menu listener
    override fun onImageClicked() {
        listener?.onFabMenuImageClicked()
    }

    override fun onFileClicked() {
        listener?.onFabMenuFileClicked()
    }

    override fun onVideoClicked() {
        listener?.onFabMenuVideoClicked()
    }

    override fun isFabMenuShowing(): Boolean {
        return menuShowing
    }

    override fun onReductionCompleted() {
        animateFab()
    }

    private fun animateFab() {
        val shapeRotation = 360F
        val iconRotation = 135F

        ValueAnimator.ofFloat(0F, 1F).apply {
            duration = 600
            addUpdateListener {
                // animate fab shape
                val value = it.animatedValue as Float

                val scale = if (value <= .5F) {
                    -.4F * value + 1
                } else .4F * value + .6F

                fabShape.scaleX = scale
                fabShape.scaleY = scale

                if (menuShowing) {
                    fabShape.rotation = shapeRotation * value
                    fabIcon.rotation = iconRotation * value
                } else {
                    fabShape.rotation = shapeRotation * (1 - value)
                    fabIcon.rotation = iconRotation * (1 - value)
                }
            }
            start()
        }
    }

    private fun isBottomNavShowing(): Boolean {
        return bottomNavigationView.visibility == View.VISIBLE
    }

    private fun animateScale(v: View, show: Boolean) {
        val scale: Float = if (show) 1F else 0F
        val animator = v.animate().scaleX(scale).scaleY(scale)

        if (show) {
            v.visibility = View.VISIBLE
        } else {
            animator.withEndAction {
                v.visibility = View.GONE
            }
        }

        animator.start()
    }
}