package com.ollie.niubility.shape

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.shapes.Shape
import com.ollie.niubility.utils.Point
import java.util.Objects
import kotlin.math.absoluteValue

abstract class StylusShape : Shape() {
  val matrix = Matrix()

  open fun onCreate(point: Point) {}

  open fun intersect(target: RectF) = target.intersect(shapeSize())

  open fun onReceivePoint(point: Point, assistantPoints: Array<Point>) {}

  abstract fun draw(canvas: Canvas)

  abstract fun shapeSize(): RectF

  final override fun draw(canvas: Canvas, paint: Paint) {
    shapeSize().let {
      resize(it.width().absoluteValue, it.height().absoluteValue)
      draw(canvas)
    }
  }

  final override fun equals(other: Any?): Boolean {
    return this === other
  }

  override fun hashCode(): Int {
    return Objects.hash(shapeSize(), javaClass)
  }
}
