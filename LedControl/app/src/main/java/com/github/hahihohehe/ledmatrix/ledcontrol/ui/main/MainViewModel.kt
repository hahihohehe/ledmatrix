package com.github.hahihohehe.ledmatrix.ledcontrol.ui.main

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val colorsSource: Array<Array<Int>> = Array(10) { Array(10) { Color.BLACK } }
    val colors: LiveData<Array<Array<Int>>> = MutableLiveData(colorsSource)
    private val paletteSource: Array<Int> = Array(16) { Color.BLACK }
    val palette: LiveData<Array<Int>> = MutableLiveData(paletteSource)

    fun updateColor(x: Int, y: Int, selected: Int) {
        colorsSource[x][y] = paletteSource[selected]
        (colors as MutableLiveData).value = colorsSource
    }

    fun getColor(x: Int, y: Int) = colorsSource[x][y]

    fun getPaletteColor(x: Int) = paletteSource[x]

    fun updatePaletteColor(x: Int, @ColorInt color: Int) {
        paletteSource[x] = color
        (palette as MutableLiveData).value = paletteSource
    }

    fun createJson(): String {
        val sb = StringBuilder()
        sb.append("[[")
        for (i in 0 until 10) {
            for (j in 0 until 10) {
                val color = if (i % 2 == 0) colorsSource[i][j] else colorsSource[i][9 - j]
                sb.append("[${Color.red(color)},${Color.green(color)}, ${Color.blue(color)}]")
                if (j != 9 || i != 9) sb.append(",")
            }
        }
        sb.append("]]")
        return sb.toString()
    }
}