package com.github.hahihohehe.ledmatrix.ledcontrol

import android.app.Application
import android.graphics.*
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

private var imageRepository: ImageRepository = ImageRepository()

private lateinit var context: Application

fun imageRepository(application: Application): ImageRepository {
    context = application;
    return imageRepository
}

class ImageRepository {
    private var imageFolder: File = File(Environment.getExternalStorageDirectory(), "matrix_images")
    val images: LiveData<List<MatrixImage>> = MutableLiveData(listOf())
    private var imageList: List<MatrixImage> = LinkedList<MatrixImage>()

    init {
        imageFolder.mkdir()
        readImages()
    }

    private fun readImages() {
        imageFolder.listFiles()
            ?.forEach { file ->  //read no images if it is not possible to read files
                try {
                    if (file.name.endsWith(".png"))
                        (imageList as LinkedList).add(
                            MatrixImage(
                                BitmapFactory.decodeFile(file.absolutePath),
                                file.name.substring(0, file.name.length - 4).toLong()
                            )
                        )
                } catch (e: NumberFormatException) {
                    Log.d("READ IMAGE", "File $file does not have a valid name")
            }

        }
        if (imageList.isEmpty())
            addNewImage()
        else
            (images as MutableLiveData).value = imageList
    }

    fun getImage(id: Long) = imageList.filter { image -> image.id == id }[0]

    fun addNewImage(): MatrixImage {
        val bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        for (x in 0 until 10)
            for (y in 0 until 10)
                canvas.drawRect(
                    Rect(x, y, x + 1, y + 1),
                    Paint().apply { color = 0xFF000000.toInt() })
        val matrixImage = MatrixImage(bitmap)
        saveImage(matrixImage)
        return matrixImage
    }

    fun saveImage(image: MatrixImage) {
        try {
            val fos = FileOutputStream(File(imageFolder.absolutePath, "${image.id}.png"))
            fos.use { fos ->
                image.bitmap.compress(Bitmap.CompressFormat.PNG, 25, fos)
            }
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return
        }

        if (!imageList.contains(image)) {
            (imageList as LinkedList).add(image)
            (images as MutableLiveData).value = imageList
        }
    }

    fun deleteMatrixImage(image: MatrixImage) {
        File(imageFolder.absolutePath, "${image.id}.png").delete()
        (imageList as LinkedList).remove(image)

        if (imageList.isEmpty())
            addNewImage()
        else
            (images as MutableLiveData).value = imageList
    }
}