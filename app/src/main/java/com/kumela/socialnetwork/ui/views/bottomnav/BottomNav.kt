package com.kumela.socialnetwork.ui.views.bottomnav

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.FontRes
import androidx.annotation.XmlRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.kumela.socialnetwork.R
import kotlinx.android.parcel.Parcelize
import kotlin.math.abs
import kotlin.math.roundToInt


@Suppress("MemberVisibilityCanBePrivate")
class BottomNav @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.BottomNavStyle
) : View(context, attrs, defStyleAttr) {

    // Dynamic Variables
    private var itemWidth: Float = 0F
    private var currentIconTint = itemIconTintActive

    private var items = emptyList<BottomNavItem>()

    // Attribute Defaults
    @ColorInt
    private var _barBackgroundColor = Color.WHITE

    @Dimension
    private var _barSideMargins = DEFAULT_SIDE_MARGIN.toPx()

    @Dimension
    private var _itemPadding = DEFAULT_ITEM_PADDING.toPx()

    @Dimension
    private var _itemIconSize = DEFAULT_ICON_SIZE.toPx()

    @Dimension
    private var _itemIconMargin = DEFAULT_ICON_MARGIN.toPx()

    @ColorInt
    private var _itemIconTint = Color.parseColor(DEFAULT_TINT)

    @ColorInt
    private var _itemIconTintActive = Color.WHITE

    @ColorInt
    private var _itemTextColor = Color.WHITE

    @Dimension
    private var _itemTextSize = DEFAULT_TEXT_SIZE.toPx()

    @FontRes
    private var _itemFontFamily: Int = INVALID_RES

    @XmlRes
    private var _itemMenuRes: Int = INVALID_RES

    private var _itemActiveIndex: Int = 0

    // Core Attributes
    var barBackgroundColor: Int
        @ColorInt get() = _barBackgroundColor
        set(@ColorInt value) {
            _barBackgroundColor = value
            paintBackground.color = value
            invalidate()
        }

    var barSideMargins: Float
        @Dimension get() = _barSideMargins
        set(@Dimension value) {
            _barSideMargins = value
            invalidate()
        }

    var itemTextSize: Float
        @Dimension get() = _itemTextSize
        set(@Dimension value) {
            _itemTextSize = value
            paintText.textSize = value
            invalidate()
        }

    var itemTextColor: Int
        @ColorInt get() = _itemTextColor
        set(@ColorInt value) {
            _itemTextColor = value
            paintText.color = value
            invalidate()
        }

    var itemPadding: Float
        @Dimension get() = _itemPadding
        set(@Dimension value) {
            _itemPadding = value
            invalidate()
        }

    var itemIconSize: Float
        @Dimension get() = _itemIconSize
        set(@Dimension value) {
            _itemIconSize = value
            invalidate()
        }

    var itemIconMargin: Float
        @Dimension get() = _itemIconMargin
        set(@Dimension value) {
            _itemIconMargin = value
            invalidate()
        }

    var itemIconTint: Int
        @ColorInt get() = _itemIconTint
        set(@ColorInt value) {
            _itemIconTint = value
            invalidate()
        }

    var itemIconTintActive: Int
        @ColorInt get() = _itemIconTintActive
        set(@ColorInt value) {
            _itemIconTintActive = value
            invalidate()
        }

    private var itemFontFamily: Int
        @FontRes get() = _itemFontFamily
        set(@FontRes value) {
            _itemFontFamily = value
            if (value != INVALID_RES) {
                paintText.typeface = ResourcesCompat.getFont(context, value)
                invalidate()
            }
        }

    private var itemMenuRes: Int
        @XmlRes get() = _itemMenuRes
        set(@XmlRes value) {
            _itemMenuRes = value
            if (value != INVALID_RES) {
                items = BottomNavParser(context, value).parse()
                invalidate()
            }
        }

    private var itemActiveIndex: Int
        get() = _itemActiveIndex
        set(value) {
            _itemActiveIndex = value
            applyItemActiveIndex()
        }

    // Listeners
    var onItemSelectedListener: OnItemSelectedListener? = null

    var onItemReselectedListener: OnItemReselectedListener? = null

    var onItemSelected: ((Int) -> Unit)? = null

    var onItemReselected: ((Int) -> Unit)? = null

    // Paints
    private val paintBackground = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = barBackgroundColor
    }

    private val paintText = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = itemTextColor
        textSize = itemTextSize
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    init {
        obtainStyledAttributes(attrs, defStyleAttr)
    }

    private fun obtainStyledAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BottomNav,
            defStyleAttr,
            0
        )

        try {
            barBackgroundColor = typedArray.getColor(
                R.styleable.BottomNav_backgroundColor,
                barBackgroundColor
            )

            barSideMargins = typedArray.getDimension(
                R.styleable.BottomNav_sideMargins,
                barSideMargins
            )

            itemPadding = typedArray.getDimension(
                R.styleable.BottomNav_itemPadding,
                itemPadding
            )

            itemTextColor = typedArray.getColor(
                R.styleable.BottomNav_textColor,
                itemTextColor
            )

            itemTextSize = typedArray.getDimension(
                R.styleable.BottomNav_textSize,
                itemTextSize
            )

            itemIconSize = typedArray.getDimension(
                R.styleable.BottomNav_iconSize,
                itemIconSize
            )

            itemIconMargin = typedArray.getDimension(
                R.styleable.BottomNav_iconMargin,
                itemIconMargin
            )

            itemIconTint = typedArray.getColor(
                R.styleable.BottomNav_iconTint,
                itemIconTint
            )

            itemIconTintActive = typedArray.getColor(
                R.styleable.BottomNav_iconTintActive,
                itemIconTintActive
            )

            itemActiveIndex = typedArray.getInt(
                R.styleable.BottomNav_activeItem,
                itemActiveIndex
            )

            itemFontFamily = typedArray.getResourceId(
                R.styleable.BottomNav_itemFontFamily,
                itemFontFamily
            )

            itemMenuRes = typedArray.getResourceId(
                R.styleable.BottomNav_menu,
                itemMenuRes
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    @Parcelize
    private class BottomNavState(
        val superSaveState: Parcelable?,
        val itemActiveIndex: Int
    ) : View.BaseSavedState(superSaveState), Parcelable

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return BottomNavState(superState, itemActiveIndex)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val bottomNavState = state as? BottomNavState
        super.onRestoreInstanceState(bottomNavState?.superSaveState)

        itemActiveIndex = bottomNavState?.itemActiveIndex ?: _itemActiveIndex
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        var lastX = barSideMargins
        itemWidth = (width - (barSideMargins * 2)) / (items.size + 1)

        for ((index, item) in items.withIndex()) {
            // Prevent text overflow by shortening the item title
            var shorted = false
            while (paintText.measureText(item.title) > itemWidth - itemIconMargin - (itemPadding * 2)) {
                item.title = item.title.dropLast(1)
                shorted = true
            }

            // Add ellipsis character to item text if it is shorted
            if (shorted) {
                item.title = item.title.dropLast(1)
                item.title += context.getString(R.string.ellipsis)
            }

            if (index == 2) {
                lastX += itemWidth
            }
            item.rect = RectF(lastX, 0f, itemWidth + lastX, height.toFloat())
            lastX += itemWidth
        }

        // Set initial active item
        applyItemActiveIndex()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw background
        canvas.drawRect(
            0f, 0f,
            width.toFloat(),
            height.toFloat(),
            paintBackground
        )

        val textHeight = (paintText.descent() + paintText.ascent()) / 2

        for ((index, item) in items.withIndex()) {
            item.icon.mutate()
            item.icon.setBounds(
                item.rect.centerX().toInt()
                        - itemIconSize.toInt() / 2,
                height / 2 - itemIconSize.toInt() / 2
                        + (textHeight * 2 * (item.alpha / OPAQUE.toFloat())).toInt(),
                item.rect.centerX().toInt()
                        + itemIconSize.toInt() / 2,
                height / 2 + itemIconSize.toInt() / 2
                        + (textHeight * 2 * (item.alpha / OPAQUE.toFloat())).toInt()
            )

            tintAndDrawIcon(item, index, canvas)

            paintText.alpha = item.alpha
            canvas.drawText(
                item.title,
                item.rect.centerX(),
                item.rect.centerY() + textHeight + itemIconSize, paintText
            )
        }
    }

    private fun tintAndDrawIcon(
        item: BottomNavItem,
        index: Int,
        canvas: Canvas
    ) {
        DrawableCompat.setTint(
            item.icon,
            if (index == itemActiveIndex) currentIconTint else itemIconTint
        )

        item.icon.draw(canvas)
    }

    /**
     * Handle item clicks
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP && abs(event.downTime - event.eventTime) < 500) {
            for ((i, item) in items.withIndex()) {
                if (item.rect.contains(event.x, event.y)) {
                    if (i != itemActiveIndex) {
                        itemActiveIndex = i
                        onItemSelected?.invoke(i)
                        onItemSelectedListener?.onItemSelected(i)
                    } else {
                        onItemReselected?.invoke(i)
                        onItemReselectedListener?.onItemReselected(i)
                    }
                }
            }
        }

        return true
    }

    private fun applyItemActiveIndex() {
        if (items.isNotEmpty()) {
            for ((index, item) in items.withIndex()) {
                if (index == itemActiveIndex) {
                    animateAlpha(item, OPAQUE)
                } else {
                    animateAlpha(item, TRANSPARENT)
                }
            }

            ValueAnimator.ofObject(ArgbEvaluator(), itemIconTint, itemIconTintActive).apply {
                duration = DEFAULT_ANIM_DURATION
                addUpdateListener {
                    currentIconTint = it.animatedValue as Int
                }
                start()
            }
        }
    }

    private fun animateAlpha(item: BottomNavItem, to: Int) {
        ValueAnimator.ofInt(item.alpha, to).apply {
            duration = DEFAULT_ANIM_DURATION
            addUpdateListener {
                item.alpha = it.animatedValue as Int
                invalidate()
            }
            start()
        }
    }

    interface OnItemSelectedListener {
        fun onItemSelected(pos: Int): Boolean
    }

    interface OnItemReselectedListener {
        fun onItemReselected(pos: Int)
    }

    /**
     * Just call [BottomNav.setOnItemSelectedListener] to override [onItemSelectedListener]
     *
     * @sample
     * setOnItemSelectedListener { position ->
     *     //TODO: Something
     * }
     */
    @Suppress("unused")
    fun setOnItemSelectedListener(listener: (position: Int) -> Unit) {
        onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(pos: Int): Boolean {
                listener.invoke(pos)
                return true
            }
        }
    }

    /**
     * Just call [BottomNav.setOnItemReselectedListener] to override [onItemReselectedListener]
     *
     * @sample
     * setOnItemReselectedListener { position ->
     *     //TODO: Something
     * }
     */
    @Suppress("unused")
    fun setOnItemReselectedListener(listener: (position: Int) -> Unit) {
        onItemReselectedListener = object : OnItemReselectedListener {
            override fun onItemReselected(pos: Int) {
                listener.invoke(pos)
            }
        }
    }

    private fun Float.toPx(): Float {
        return (this * resources.displayMetrics.density).roundToInt().toFloat()
    }

    companion object {
        private const val INVALID_RES = -1
        private const val DEFAULT_TINT = "#C8FFFFFF"

        private const val DEFAULT_SIDE_MARGIN = 10F
        private const val DEFAULT_ITEM_PADDING = 10F
        private const val DEFAULT_ANIM_DURATION = 200L
        private const val DEFAULT_ICON_SIZE = 20F
        private const val DEFAULT_ICON_MARGIN = 4F
        private const val DEFAULT_TEXT_SIZE = 12F

        private const val OPAQUE = 255
        private const val TRANSPARENT = 0
    }
}
