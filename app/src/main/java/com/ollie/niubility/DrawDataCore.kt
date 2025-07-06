package com.ollie.niubility

import com.ollie.niubility.command.Command
import com.ollie.niubility.shape.StylusShape
import com.ollie.niubility.utils.ObserveList

class DrawDataCore {
  // 命令历史记录
  private val history = mutableListOf<Command>()
  // 回做堆栈
  private val redoHistory = mutableListOf<Command>()
  // 图形存储列表
  val shapeList = ObserveList<StylusShape>()
  // 获取当前命令记录，是否有可撤销的命令
  val undoState get() = history.isNotEmpty()
  // 获取当前命令记录，是否有可重做的命令
  val redoState get() = redoHistory.isNotEmpty()

  /**
   * 执行命令
   */
  fun executeCommand(command: Command) {
    synchronized(shapeList) {
      if (command.execute(shapeList)) {
        history.add(command)
        redoHistory.clear()
      }
    }
  }

  /**
   * 撤销
   */
  fun undo() {
    if (undoState) {
      synchronized(shapeList) {
        val command = history.removeAt(history.lastIndex)
        command.undo(shapeList)
        redoHistory.add(command)
      }
    }
  }

  /**
   * 重做
   */
  fun redo() {
    if (redoState) {
      synchronized(shapeList) {
        val command = redoHistory.removeAt(redoHistory.lastIndex)
        command.execute(shapeList)
        history.add(command)
      }
    }
  }

  fun reset() {
    history.clear()
    redoHistory.clear()
    synchronized(shapeList) { shapeList.clear() }
  }
}