package com.kumela.socialnetwork.ui.common.toolbar

import android.graphics.PorterDuff
import android.graphics.Typeface
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.IntDef
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import com.kumela.socialnetwork.R
import com.kumela.socialnetwork.ui.common.mvc.BaseViewMvc

/**
 * Created by Toko on 02,October,2020
 **/

class ToolbarViewMvc(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseViewMvc(inflater, parent, R.layout.layout_toolbar) {

    fun interface MenuListener {
        fun onMenuItemClicked(menuItem: MenuItem): Boolean
    }

    private var navigateUpListener: View.OnClickListener? = null
    private var menuListener: MenuListener? = null
    private var buttonRightOnClickListener: View.OnClickListener? = null

    private val buttonBack: ImageButton = findViewById(R.id.button_back)
    private val title: TextView = findViewById(R.id.text_header)
    private val buttonMenu: ImageButton = findViewById(R.id.button_menu)
    private val buttonRight: Button = findViewById(R.id.button_right)

    private var popupMenu: PopupMenu? = null

    init {
        buttonBack.setOnClickListener { view ->
            navigateUpListener?.onClick(view)
        }
        buttonMenu.setOnClickListener {
            popupMenu!!.show()
        }
        buttonRight.setOnClickListener { view ->
            buttonRightOnClickListener?.onClick(view)
        }
    }

    fun setBackgroundColor(@ColorRes color: Int) {
        rootView.setBackgroundColor(getColor(color))
    }

    fun setTitle(title: String) {
        this.title.text = title
    }

    fun setTitleStyle(
        textSize: Float,
        @ColorRes textColor: Int,
        bold: Boolean = false,
        @TextAlignment alignment: Int = TextView.TEXT_ALIGNMENT_TEXT_START
    ) {
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        title.setTextColor(getColor(textColor))
        if (bold) {
            title.setTypeface(title.typeface, Typeface.BOLD)
        }
        title.textAlignment = alignment
    }

    fun tintIcons(@ColorRes colorRes: Int) {
        val color = getColor(colorRes)

        buttonBack.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
        buttonMenu.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
    }

    fun enableUpButtonAndListen(navigateUpListener: View.OnClickListener) {
        this.navigateUpListener = navigateUpListener
        buttonBack.visibility = View.VISIBLE
    }

    fun enableMenuButtonAndListen(@MenuRes menuRes: Int, menuListener: MenuListener) {
        this.menuListener = menuListener
        buttonMenu.visibility = View.VISIBLE

        popupMenu = PopupMenu(context, buttonMenu)
        popupMenu!!.setOnMenuItemClickListener { menuItem ->
            this.menuListener!!.onMenuItemClicked(menuItem)
        }
        popupMenu!!.inflate(menuRes)
    }

    fun enableRightButtonAndListen(text: String, listener: View.OnClickListener) {
        this.buttonRightOnClickListener = listener
        buttonRight.text = text
        buttonRight.visibility = View.VISIBLE
    }

    fun disableButtons() {
        buttonBack.isEnabled = false
        buttonMenu.isEnabled = false
        buttonRight.isEnabled = false
    }

    fun enableButtons() {
        buttonBack.isEnabled = true
        buttonMenu.isEnabled = true
        buttonRight.isEnabled = true
    }
}

@IntDef(
    value = [
        View.TEXT_ALIGNMENT_INHERIT,
        View.TEXT_ALIGNMENT_GRAVITY,
        View.TEXT_ALIGNMENT_CENTER,
        View.TEXT_ALIGNMENT_TEXT_START,
        View.TEXT_ALIGNMENT_TEXT_END,
        View.TEXT_ALIGNMENT_VIEW_START,
        View.TEXT_ALIGNMENT_VIEW_END
    ]
)
@Retention(AnnotationRetention.SOURCE)
annotation class TextAlignment