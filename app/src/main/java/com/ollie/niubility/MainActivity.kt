package com.ollie.niubility

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.net.toUri
import com.ollie.niubility.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    binding = ActivityMainBinding.inflate(layoutInflater, null, false)
    setContentView(binding.root)

    ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }

    // 悬浮权限
    binding.permissionFloatWindow.setPermissionName(getString(R.string.permission_float_window))
    binding.permissionFloatWindow.setOnClickListener {
      val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, "package:$packageName".toUri())
      startActivity(intent)
    }
  }

  override fun onResume() {
    super.onResume()
    binding.permissionFloatWindow.visibility = if (!Settings.canDrawOverlays(this)) View.VISIBLE else View.GONE
  }
}