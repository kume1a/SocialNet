package com.kumela.socialnet.ui.common.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

/**
 * Created by Toko on 10,November,2020
 **/

class OnRecyclerSnapListener(
    private val snapHelper: SnapHelper,
    var onSnapPositionChangeListener: (Int) -> Unit
) : RecyclerView.OnScrollListener() {

    private var snapPosition = RecyclerView.NO_POSITION

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val snapPosition = getSnapPosition(recyclerView)
        if (this.snapPosition != snapPosition) {
            onSnapPositionChangeListener.invoke(snapPosition)
            this.snapPosition = snapPosition
        }
    }

    private fun getSnapPosition(recyclerView: RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
        val snapView = snapHelper.findSnapView(layoutManager) ?: return RecyclerView.NOT_FOCUSABLE
        return layoutManager.getPosition(snapView)
    }
}