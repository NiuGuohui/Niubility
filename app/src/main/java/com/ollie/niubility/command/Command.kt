package com.ollie.niubility.command

import com.ollie.niubility.shape.StylusShape


abstract class Command() {

  /**
   * 撤销命令
   */
  open fun undo(list: ArrayList<StylusShape>) {}

  /**
   * 命令执行
   * @return 根据返回的布尔值，决定这个命令是否可被撤销(undo)
   */
  abstract fun execute(list: ArrayList<StylusShape>): Boolean
}
