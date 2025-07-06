package com.ollie.niubility.command

import com.ollie.niubility.shape.StylusShape

/**
 * 清空命令
 */
class CleanCommand() : Command() {
  private val saveList = mutableListOf<StylusShape>()

  override fun undo(list: ArrayList<StylusShape>) {
    list.addAll(saveList)
    saveList.clear()
  }

  override fun execute(list: ArrayList<StylusShape>): Boolean {
    saveList.addAll(list)
    list.clear()
    return true
  }
}
