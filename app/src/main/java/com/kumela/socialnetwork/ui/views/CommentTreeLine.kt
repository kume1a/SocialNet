package com.kumela.socialnetwork.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.min
import kotlin.math.roundToInt


@Suppress("MemberVisibilityCanBePrivate")
class CommentTreeLine @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1
) : View(context, attrs, defStyleAttr) {

    // Core Attributes
    var paintBottomLine: Boolean = true

    // Paints
    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 2f.toPx()
    }

    // Paths
    private val path = Path()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val desiredWidth: Int = widthSize
        val desiredHeight: Int = heightSize

        val width: Int
        val height: Int

        //Measure Width
        width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(desiredWidth, widthSize)
            else -> desiredWidth
        }

        //Measure Height
        height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(desiredHeight, heightSize)
            else -> desiredHeight
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        path.reset()
        path.moveTo(width.toFloat() / 2, 0f)
        path.cubicTo(
            width.toFloat() / 2, 0f,
            width.toFloat() / 2, height.toFloat() * .6f,
            width.toFloat() - 8f.toPx(), (height.toFloat()) / 2
        )
        path.moveTo(width.toFloat() / 2, 0f)
        if (paintBottomLine) {
            path.lineTo(width.toFloat() / 2, height.toFloat())
        }
        canvas?.drawPath(path, paint)
    }

    private fun Float.toPx(): Float {
        return (this * resources.displayMetrics.density).roundToInt().toFloat()
    }
}
