package com.github.hahihohehe.ledmatrix.ledcontrol.ui.main

import android.graphics.Color
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

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

    fun upload(ipAddress: String) {
        val job = viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL("http://$ipAddress/display")

                val con: HttpURLConnection = url.openConnection() as HttpURLConnection
                con.requestMethod = "POST"

                con.setRequestProperty("Content-Type", "application/json; utf-8")

                con.doOutput = true

                val jsonInputString = createJson()

                con.outputStream.use { os ->
                    val input = jsonInputString.toByteArray(charset("utf-8"))
                    os.write(input, 0, input.size)
                }

                val code: Int = con.responseCode
                println(code)

                BufferedReader(InputStreamReader(con.inputStream, "utf-8")).use { br ->
                    val response = StringBuilder()
                    var responseLine: String? = null
                    while (br.readLine().also { responseLine = it } != null) {
                        response.append(responseLine!!.trim { it <= ' ' })
                    }
                    println(response.toString())
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    private fun createJson(): String {
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