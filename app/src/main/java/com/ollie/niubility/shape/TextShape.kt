package com.ollie.niubility.shape

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import com.ollie.niubility.utils.Point
import kotlin.math.absoluteValue

open class TextShape(var text: String, initialColor: Int = Color.RED, initialSize: Float = 48f) : StylusShape() {
  private val paint = Paint().apply {
    isDither = true
    isAntiAlias = true
    color = initialColor
    textSize = initialSize
    textAlign = Paint.Align.LEFT
    typeface = Typeface.SANS_SERIF
  }

  var color: Int = initialColor
    set(value) {
      field = value
      paint.color = value
    }

  var size: Float = initialSize
    set(value) {
      field = value
      paint.textSize = value
    }


  private fun set(point: Point) {
    val bounds = Rect()
    paint.getTextBounds(text, 0, text.length, bounds)
    val w = bounds.width().absoluteValue / 2
    val h = bounds.height().absoluteValue / 2
    // 注意，文字绘制基线是在文字的左下角
    matrix.setTranslate(point.x - w, point.y + h)
  }

  override fun onCreate(point: Point) = set(point)
  override fun onReceivePoint(point: Point, assistantPoints: Array<Point>) = set(point)

  override fun draw(canvas: Canvas) {
    // 注意，文字绘制基线是在文字的左下角
    val (x, y, scale) = getXY()
    canvas.drawText(text, x, y, Paint(paint).apply { textSize *= scale })
  }

  override fun shapeSize(): RectF {
    val temp = Rect()
    paint.getTextBounds(text, 0, text.length, temp)
    val (x, y) = getXY()
    return RectF(x, y, x + temp.width(), y + temp.height())
  }

  private fun getXY(): Triple<Float, Float, Float> {
    val v = FloatArray(9)
    matrix.getValues(v)
    val x = v[Matrix.MTRANS_X] * v[Matrix.MSCALE_X]
    val y = v[Matrix.MTRANS_Y] * v[Matrix.MSCALE_Y]
    val scale = v[Matrix.MSCALE_X]
    return Triple(x, y, scale)
  }

}
