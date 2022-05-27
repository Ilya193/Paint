package com.xlwe.paint.core

import android.graphics.Bitmap

interface SaveFile {
    fun save(bitmap: Bitmap, name: String)
}