package com.ollie.niubility.utils

import android.util.Log
import com.ollie.niubility.utils.Point
import kotlin.math.abs


const val DEFAULT_SMOOTH_VALUE = 038f

/**
 * 获取临时点：用于计算贝塞尔曲线控制点
 * 实现方案：曲线拟合算法,正弦函数
 * 注意：参数的参数，是有一定的方向性的
 *
 * @param pointA
 * @param pointB
 * @param smoothValue 平滑因子，值越大，曲线越平滑（取值范围0-1） 其实就是在AB两点连线之间有一个点(点C)将AB线段分成两段，BC/AC=smoothVaule
 * @return
 */
fun calculateTempPoint(pointA: Point, pointB: Point, smoothValue: Float): Point {
  var x = 0.0f
  var y = 0.0f
  try {
    val ab: Float = pointsDistance(pointA, pointB)
    val ac: Float = ab * (1 - smoothValue)
    val distanceY: Float = abs(pointA.y - pointB.y)
    val tempDistanceY: Float = (distanceY * ac) / ab

    // A点在B点下方
    y = if (pointA.y >= pointB.y) pointA.y - tempDistanceY else pointA.y + tempDistanceY

    val tempDistanceX: Float = (abs(pointA.x - pointB.x) * ac) / ab
    // A点在B点左方
    x = if (pointA.x <= pointB.x) pointA.x + tempDistanceX else pointA.x - tempDistanceX
  } catch (e: Exception) {
    Log.e("Niu", "构建贝塞尔曲线时，获取控制点所需的临时点，发生错误")
  }
  return Point(x, y, 0f)
}

/**
 * 获取两个控制点
 * 计算控制点：根据有效的3个事件点，根据曲线拟合算法，分别计算出AB段、BC段的贝塞尔曲线控制点
 *
 * @param pointA 起始点
 * @param pointB 中间点
 * @param pointC 尾部点
 * @param smoothValue
 * @return
 */
fun calculateControl(
  pointA: Point,
  pointB: Point,
  pointC: Point,
  smoothValue: Float = DEFAULT_SMOOTH_VALUE
): ArrayList<Point> {
  val result: ArrayList<Point> = ArrayList()
  try {
    val tempAB: Point = calculateTempPoint(pointA, pointB, smoothValue)
    val tempCB: Point = calculateTempPoint(pointC, pointB, smoothValue)

    val tempAB_CB: Point = calculateTempPoint(tempCB, tempAB, smoothValue)
    // 点tempAB_CB平移到点pointB,计算X、Y方向的矢量差值
    val tempX: Float = pointB.x - tempAB_CB.x
    val tempY: Float = pointB.y - tempAB_CB.y
    result.add(Point(tempAB.x + tempX, tempAB.y + tempY, 0f))
    result.add(Point(tempCB.x + tempX, tempCB.y + tempY, 0f))
  } catch (e: Exception) {
    Log.e("Niu", "计算控制点发生错误")
  }
  return result
}
