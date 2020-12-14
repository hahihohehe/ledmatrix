package com.github.hahihohehe.ledmatrix.ledcontrol.ui.main

import android.app.Application
import android.graphics.*
import androidx.annotation.ColorInt
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.hahihohehe.ledmatrix.ledcontrol.ImageRepository
import com.github.hahihohehe.ledmatrix.ledcontrol.MatrixImage
import com.github.hahihohehe.ledmatrix.ledcontrol.imageRepository

class MatrixViewModel(application: Application) : AndroidViewModel(application) {
    private val colorsSource: Array<Array<Int>> = Array(10) { Array(10) { Color.BLACK } }
    val colors: LiveData<Array<Array<Int>>> = MutableLiveData(colorsSource)
    private val paletteSource: Array<Int> = Array(16) { Color.BLACK }
    val palette: LiveData<Array<Int>> = MutableLiveData(paletteSource)
    private val imageRepository: ImageRepository = imageRepository(application)
    var matrixImage: MatrixImage? = null

    fun loadImage(id: Long) {
        if (id != MatrixFragment.EMPTY_MATRIX) {
            matrixImage = imageRepository.getImage(id)
            loadImage(matrixImage!!)
        }
    }

    private fun loadImage(image: MatrixImage) {
        for (x in 0 until 10)
            for (y in 0 until 10)
                colorsSource[x][y] = image.bitmap.getPixel(x, y)
    }

    fun updateColor(x: Int, y: Int, selected: Int) {
        colorsSource[x][y] = paletteSource[selected]
        (colors as MutableLiveData).value = colorsSource
    }

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

    fun isEmpty(): Boolean {
        for (x in 0 until 10)
            for (y in 0 until 10)
                if (colorsSource[x][y] and 0x00FFFFFF != 0)
                    return false
        return true
    }

    fun saveMatrixImage() {
        if (isEmpty())
            return

        if (matrixImage == null)
            matrixImage = MatrixImage(Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888))
        val image = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        for (x in 0 until 10)
            for (y in 0 until 10)
                canvas.drawRect(
                    Rect(x, y, x + 1, y + 1),
                    Paint().apply { color = colorsSource[x][y] })
        matrixImage!!.bitmap = image
        imageRepository.saveImage(matrixImage!!)
    }

    override fun onCleared() {
        super.onCleared()
        println("Cleared Viewmodel")
        saveMatrixImage()
    }
}