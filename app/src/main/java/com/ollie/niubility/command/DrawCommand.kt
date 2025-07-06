package com.ollie.niubility.command

import com.ollie.niubility.shape.StylusShape

/**
 * 绘图命令
 */
class DrawCommand(private val shape: StylusShape) : Command() {
  override fun undo(list: ArrayList<StylusShape>) {
    list.remove(shape)
  }

  override fun execute(list: ArrayList<StylusShape>): Boolean {
    list.add(shape)
    return true
  }
}
