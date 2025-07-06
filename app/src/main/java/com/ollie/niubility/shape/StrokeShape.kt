package com.ollie.niubility.shape

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.RectF
import com.ollie.niubility.utils.Point

open class StrokeShape(strokeWidth: Float = 2f, color: Int = Color.RED) : StylusShape() {
  protected val paint = Paint().apply {
    isDither = true
    isAntiAlias = true
    style = Paint.Style.STROKE
    strokeCap = Paint.Cap.ROUND
    strokeJoin = Paint.Join.ROUND
    this.strokeWidth = strokeWidth
    this.color = color
    isFilterBitmap = true
  }

  protected val path = Path()
  protected var lastPoint: Point? = null

  override fun onCreate(point: Point) {
    if (path.isEmpty) path.moveTo(point.x, point.y)
    lastPoint = point
  }

  override fun onReceivePoint(point: Point, assistantPoints: Array<Point>) {
    if (lastPoint != null) {
      val currentP = Point(
        (lastPoint!!.x + point.x) / 2, (lastPoint!!.y + point.y) / 2, (lastPoint!!.pressure + point.pressure) / 2
      )
      path.quadTo(lastPoint!!.x, lastPoint!!.y, currentP.x, currentP.y);
    }
    lastPoint = point
  }

  override fun draw(canvas: Canvas) {
    canvas.drawPath(Path(path).apply { transform(matrix) }, paint)
  }

  override fun shapeSize(): RectF {
    val bounds = RectF()
    path.computeBounds(bounds, true)
    return bounds
  }

  override fun intersect(target: RectF): Boolean {
    if (super.intersect(target)) {
      val r = RectF()
      val nPath = Path()
      val m = PathMeasure(path, false)
      if (m.length <= THRESHOLD && !path.isEmpty) return true
      do {
        for (i in 0 until m.length.toInt() step THRESHOLD) {
          nPath.reset()
          r.setEmpty()
          // 获取一小段路径
          m.getSegment(i.toFloat(), i + THRESHOLD.toFloat(), nPath, true)
          // 获取这一小段路径的矩形
          nPath.computeBounds(r, false)
          // 判断是否相交
          if (r.intersect(target)) return true
        }
      } while (m.nextContour())
    }
    return false
  }

  companion object {
    const val THRESHOLD = 10
  }
}
