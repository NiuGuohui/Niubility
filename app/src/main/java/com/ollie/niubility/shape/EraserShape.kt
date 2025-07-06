package com.ollie.niubility.shape

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode

open class EraserShape(width: Float = 2f) : StrokeShape(width, Color.TRANSPARENT) {
  init {
    paint.isFilterBitmap = false
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
  }
}
