package com.ollie.niubility.brush

import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent
import com.ollie.niubility.DrawDataCore
import com.ollie.niubility.command.DrawCommand
import com.ollie.niubility.shape.StrokeShape

open class BallPenBrush(private val core: DrawDataCore) : Brush() {
  // 当前正在画的
  protected var current: StrokeShape? = null

  var strokeWidth = 2f
  var color = Color.RED

  override fun onActionMove(event: MotionEvent) {
    current?.let { shape ->
      getStylusPoint(event)?.let { point ->
        // 触发图形的数据接收
        shape.onReceivePoint(point, getHistoricalPoints(event))
      }
    }
  }

  override fun onActionDown(event: MotionEvent) {
    if (isDrawing) {
      getStylusPoint(event)?.let {
        current = StrokeShape(strokeWidth, color)
        current?.onCreate(it)
      }
    }
  }

  override fun onActionEnd(event: MotionEvent) {
    current?.let { shape ->
      // 将最后一个点加入
      getStylusPoint(event)?.let { shape.onReceivePoint(it, emptyArray()) }
      // 入栈绘图命令
      core.executeCommand(DrawCommand(shape))
    }
    current = null
  }

  override fun onDraw(canvas: Canvas) {
    current?.draw(canvas)
  }

  override fun release() {
    current = null
  }
}