package com.ollie.niubility.brush

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import com.ollie.niubility.DrawDataCore
import com.ollie.niubility.command.RemoveCommand
import com.ollie.niubility.shape.StylusShape
import com.ollie.niubility.utils.Point
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors

open class StrokeEraserBrush(private val core: DrawDataCore) : Brush() {
  // 当前正在画的点
  protected var current: Point? = null
  private val detectThread = Executors.newCachedThreadPool()
  // 待移除的图形(使用CopyOnWriteArrayList保证线程安全)
  private val waitToRemove = CopyOnWriteArrayList<StylusShape>()

  // 橡皮直径
  var size = 40f

  override fun onActionMove(event: MotionEvent) {
    getStylusPoint(event)?.let { point ->
      current = point
      val list = listOf(point, *getHistoricalPoints(event))
      // 在异步线程中执行检测逻辑，防止阻塞主线程
      detectThread?.execute {
        list.forEach { point ->
          val radius = size / 2
          // 将当前点转化为矩形，创建橡皮矩形
          val current = RectF(point.x - radius, point.y - radius, point.x + radius, point.y + radius)
          // 通过加锁复制一份图形列表，最小化锁的锁定时间
          synchronized(core.shapeList) { core.shapeList.clone() as ArrayList<StylusShape> }
            // 遍历当前图形列表，查找有可能会被擦除的图形
            .forEach {
              // 只计算相交矩形中的图形，防止无用的计算
              if (it.intersect(current)) waitToRemove.add(it)
            }
          // 这里使用锁是因为执行命令必须要按顺序发送;
          // 理论上在并发执行时只会有一个会执行发送，因为执行完毕就清空waitToRemove了
          synchronized(waitToRemove) {
            if (waitToRemove.isNotEmpty()) {
              // 在计算完毕后，统一执行移除，防止遇到多线程修改List的错误
              waitToRemove.forEach { core.executeCommand(RemoveCommand(it)) }
              waitToRemove.clear()
            }
          }
        }
      }
    }
  }

  override fun onActionDown(event: MotionEvent) {
    current = getStylusPoint(event)
  }

  override fun onActionEnd(event: MotionEvent) {
    current = null
  }

  private val paint = Paint().apply {
    isAntiAlias = true
    color = Color.BLACK
  }

  override fun onDraw(canvas: Canvas) {
    current?.let {
      paint.alpha = 10
      paint.strokeWidth = 0f
      paint.style = Paint.Style.FILL
      canvas.drawCircle(it.x , it.y, size / 2, paint)
      paint.alpha = 60
      paint.strokeWidth = 3f
      paint.style = Paint.Style.STROKE
      canvas.drawCircle(it.x, it.y , size / 2, paint)
    }
  }

  override fun release() {
    current = null
    detectThread.close()
  }
}