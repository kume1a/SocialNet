package com.kumela.socialnetwork.ui.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.core.animation.doOnEnd
import com.kumela.socialnetwork.R
import kotlin.math.min
import kotlin.math.roundToInt


@Suppress("MemberVisibilityCanBePrivate")
class StoryProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.StoriesProgressView
) : View(context, attrs, defStyleAttr) {

    data class StoryProgressItem(val rect: RectF, var progress: Float)

    interface Listener {
        fun onNext()
        fun onPrevious()
        fun onBack()
        fun onComplete()
    }

    // Dynamic Variables
    private val storyItems = ArrayList<StoryProgressItem>()
    private var itemCount: Int = 0
    private var isPaused: Boolean = true
    private var currentIndex = 0

    private val animator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 3000
        addUpdateListener {
            storyItems[currentIndex].progress = animatedFraction
            invalidate()
        }
        doOnEnd {
            if (currentIndex == itemCount - 1) {
                listener?.onComplete()
            } else {
                skip()
            }
        }
    }

    // Attribute Defaults
    @ColorInt
    private var _colorActive = DEFAULT_COLOR_ACTIVE

    @ColorInt
    private var _colorInactive = DEFAULT_COLOR_INACTIVE

    private var _itemPadding = 4f.toPx()

    // Core Attributes
    var colorActive: Int
        set(@ColorInt value) {
            _colorActive = value
            paintActive.color = value
            invalidate()
        }
        get() = _colorActive

    var colorInactive: Int
        set(@ColorInt value) {
            _colorInactive = value
            paintInactive.color = value
            invalidate()
        }
        get() = _colorInactive

    // Paints
    private val paintActive = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = _colorActive
    }

    private val paintInactive = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = _colorInactive
    }

    // listeners
    var listener: Listener? = null

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.StoryProgressView,
            defStyleAttr,
            0
        )

        try {
            colorActive =
                typedArray.getColor(R.styleable.StoryProgressView_colorActive, colorActive)
            colorInactive =
                typedArray.getColor(R.styleable.StoryProgressView_colorInactive, colorInactive)
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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (itemCount > 0) {
            calculateItems(w.toFloat(), h.toFloat())
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for (item in storyItems) {
            // draw inactive part
            canvas?.drawRect(
                item.rect.left + item.rect.width() * item.progress,
                item.rect.top,
                item.rect.right,
                item.rect.bottom,
                paintInactive
            )

            // draw active part
            if (item.progress > 0) {
                canvas?.drawRect(
                    item.rect.left,
                    item.rect.top,
                    item.rect.left + item.rect.width() * item.progress,
                    item.rect.bottom,
                    paintActive
                )
            }
        }
    }

    private fun calculateItems(w: Float, h: Float) {
        var startX = paddingStart.toFloat()
        val allPadding = paddingStart + paddingEnd + (itemCount - 1) * _itemPadding
        val itemWidth = (w - allPadding) / itemCount

        storyItems.clear()
        for (i in 0 until itemCount) {
            val rect = RectF(startX, 0f, startX + itemWidth, h)
            storyItems.add(StoryProgressItem(rect,0f))
            startX += _itemPadding
            startX += itemWidth
        }
    }

    /**
     * binds count of items and stops the progress
     */
    fun bindCount(@IntRange(from = 0) count: Int) {
        itemCount = count
        isPaused = true
        calculateItems(width.toFloat(), height.toFloat())
        invalidate()
    }

    /**
     * start the progress
     *
     * requires [bindCount] to be called before this
     */
    fun start() {
        isPaused = false
        currentIndex = 0
        animator.start()
    }

    fun skip() {
        storyItems[currentIndex].progress = 1f
        if (currentIndex == itemCount - 1) {
            listener?.onComplete()
        } else {
            isPaused = false
            currentIndex++
            animator.start()
            listener?.onNext()
        }
    }

    fun reverse() {
        storyItems[currentIndex].progress = 0f
        if (currentIndex != 0) {
            isPaused = false
            currentIndex--
            animator.start()
            listener?.onPrevious()
        } else {
            listener?.onBack()
        }
    }

    fun pause() {
        isPaused = true
        animator.pause()
    }

    fun resume() {
        isPaused = false
        animator.resume()
    }

    private fun Float.toPx(): Float {
        return (this * resources.displayMetrics.density).roundToInt().toFloat()
    }

    @Suppress("SpellCheckingInspection")
    companion object {
        private const val DEFAULT_COLOR_ACTIVE = Color.WHITE
        private val DEFAULT_COLOR_INACTIVE = Color.parseColor("#8affffff")
    }
}
