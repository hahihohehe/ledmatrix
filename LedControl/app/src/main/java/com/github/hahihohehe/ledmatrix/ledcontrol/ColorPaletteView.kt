package com.github.hahihohehe.ledmatrix.ledcontrol

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.GridLayout
import androidx.annotation.ColorInt


class ColorPaletteView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    GridLayout(context, attrs, defStyleAttr) {
    private val views: Array<View> = Array(16) { View(context) }
    private var colors: Array<Int> = Array(16) { Color.BLACK }
    var selected = 0
        set(value) {
            field = value
            updateViews()
        }

    init {
        columnCount = 8
        rowCount = 2
        for (i in 0 until 16) {
            val pixelView = views[i]
            val param = LayoutParams(
                spec(
                    UNDEFINED, FILL, 1f
                ),
                spec(UNDEFINED, FILL, 1f)
            )
            param.height = 0
            param.width = 0
            param.setMargins(1, 1, 1, 1)
            pixelView.layoutParams = param
            pixelView.setBackgroundColor(Color.BLACK)
            pixelView.setOnClickListener {
                if (onItemClickedListerner != null)
                    onItemClickedListerner!!(i)
            }
            addView(pixelView)
        }

        viewTreeObserver.addOnGlobalLayoutListener(
            object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val pLength: Int
                    val pWidth: Int = width
                    val pHeight: Int = width

                    //Set myGridLayout equal width and height
                    pLength = if (pWidth >= pHeight) pHeight else pWidth
                    val pParams: ViewGroup.LayoutParams = layoutParams
                    pParams.width = pLength
                    pParams.height = pLength / 4
                    layoutParams = pParams

                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        selected = 0
    }

    var onItemClickedListerner: ((Int) -> Unit)? = null

    fun setPixelColor(i: Int, @ColorInt color: Int) {
        views[i].setBackgroundColor(color)
    }

    fun setColors(colors: Array<Int>) {
        this.colors = colors
        updateViews()
    }

    private fun updateViews() {
        for (i in 0 until 16) {
            if (selected == i) {
                val border = GradientDrawable()
                border.setColor(colors[i])
                border.setStroke(15, Color.TRANSPARENT)
                views[i].background = border
            } else
                views[i].setBackgroundColor(colors[i])
        }
    }

    fun isSelected(x: Int) = selected == x
}