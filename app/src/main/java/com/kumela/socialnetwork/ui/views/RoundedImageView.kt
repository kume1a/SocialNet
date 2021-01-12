@file:Suppress("MemberVisibilityCanBePrivate")

package com.kumela.socialnetwork.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.appcompat.widget.AppCompatImageView
import com.kumela.socialnetwork.R
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Created by Toko on 02,October,2020
 **/

class RoundedImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = R.attr.RoundedImageView
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private lateinit var rectF: RectF
    private val path = Path()

    private var rectFStroke: RectF? = null
    private var rectFImageClip: RectF? = null
    private val pathImageClip: Path = Path()

    @Dimension
    private var _cornerRadius = DEFAULT_CORNER_RADIUS.toPx()

    @Dimension
    private var _strokeMargin = 0f

    @Dimension
    private var _imageStrokeWidth = -1f

    @ColorInt
    private var _imageStrokeColor = -1


    // Core Attributes
    var cornerRadius: Float
        @Dimension get() = _cornerRadius
        set(@Dimension value) {
            _cornerRadius = value
            invalidate()
        }

    var strokeMargin: Float
        @Dimension get() = _strokeMargin
        set(@Dimension value) {
            _strokeMargin = value
            invalidate()
        }

    var imageStrokeColor: Int
        @ColorInt get() = _imageStrokeColor
        set(@ColorInt value) {
            _imageStrokeColor = value
            paintStroke.color = value
            invalidate()
        }

    var imageStrokeWidth: Float
        @Dimension get() = _imageStrokeWidth
        set(@Dimension value) {
            _imageStrokeWidth = value
            paintStroke.strokeWidth = value
            invalidate()
        }

    // Paints
    private val paintStroke = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = imageStrokeWidth
        color = imageStrokeColor
    }

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.RoundedImageView,
            defStyleAttr,
            0
        )

        try {
            cornerRadius = typedArray.getDimension(
                R.styleable.RoundedImageView_cornerRadius,
                cornerRadius
            )

            strokeMargin = typedArray.getDimension(
                R.styleable.RoundedImageView_strokeMargin,
                strokeMargin
            )

            imageStrokeWidth = typedArray.getDimension(
                R.styleable.RoundedImageView_strokeWidth,
                imageStrokeWidth
            )

            imageStrokeColor = typedArray.getColor(
                R.styleable.RoundedImageView_strokeColor,
                imageStrokeColor
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val desiredWidth: Int = DEFAULT_SIZE.toPx().toInt()
        val desiredHeight: Int = DEFAULT_SIZE.toPx().toInt()

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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        rectF = RectF(0f, 0f, w.toFloat(), h.toFloat())

        path.reset()
        path.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW)
        path.close()

        if (imageStrokeWidth != -1f) {
            val strokeMargin = 3f.toPx()

            rectFStroke = RectF(
                strokeMargin, strokeMargin, w.toFloat() - strokeMargin, h.toFloat() - strokeMargin
            )

            rectFImageClip = RectF(
                imageStrokeWidth + strokeMargin,
                imageStrokeWidth + strokeMargin,
                w.toFloat() - imageStrokeWidth - strokeMargin,
                h.toFloat() - imageStrokeWidth - strokeMargin
            )
            pathImageClip.reset()
            pathImageClip.addRoundRect(
                rectFImageClip!!,
                cornerRadius,
                cornerRadius,
                Path.Direction.CW
            )
            pathImageClip.close()
        }
    }

    override fun draw(canvas: Canvas?) {
        if (canvas != null) {
            val save = canvas.save()
            canvas.clipPath(path)
            super.draw(canvas)
            canvas.restoreToCount(save)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (imageStrokeWidth != -1f) {
            canvas?.drawRoundRect(
                rectFStroke!!,
                cornerRadius + imageStrokeWidth,
                cornerRadius + imageStrokeWidth,
                paintStroke
            )

            canvas?.clipPath(pathImageClip)
        }

        super.onDraw(canvas)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        if (canvas != null) {
            val save = canvas.save()
            canvas.clipPath(path)
            super.dispatchDraw(canvas)
            canvas.restoreToCount(save)
        }
    }

    private fun Float.toPx(): Float {
        return (this * resources.displayMetrics.density).roundToInt().toFloat()
    }

    companion object {
        private const val DEFAULT_CORNER_RADIUS = 12f
        private const val DEFAULT_SIZE = 50f
    }
}