package com.github.hahihohehe.ledmatrix.ledcontrol

import android.graphics.Bitmap

data class MatrixImage(var bitmap: Bitmap, val id: Long = System.currentTimeMillis())