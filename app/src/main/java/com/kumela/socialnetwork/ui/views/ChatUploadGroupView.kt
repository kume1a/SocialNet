package com.kumela.socialnetwork.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.kumela.socialnetwork.R
import kotlin.math.min
import kotlin.math.roundToInt


@Suppress("MemberVisibilityCanBePrivate")
class ChatUploadGroupView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.ChatUploadGroupView
) : View(context, attrs, defStyleAttr) {

    // Attribute Defaults
    @ColorInt
    private var _colorBackground = DEFAULT_BACKGROUND_COLOR

    // Core Attributes
    var colorBackground: Int
        set(@ColorInt value) {
            _colorBackground = value
            paintBackground.color = value
            invalidate()
        }
        get() = _colorBackground

    // Paints
    private val paintBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = colorBackground
    }

    // Paths
    private val pathBackground = Path()

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ChatUploadGroupView,
            defStyleAttr,
            0
        )

        try {
            colorBackground = typedArray.getColor(
                R.styleable.ChatUploadGroupView_colorBackground,
                colorBackground
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val desiredWidth: Int = 225f.toPx().toInt()
        val desiredHeight: Int = 225f.toPx().toInt()

        //Measure Width
        val width: Int = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(desiredWidth, widthSize)
            else -> desiredWidth
        }

        //Measure Height
        val height: Int = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(desiredHeight, heightSize)
            else -> desiredHeight
        }
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        pathBackground.reset()
        pathBackground.moveTo(0f, h.toFloat())
        pathBackground.lineTo(w.toFloat(), h.toFloat())
        pathBackground.cubicTo(
            w.toFloat(), h.toFloat(),
            w.toFloat(), 0f,
            0f, 0f
        )
        pathBackground.lineTo(0f, h.toFloat())
        pathBackground.close()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawPath(pathBackground, paintBackground)
    }

    private fun Float.toPx(): Float {
        return (this * resources.displayMetrics.density).roundToInt().toFloat()
    }

    companion object {
        private val DEFAULT_BACKGROUND_COLOR = Color.parseColor("#2C2561")
    }
}
