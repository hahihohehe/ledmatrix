package com.github.hahihohehe.ledmatrix.ledcontrol

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.util.*

private var imageRepository: ImageRepository = ImageRepository()

private lateinit var context: Application

fun imageRepository(application: Application): ImageRepository {
    context = application;
    return imageRepository
}

class ImageRepository {
    private var imageFolder: File = File(Environment.getExternalStorageDirectory(), "matrix_images")
    var imageList: List<MatrixImage> private set

    init {
        imageList = LinkedList<MatrixImage>()
        imageFolder.mkdir()
        readImages()
    }

    private fun readImages() {
        imageFolder.listFiles()!!.forEach { file ->
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
    }

    fun getImage(id: Long) = imageList.filter { image -> image.id == id }[0]

    fun saveImage(image: MatrixImage) {
        val fos = FileOutputStream(File(imageFolder.absolutePath, "${image.id}.png"))
        fos.use { fos ->
            image.bitmap.compress(Bitmap.CompressFormat.PNG, 25, fos)
        }

        fos.close()

        if (!imageList.contains(image))
            (imageList as LinkedList).add(image)
    }
}