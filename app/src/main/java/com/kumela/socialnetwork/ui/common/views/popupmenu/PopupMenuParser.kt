package com.kumela.socialnetwork.ui.common.views.popupmenu

import android.content.Context
import android.content.res.XmlResourceParser
import android.graphics.drawable.Drawable
import androidx.annotation.XmlRes
import androidx.core.content.ContextCompat

/**
 * Created by Toko on 15,September,2020
 **/

class PopupMenuParser(private val context: Context, @XmlRes res: Int) {

    private val parser: XmlResourceParser = context.resources.getXml(res)

    fun parse(): List<Drawable> {
        val items = mutableListOf<Drawable>()
        var eventType: Int?

        do {
            eventType = parser.next()
            if (eventType == XmlResourceParser.START_TAG && parser.name == ITEM_TAG) {
                items.add(getTabConfig(parser))
            }
        } while (eventType != XmlResourceParser.END_DOCUMENT)

        return items
    }

    private fun getTabConfig(parser: XmlResourceParser): Drawable {
        val attributeCount = parser.attributeCount
        var itemDrawable: Drawable? = null

        for (index in 0 until attributeCount) {
            when (parser.getAttributeName(index)) {
                ICON_ATTRIBUTE ->
                    itemDrawable = ContextCompat.getDrawable(
                        context,
                        parser.getAttributeResourceValue(index, 0)
                    )
            }
        }

        if (itemDrawable == null)
            throw Throwable("Item icon can not be null!")

        return itemDrawable
    }

    companion object {
        private const val ITEM_TAG = "item"
        private const val ICON_ATTRIBUTE = "icon"
    }
}