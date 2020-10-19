package com.github.hahihohehe.ledmatrix.ledcontrol.ui.main

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val colorsSource: Array<Array<Int>> = Array(10) { Array(10) { Color.BLACK } }

    val colors: LiveData<Array<Array<Int>>> = MutableLiveData(colorsSource)

    fun updateColor(x: Int, y: Int, @ColorInt color: Int) {
        colorsSource[x][y] = color
        (colors as MutableLiveData).value = colorsSource
    }

    fun getColor(x: Int, y: Int) = colorsSource[x][y]

    // TODO: Implement the ViewModel
}