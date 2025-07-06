package com.ollie.niubility.command

import com.ollie.niubility.shape.StylusShape
import kotlin.properties.Delegates

/**
 * 移除指定shape命令
 */
class RemoveCommand(val shape: StylusShape) : Command() {
  private var index by Delegates.notNull<Int>()

  override fun undo(list: ArrayList<StylusShape>) {
    if (index >= 0) list.add(index, shape)
  }

  override fun execute(list: ArrayList<StylusShape>): Boolean {
    index = list.indexOf(shape)
    if (index >= 0) list.removeAt(index)
    return true
  }
}
