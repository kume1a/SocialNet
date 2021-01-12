package com.kumela.socialnetwork.ui.views

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.SizeF
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.ColorInt
import com.google.android.material.math.MathUtils.lerp
import com.kumela.socialnetwork.R
import kotlin.math.min

/**
 * Created by Toko on 10,January,2021
 **/

@Suppress("MemberVisibilityCanBePrivate")
class AuthBackgroundInk @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.AuthBackgroundInk
) : View(context, attrs, defStyleAttr) {

    // Dynamic Variables
    private val color1Path = Path()
    private val color2Path = Path()

    private val color1Animator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 400
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener { postInvalidate() }
    }
    private val color2Animator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 400
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener { postInvalidate() }
    }

    // Attribute Defaults
    @ColorInt
    private var _color1 = Color.parseColor(DEFAULT_COLOR1)

    @ColorInt
    private var _color2 = Color.parseColor(DEFAULT_COLOR2)

    // Core Attributes
    var color1: Int
        @ColorInt get() = _color1
        set(@ColorInt value) {
            _color1 = value
            paintInk1.color = value
            invalidate()
        }

    var color2: Int
        @ColorInt get() = _color2
        set(@ColorInt value) {
            _color2 = value
            paintInk2.color = value
            invalidate()
        }

    // Paints
    private val paintInk1 = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = color1
    }

    private val paintInk2 = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = color2
    }

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AuthBackgroundInk,
            defStyleAttr,
            0
        )

        try {
            color1 = typedArray.getColor(R.styleable.AuthBackgroundInk_color1, color1)
            color2 = typedArray.getColor(R.styleable.AuthBackgroundInk_color1, color2)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    fun forward() {
        color1Animator.start()
        color2Animator.start()
    }

    fun reverse() {
        color1Animator.reverse()
        color2Animator.reverse()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val desiredWidth = 0
        val desiredHeight = 0

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

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

        @SuppressLint("DrawAllocation")
        val size = SizeF(width.toFloat(), height.toFloat())

        calculateColor1Path(size)
        calculateColor2Path(size)

        canvas?.drawPath(color1Path, paintInk1)
        canvas?.drawPath(color2Path, paintInk2)
    }

    private fun addPointsToPath(path: Path, points: List<PointF>) {
        if (points.size < 3) {
            throw IllegalArgumentException("Need three or more points to create a path.")
        }

        for (i in 0..points.size - 2) {
            val xc = (points[i].x + points[i + 1].x) / 2
            val yc = (points[i].y + points[i + 1].y) / 2
            path.quadTo(points[i].x, points[i].y, xc, yc)
        }

        path.quadTo(
            points[points.size - 2].x,
            points[points.size - 2].y,
            points[points.size - 1].x,
            points[points.size - 1].y,
        )
    }

    private fun calculateColor1Path(size: SizeF) {
        val fraction = color1Animator.animatedFraction

        color1Path.reset()
        color1Path.moveTo(size.width, size.height / 2)
        color1Path.lineTo(size.width, 0f)
        color1Path.lineTo(0f, 0f)
        color1Path.lineTo(0f, lerp(0f, size.height, fraction))

        val points = listOf(
            PointF(
                lerp(0f, size.width / 3, fraction),
                lerp(0f, size.height, fraction),
            ),
            PointF(
                lerp(size.width / 2, size.width / 4 * 3, fraction),
                lerp(size.height / 2, size.height / 4 * 3, fraction),
            ),
            PointF(
                size.width,
                lerp(size.height / 2, size.height * 3 / 4, fraction),
            ),
        )

        addPointsToPath(color1Path, points)
    }

    private fun calculateColor2Path(size: SizeF) {
        val fraction = color2Animator.animatedFraction

        color2Path.reset()
        color2Path.moveTo(size.width, 300f)
        color2Path.lineTo(size.width, 0f)
        color2Path.lineTo(0f, 0f)
        color2Path.lineTo(0f, lerp(size.height / 4, size.height / 2, fraction))

        val points = listOf(
            PointF(
                size.width / 4,
                lerp(size.height / 2, size.height * 3 / 4, fraction),
            ),
            PointF(
                size.width * 3 / 5,
                lerp(size.height / 4, size.height / 2, fraction),
            ),
            PointF(
                size.width * 4 / 5,
                lerp(size.height / 6, size.height / 3, fraction),
            ),
            PointF(
                size.width,
                lerp(size.height / 5, size.height / 4, fraction),
            ),
        )
        addPointsToPath(color2Path, points)
    }

    companion object {
        private const val DEFAULT_COLOR1 = "#ffae48"
        private const val DEFAULT_COLOR2 = "#4c515a"
    }
}
