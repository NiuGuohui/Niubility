package com.ollie.niubility.brush

import android.view.MotionEvent
import com.ollie.niubility.DrawDataCore
import com.ollie.niubility.shape.EraserShape

class EraserBrush(core: DrawDataCore) : BallPenBrush(core) {

  override fun onActionDown(event: MotionEvent) {
    if (isDrawing) {
      getStylusPoint(event)?.let {
        current = EraserShape(strokeWidth)
        current?.onCreate(it)
      }
    }
  }
}