package com.ollie.niubility.utils

import java.io.Serializable

/**
 * 点位数据
 */
data class Point(val x: Float, val y: Float, val pressure: Float) : Serializable {
  override fun hashCode() = "$x,$y,$pressure".hashCode()

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Point
    return !(x != other.x || y != other.y || pressure != other.y)
  }
}