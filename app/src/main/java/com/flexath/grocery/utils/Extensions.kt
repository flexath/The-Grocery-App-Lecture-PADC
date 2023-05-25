package com.flexath.grocery.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.graphics.scale
import com.bumptech.glide.Glide
import com.flexath.grocery.R

fun Uri.loadBitMapFromUri(context: Context) : Bitmap {
    return Glide.with(context)
        .asBitmap()
        .load(this)
        .submit()
        .get()
}

fun Bitmap.scaleToRatio(ratio : Double) : Bitmap {
    return this.scale((this.width * ratio).toInt(), (this.height * ratio).toInt())
}