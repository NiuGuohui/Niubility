package com.ollie.niubility.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ollie.niubility.databinding.PermissionItemBinding

class PermissionSettingItem(context: Context, attributeSet: AttributeSet? = null) :
  ConstraintLayout(context, attributeSet) {
  private val binding = PermissionItemBinding.inflate(LayoutInflater.from(context), this, true)


  fun setPermissionName(name: String) {
    binding.permissionItemTitle.text = name
  }

  fun setOnAuthorizeClickListener(listener: OnClickListener) {
    binding.permissionItemBtn.setOnClickListener(listener)
  }
}