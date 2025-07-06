package com.ollie.niubility

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ollie.niubility.brush.Brush

class DrawView(context: Context, attributes: AttributeSet) : View(context, attributes) {
  // 命令模式
  val core = DrawDataCore()
  // 当前笔刷
  var brush: Brush? = null
    set(value) {
      field?.release()
      field = value
    }

  init {
    setBackgroundColor(Color.TRANSPARENT)
    core.shapeList.addUpdateListener { postInvalidateOnAnimation() }
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    postInvalidateOnAnimation()
    return brush?.onTouchEvent(event) ?: false
  }

  override fun onDraw(canvas: Canvas) {
    // 先清空画布
    cleanCanvas(canvas)
    // 绘制图形，通过加锁防止出现线程间的竞态修改
    synchronized(core.shapeList) { core.shapeList.forEach { it.draw(canvas) } }
    // 调用当前笔刷的绘制方法
    brush?.onDraw(canvas)
  }

  fun cleanCanvas(canvas: Canvas) {
    canvas.drawColor(Color.TRANSPARENT)
  }
}
