package com.ollie.niubility.utils

import java.util.Comparator
import java.util.function.Predicate
import java.util.function.UnaryOperator

/**
 * 将MutableList转化为可观察List
 */
class ObserveList<T>() : ArrayList<T>() {
  private val updateListener = HashSet<(it: ArrayList<T>) -> Unit>()

  private fun notifyChange() = updateListener.forEach { it.invoke(this) }

  override fun add(element: T) = super.add(element).apply { notifyChange() }

  override fun addAll(elements: Collection<T>) = super.addAll(elements).apply { notifyChange() }

  override fun add(index: Int, element: T) = super.add(index, element).apply { notifyChange() }

  override fun addAll(index: Int, elements: Collection<T>): Boolean {
    return super.addAll(index, elements).apply { notifyChange() }
  }

  override fun set(index: Int, element: T) = super.set(index, element).apply { notifyChange() }

  override fun remove(element: T) = super.remove(element).apply { notifyChange() }

  override fun removeAt(index: Int) = super.removeAt(index).apply { notifyChange() }

  override fun removeAll(elements: Collection<T>) = super.removeAll(elements).apply { notifyChange() }

  override fun replaceAll(operator: UnaryOperator<T>) {
    super.replaceAll(operator).apply { notifyChange() }
  }


  override fun removeIf(filter: Predicate<in T>) = super.removeIf(filter).apply { notifyChange() }

  override fun removeRange(fromIndex: Int, toIndex: Int) {
    super.removeRange(fromIndex, toIndex).apply { notifyChange() }
  }

  override fun clear() = super.clear().apply { notifyChange() }

  override fun sort(c: Comparator<in T>?) = super.sort(c).apply { notifyChange() }

  fun addUpdateListener(listener: (it: ArrayList<T>) -> Unit): () -> Unit {
    updateListener.add(listener)
    return { updateListener.remove(listener) }
  }

  fun removeAllUpdateListeners() {
    updateListener.clear()
  }
}
