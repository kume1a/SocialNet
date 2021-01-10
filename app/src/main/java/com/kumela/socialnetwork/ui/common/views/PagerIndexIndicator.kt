@file:Suppress("MemberVisibilityCanBePrivate")

package com.kumela.socialnetwork.ui.common.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import com.kumela.socialnetwork.R

/**
 * Created by Toko on 06,November,2020
 **/

class PagerIndexIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.PagerIndexIndicator
) : View(context, attrs, defStyleAttr) {

    private var rectF: RectF = RectF()
    private var itemWidth: Float = -1f

    private var itemCount: Int = -1

    fun setItemCount(count: Int, initialIndex: Int) {
        if (initialIndex < 0 || initialIndex > count - 1) {
            throw IllegalArgumentException("initial index must be between 0 and count ($count)")
        }

        itemCount = count
        activeIndex = initialIndex

        recalculateRect(width, height)
        invalidate()
    }

    // Attribute Defaults
    @ColorInt
    private var _activeColor = ContextCompat.getColor(context, R.color.white)

    @Dimension
    private var _horizontalMargin =
        context.resources.getDimension(R.dimen.pager_index_indicator_default_horizontal_margin)

    private var _animDuration = 200L

    var activeColor: Int
        @ColorInt get() = _activeColor
        set(@ColorInt value) {
            _activeColor = value
            paint.color = value
            invalidate()
        }

    var horizontalMargin: Float
        @Dimension get() = _horizontalMargin
        set(@Dimension value) {
            _horizontalMargin = value
            invalidate()
        }

    var animDuration: Long
        get() = _animDuration
        set(value) {
            _animDuration = value
        }

    private var activeIndex: Int = 0

    // Paints
    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = activeColor
    }

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BottomNav,
            defStyleAttr,
            0
        )

        try {
            activeColor = typedArray.getColor(
                R.styleable.PagerIndexIndicator_activeColor,
                activeColor
            )

            horizontalMargin = typedArray.getDimension(
                R.styleable.PagerIndexIndicator_horizontalMargin,
                horizontalMargin
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (itemCount != -1) {
            recalculateRect(w, h)
        }
    }

    private fun recalculateRect(w: Int, h: Int) {
        rectF.top = 0F
        rectF.bottom = h.toFloat()

        rectF.left = w / itemCount * activeIndex + horizontalMargin
        rectF.right = w / itemCount * (activeIndex + 1) - horizontalMargin

        itemWidth = w / itemCount - 2 * horizontalMargin

        Log.d(javaClass.simpleName, "recalculateRect: rect = ${rectF.toShortString()}")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (itemCount <= 1) return

        canvas?.drawRoundRect(rectF, 25f, 25f, paint)
    }

    /**
     * applies next index with animation
     *
     * @return true if it was successful action false otherwise
     */
    fun next(): Boolean {
        if (itemCount == -1) throw IllegalStateException("itemCount must be initialized")
        if (activeIndex == itemCount - 1) return false

        activeIndex++
        applyActiveIndex(true)
        return true
    }

    /**
     * applies next index with animation
     *
     * @return true if it was successful action false otherwise
     */
    fun previous(): Boolean {
        if (itemCount == -1) throw IllegalStateException("itemCount must be initialized")
        if (activeIndex == 0) return false

        activeIndex--
        applyActiveIndex(false)
        return true
    }

    private fun applyActiveIndex(next: Boolean) {
        ValueAnimator.ofFloat(
            rectF.left,
            if (next) rectF.right + 2 * horizontalMargin
            else rectF.left - 2 * horizontalMargin - itemWidth
        ).apply {
            duration = animDuration
            addUpdateListener {
                val value = it.animatedValue as Float

                rectF.left = value
                rectF.right = value + itemWidth
                invalidate()
            }
            start()
        }
    }
}
