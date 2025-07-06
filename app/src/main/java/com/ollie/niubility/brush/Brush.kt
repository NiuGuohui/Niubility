package com.ollie.niubility.brush

import android.graphics.Canvas
import android.view.MotionEvent
import com.ollie.niubility.utils.Point

abstract class Brush {
  // 触控笔当前的正在绘制时的Id
  private var currentStylusPointerId: Int? = null

  fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.actionMasked) {
      MotionEvent.ACTION_DOWN -> {
        // 查找笔触
        currentStylusPointerId = findStylusPointerId(event)
        onActionDown(event)
      }

      MotionEvent.ACTION_MOVE -> {
        if (currentStylusPointerId == null) currentStylusPointerId = findStylusPointerId(event)
        onActionMove(event)
      }

      MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> onActionEnd(event)
    }

    return isDrawing
  }

  /**
   * 查找手写笔笔触
   */
  private fun findStylusPointerId(event: MotionEvent): Int? {
    for (p in 0 until event.pointerCount) {
      // 找到触控笔的pointer
      if (event.getToolType(p) == MotionEvent.TOOL_TYPE_STYLUS) return event.getPointerId(p)
    }
    return null
  }

  private fun getStylusIndex(event: MotionEvent) = currentStylusPointerId?.run { event.findPointerIndex(this) }

  protected val isDrawing: Boolean
    get() = currentStylusPointerId != null

  protected fun getStylusPoint(event: MotionEvent): Point? {
    getStylusIndex(event)?.run {
      return Point(event.getX(this), event.getY(this), event.getPressure(this))
    }
    return null
  }

  protected fun getHistoricalPoints(event: MotionEvent): Array<Point> {
    return getStylusIndex(event)?.let { index ->
      Array(event.historySize) {
        Point(
          event.getHistoricalX(index, it), event.getHistoricalY(index, it), event.getHistoricalPressure(index, it)
        )
      }
    } ?: emptyArray()
  }

  protected abstract fun onActionMove(event: MotionEvent)

  protected abstract fun onActionDown(event: MotionEvent)

  protected abstract fun onActionEnd(event: MotionEvent)

  abstract fun onDraw(canvas: Canvas)

  abstract fun release()
}
