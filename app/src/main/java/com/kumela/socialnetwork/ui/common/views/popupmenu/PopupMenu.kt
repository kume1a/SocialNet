package com.kumela.socialnetwork.ui.common.views.popupmenu

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.XmlRes
import androidx.core.animation.doOnEnd
import androidx.core.graphics.drawable.DrawableCompat
import com.kumela.socialnetwork.R
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt


/**
 * Created by Toko on 15,September,2020
 **/

@Suppress("MemberVisibilityCanBePrivate", "unused")
class PopupMenu @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.PopupMenu
) : View(context, attrs, defStyleAttr) {

    private var icons = emptyList<Drawable>()

    private var popupHeight = -1F

    private val linearInterpolator = LinearInterpolator()

    private var animationStage: AnimationStage = AnimationStage.HIDDEN
    private var popupY: Float = 0F
    private var currentMarginBetween = 0F
    private var iconMaskFraction = 0F

    private var listener: Listener? = null
    private var touchable = false

    var animationInProgress = false
        private set

    @ColorInt
    private var _backgroundColor = DEFAULT_BACKGROUND_COLOR

    @ColorInt
    private var _foregroundColor = DEFAULT_FOREGROUND_COLOR

    @Dimension
    private var _iconSize = DEFAULT_ICON_SIZE.toPx()

    @Dimension
    private var _verticalMargin = DEFAULT_VERTICAL_MARGIN.toPx()

    @Dimension
    private var _sideMargins = DEFAULT_IN_BETWEEN_MARGIN.toPx()

    @XmlRes
    private var _menuRes = -1

    // Core Attributes
    var menuBackgroundColor: Int
        @ColorInt get() = _backgroundColor
        set(@ColorInt value) {
            _backgroundColor = value
            paintBackground.color = value
            invalidate()
        }

    var menuForegroundColor: Int
        @ColorInt get() = _foregroundColor
        set(@ColorInt value) {
            _foregroundColor = value
            paintForeground.color = value
            invalidate()
        }

    var iconSize: Float
        @Dimension get() = _iconSize
        set(@Dimension value) {
            _iconSize = value
            invalidate()
        }

    val iconMaskRadius: Float
        @Dimension get() = iconSize * .62F


    var verticalMargin: Float
        @Dimension get() = _verticalMargin
        set(@Dimension value) {
            _verticalMargin = value
            invalidate()
        }

    var sideMargins: Float
        @Dimension get() = _sideMargins
        set(@Dimension value) {
            _sideMargins = value
            invalidate()
        }

    private var menuRes: Int
        @XmlRes get() = _menuRes
        set(@XmlRes value) {
            _menuRes = value
            if (value != -1) {
                icons = PopupMenuParser(context, value).parse()
                invalidate()
            }
        }

    // Paints
    private val paintBackground = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = menuBackgroundColor
    }

    private val paintForeground = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = menuForegroundColor
    }


    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.PopupMenu, defStyleAttr, 0
        )

        try {
            menuBackgroundColor = typedArray.getColor(
                R.styleable.PopupMenu_backgroundColor, menuForegroundColor
            )

            menuForegroundColor = typedArray.getColor(
                R.styleable.PopupMenu_foregroundColor, menuForegroundColor
            )

            iconSize = typedArray.getDimension(
                R.styleable.PopupMenu_iconSize, iconSize
            )

            verticalMargin = typedArray.getDimension(
                R.styleable.PopupMenu_marginVertical, verticalMargin
            )

            sideMargins = typedArray.getDimension(
                R.styleable.PopupMenu_iconInBetweenMargin, sideMargins
            )

            menuRes = typedArray.getResourceId(
                R.styleable.PopupMenu_menu, menuRes
            )

            popupHeight = iconSize + verticalMargin * 2

            // tint icons
            for (icon in icons) {
                DrawableCompat.setTint(icon, menuForegroundColor)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val desiredWidth: Int = ((sideMargins + iconSize) * 4).toInt()
        val desiredHeight: Int = (popupHeight * 2.5).toInt()

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

        if (canvas != null) when (animationStage) {
            AnimationStage.HIDDEN -> return
            AnimationStage.REVEAL_OR_HIDE -> {
                canvas.drawCircle(width / 2F, popupY, popupHeight / 2, paintBackground)
                canvas.drawCircle(width / 2F, popupY, iconMaskRadius, paintForeground)
            }
            AnimationStage.EXPANSION_OR_REDUCTION -> {
                val fraction = currentMarginBetween / sideMargins
                val halfWidth = width / 2F

                val pointXLeft =
                    halfWidth - (iconMaskRadius + currentMarginBetween + iconMaskRadius) * fraction
                val pointXRight =
                    halfWidth + (iconMaskRadius + currentMarginBetween + iconMaskRadius) * fraction

                // draw background
                canvas.drawRoundRect(
                    pointXLeft - sideMargins * 2,
                    popupY - popupHeight / 2,
                    pointXRight + sideMargins * 2,
                    popupY + popupHeight / 2,
                    100F, 100F,
                    paintBackground
                )

                canvas.drawCircle(pointXLeft, popupY, iconMaskRadius, paintForeground)    // left
                canvas.drawCircle(pointXRight, popupY, iconMaskRadius, paintForeground)   // right
                canvas.drawCircle(width / 2F, popupY, iconMaskRadius, paintForeground) // center
            }
            AnimationStage.REVEAL_OR_HIDE_ICONS -> {
                val halfWidth = width / 2F
                val circleRadius = iconMaskRadius * iconMaskFraction

                val pointXLeft = halfWidth - iconMaskRadius * 2 - sideMargins
                val pointXRight = halfWidth + iconMaskRadius * 2 + sideMargins

                // draw background
                canvas.drawRoundRect(
                    0F,
                    popupY - popupHeight / 2,
                    width.toFloat(),
                    popupY + popupHeight / 2,
                    100F, 100F,
                    paintBackground
                )

                val iconExtraSize = iconSize * iconMaskFraction / 6

                // set icon bounds
                val iconHalfSize = iconSize / 2 + iconExtraSize
                val iconTopBound = (popupY - iconHalfSize - iconExtraSize).toInt()
                val iconBottomBound = (popupY + iconHalfSize + iconExtraSize).toInt()
                icons[0].setBounds(
                    (pointXLeft - iconHalfSize).toInt(), iconTopBound,
                    (pointXLeft + iconHalfSize).toInt(), iconBottomBound
                )
                icons[1].setBounds(
                    (halfWidth - iconHalfSize).toInt(), iconTopBound,
                    (halfWidth + iconHalfSize).toInt(), iconBottomBound
                )
                icons[2].setBounds(
                    (pointXRight - iconHalfSize).toInt(), iconTopBound,
                    (pointXRight + iconHalfSize).toInt(), iconBottomBound
                )

                // draw icons on canvas
                icons.forEach { it.draw(canvas) }

                canvas.drawCircle(pointXLeft, popupY, circleRadius, paintForeground)    // left
                canvas.drawCircle(pointXRight, popupY, circleRadius, paintForeground)   // right
                canvas.drawCircle(width / 2F, popupY, circleRadius, paintForeground) // center
            }
        }
    }

    fun open() {
        animationStage = AnimationStage.REVEAL_OR_HIDE

        val popupAnimator = getValueAnimator(0F, height.toFloat(), 500)
        popupAnimator.addUpdateListener {
            popupY = height - (it.animatedValue as Float) + popupHeight / 2
            invalidate()
        }

        val expansionAnimator = getValueAnimator(0F, sideMargins, 300)
        expansionAnimator.addUpdateListener {
            currentMarginBetween = it.animatedValue as Float
            invalidate()
        }

        val showIconsAnimator = ValueAnimator.ofFloat(1F, 0F).apply {
            duration = 600
            addUpdateListener {
                iconMaskFraction = it.animatedValue as Float
                invalidate()
            }
        }

        popupAnimator.doOnEnd {
            animationStage = AnimationStage.EXPANSION_OR_REDUCTION
            expansionAnimator.start()
        }
        expansionAnimator.doOnEnd {
            animationStage = AnimationStage.REVEAL_OR_HIDE_ICONS
            showIconsAnimator.start()
        }
        showIconsAnimator.doOnEnd {
            touchable = true
            animationInProgress = false
        }

        animationInProgress = true
        popupAnimator.start()
    }

    fun close() {
        animationStage = AnimationStage.REVEAL_OR_HIDE_ICONS
        touchable = false

        val hideAnimator = getValueAnimator(0F, height.toFloat(), 500)
        hideAnimator.addUpdateListener {
            popupY = (it.animatedValue as Float) + popupHeight / 2
            invalidate()
        }

        val reductionAnimator = getValueAnimator(sideMargins, 0F, 300)
        reductionAnimator.addUpdateListener {
            currentMarginBetween = it.animatedValue as Float
            invalidate()
        }

        val hideIconsAnimator = getValueAnimator(0F, 1F, 300)
        hideIconsAnimator.addUpdateListener {
            iconMaskFraction = it.animatedValue as Float
            invalidate()
        }

        // chain animations
        hideIconsAnimator.doOnEnd {
            animationStage = AnimationStage.EXPANSION_OR_REDUCTION
            reductionAnimator.start()
        }
        reductionAnimator.doOnEnd {
            listener?.onReductionCompleted()
            animationStage = AnimationStage.REVEAL_OR_HIDE
            hideAnimator.start()
        }
        hideAnimator.doOnEnd {
            animationInProgress = false
        }

        animationInProgress = true
        hideIconsAnimator.start()
    }

    private fun getValueAnimator(from: Float, to: Float, duration: Long): ValueAnimator {
        return ValueAnimator.ofFloat(from, to).apply {
            this.duration = duration
            interpolator = linearInterpolator
        }
    }

    private fun Float.toPx(): Float {
        return (this * resources.displayMetrics.density).roundToInt().toFloat()
    }

    private enum class AnimationStage {
        REVEAL_OR_HIDE,
        EXPANSION_OR_REDUCTION,
        REVEAL_OR_HIDE_ICONS,

        HIDDEN
    }

    interface Listener {
        fun onImageClicked()
        fun onFileClicked()
        fun onVideoClicked()

        fun onReductionCompleted()
    }

    fun addListener(listener: Listener) {
        this.listener = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (touchable) {
            if (event.y > popupY - popupHeight / 2 && event.y < popupY + popupHeight / 2) {
                if (event.action == MotionEvent.ACTION_UP && abs(event.downTime - event.eventTime) < 500) {
                    when {
                        event.x < width / 2 - iconSize / 2 - currentMarginBetween / 2 -> listener?.onImageClicked()
                        event.x > width / 2 + iconSize / 2 + currentMarginBetween / 2 -> listener?.onVideoClicked()
                        else -> listener?.onFileClicked()
                    }
                }
            }
        }

        return true
    }

    companion object {
        private const val DEFAULT_BACKGROUND_COLOR = Color.BLACK
        private const val DEFAULT_FOREGROUND_COLOR = Color.WHITE

        private const val DEFAULT_ICON_SIZE = 18F
        private const val DEFAULT_IN_BETWEEN_MARGIN = 16F
        private const val DEFAULT_VERTICAL_MARGIN = 12F
    }
}