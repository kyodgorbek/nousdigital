package com.core.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import java.lang.Exception


fun Drawable.toBitmap(): Bitmap? = try {
    Bitmap.createBitmap(this.intrinsicWidth, this.intrinsicHeight, Bitmap.Config.ARGB_8888).apply {
        val canvas = Canvas(this)
        this@toBitmap.setBounds(0, 0, canvas.width, canvas.height)
        this@toBitmap.draw(canvas)
    }
} catch (e: Exception) {
    null
}