package com.github.hahihohehe.ledmatrix.ledcontrol

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.GridLayout
import androidx.annotation.ColorInt


class MatrixView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    GridLayout(context, attrs, defStyleAttr) {
    private val views: Array<Array<View>> = Array(10) { Array(10) { View(context) } }

    init {
        columnCount = 10
        rowCount = 10
        for (i in 0 until 10) {
            for (j in 0 until 10) {
                val pixelView = views[i][j]
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
                    if (onPixelClickedListerner != null)
                        onPixelClickedListerner!!(i, j)
                }
                addView(pixelView)
            }
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
                    pParams.height = pLength
                    layoutParams = pParams

                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
    }

    var onPixelClickedListerner: ((Int, Int) -> Unit)? = null

    fun setPixelColor(x: Int, y: Int, @ColorInt color: Int) {
        views[x][y].setBackgroundColor(color)
    }

    fun setColors(colors: Array<Array<Int>>) {
        for (i in 0 until 10) {
            for (j in 0 until 10) {
                views[i][j].setBackgroundColor(colors[i][j])
            }
        }
    }
}