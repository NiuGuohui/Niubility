package com.ollie.niubility

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.customview.widget.ViewDragHelper
import com.ollie.niubility.databinding.OverlayBinding
import kotlin.math.max
import kotlin.math.min


class NiubilityOverlay(context: Context) : FrameLayout(context) {
  val binding = OverlayBinding.inflate(LayoutInflater.from(context), this, true)

  val dragHelper: ViewDragHelper = ViewDragHelper.create(binding.root, 1.0f, object : ViewDragHelper.Callback() {
    override fun tryCaptureView(child: View, pointerId: Int): Boolean {
      return child.id == R.id.draw_control_panel
    }

    override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
      return max(0, min(left, width - child.width))
    }

    override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
      // 限制子 View 的垂直位置
      return max(0, min(top, height - child.height))
    }

    override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
      // 释放时的回调，可以实现自动吸附逻辑
      var finalLeft = releasedChild.left
      finalLeft = if (finalLeft < width / 2) 0 else width - releasedChild.width
      dragHelper.settleCapturedViewAt(finalLeft, releasedChild.top)
      invalidate()
    }
  })

  init {
    isRunning = true

    binding.drawControlPanel.setDrawView(binding.drawView)
    binding.drawControlPanel.setOnCloseClickListener { close() }

  }

  override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    return dragHelper.shouldInterceptTouchEvent(ev)
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    dragHelper.processTouchEvent(event)
    return true
  }

  override fun computeScroll() {
    if (dragHelper.continueSettling(true)) {
      invalidate()
    }
  }

  fun close() {
    try {
      (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?)?.removeViewImmediate(this)
    } catch (e: Exception) {
      visibility = GONE
    }
    isRunning = false
  }

  companion object {
    var isRunning = false
  }
}