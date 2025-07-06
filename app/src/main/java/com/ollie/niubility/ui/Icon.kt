package com.ollie.niubility.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.ollie.niubility.R


open class Icon(context: Context, attr: AttributeSet? = null) : AppCompatTextView(context, attr) {

  init {
    isClickable = true
    setTypeface(ResourcesCompat.getFont(context, R.font.niubility))

    setOnHoverListener { v, event ->
      when (event.action) {
        MotionEvent.ACTION_HOVER_ENTER ->
          v.isHovered = true

        MotionEvent.ACTION_HOVER_EXIT ->
          v.isHovered = false
      }
      return@setOnHoverListener false
    }
  }
}