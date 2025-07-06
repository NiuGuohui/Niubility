package com.ollie.niubility.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import com.ollie.niubility.DrawView
import com.ollie.niubility.R
import com.ollie.niubility.brush.BallPenBrush
import com.ollie.niubility.brush.StrokeEraserBrush
import com.ollie.niubility.command.CleanCommand
import com.ollie.niubility.databinding.DrawControlPanelBinding


class DrawControlPanel(context: Context, attributeSet: AttributeSet? = null) : FrameLayout(context, attributeSet) {
  private lateinit var drawView: DrawView
  private var unSubscribeShapesChange: (() -> Unit)? = null
  private val binding = DrawControlPanelBinding.inflate(LayoutInflater.from(context), this, true)

  private var retract = false

  val ballPenBrush = BrushItem(context, R.string.icon_ball_pen)
  val eraserBrush = BrushItem(context, R.string.icon_eraser)
  val cleanBrush = BrushItem(context, R.string.icon_clean).apply {
    setOnClickListener {
      if (drawView.core.shapeList.isNotEmpty()) drawView.core.executeCommand(CleanCommand())
    }
  }

  init {
    translationX = -28f

    binding.retract.setOnClickListener {
      retract = !retract
      if (retract) {
        binding.content.visibility = GONE
        translationX = 0f
      } else {
        binding.content.visibility = VISIBLE
        translationX = -28f
      }
    }
    binding.undo.setOnClickListener { drawView.core.undo() }
    binding.redo.setOnClickListener { drawView.core.redo() }
    ballPenBrush.setOnClickListener {
      ballPenBrush.isSelected = true
      drawView.brush = BallPenBrush(drawView.core).apply {
        strokeWidth = 5f
      }
      eraserBrush.isSelected = false
    }
    eraserBrush.setOnClickListener {
      eraserBrush.isSelected = true
      drawView.brush = StrokeEraserBrush(drawView.core)
      ballPenBrush.isSelected = false
    }

    binding.brushList.addView(ballPenBrush)
    binding.brushList.addView(eraserBrush)
    binding.brushList.addView(cleanBrush)
  }

  fun setOnCloseClickListener(listener: OnClickListener) = binding.close.setOnClickListener(listener)

  fun setDrawView(view: DrawView) {
    drawView = view
    unSubscribeShapesChange = drawView.core.shapeList.addUpdateListener {
      post {
        binding.undo.isEnabled = drawView.core.undoState
        if (drawView.core.undoState) {
          binding.undo.alpha = 1f
          binding.undo.isClickable = true
        } else {
          binding.undo.alpha = 0.5f
          binding.undo.isClickable = false
        }
        binding.redo.isEnabled = drawView.core.redoState
        if (drawView.core.redoState) {
          binding.redo.alpha = 1f
          binding.redo.isClickable = true
        } else {
          binding.redo.alpha = 0.5f
          binding.redo.isClickable = false
        }
      }
    }
    drawView.core.shapeList.clear()
    ballPenBrush.performClick()
  }

  override fun onTouchEvent(ev: MotionEvent): Boolean {
    return true
  }
}