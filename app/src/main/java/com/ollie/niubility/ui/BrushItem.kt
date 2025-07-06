package com.ollie.niubility.ui

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.ollie.niubility.R

class BrushItem(context: Context, @StringRes icon: Int) : Icon(context) {
  private var selected: Boolean = false

  init {
    setBackgroundResource(R.drawable.touchable_bg)
    setText(icon)
    textSize = 20f
    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    gravity = Gravity.CENTER
    setPadding(0, 32, 0, 32)
  }

  override fun isSelected(): Boolean = selected

  override fun setSelected(value: Boolean) {
    selected = value
    if (value) {
      setBackgroundResource(R.drawable.selected_touchable_bg)
      setTextColor(ResourcesCompat.getColor(resources, android.R.color.white, context.theme))
    } else {
      setBackgroundResource(R.drawable.touchable_bg)
      setTextColor(TextView(context).currentTextColor)
    }
  }
}