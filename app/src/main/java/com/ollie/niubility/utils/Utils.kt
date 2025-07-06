package com.ollie.niubility.utils

import com.ollie.niubility.utils.Point
import kotlin.math.sqrt

/**
 * 点位距离，直接根据勾股定理即可
 */
fun pointsDistance(point1: Point, point2: Point): Float {
  val dx = point1.x.toDouble() - point2.x.toDouble()
  val dy = point1.y.toDouble() - point2.y.toDouble()
  return sqrt(dx * dx + dy * dy).toFloat()
}
