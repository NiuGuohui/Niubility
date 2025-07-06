package com.ollie.niubility

import android.app.AlertDialog
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import com.ollie.niubility.NiubilityOverlay


class NiubilityTileService : TileService() {

  override fun onStartListening() {
    qsTile?.apply {
      label = getString(R.string.app_name_short)
      qsTile?.state = Tile.STATE_INACTIVE
      icon = Icon.createWithResource(this@NiubilityTileService, R.drawable.ic_tile)
      qsTile?.updateTile()
    }
  }

  @Synchronized
  override fun onClick() {
    if (!Settings.canDrawOverlays(this)) {
      showDialog(AlertDialog.Builder(applicationContext).apply {
        setTitle("开启悬浮窗权限")
        setIcon(R.drawable.permission_warn)
        setMessage("请前往设置开启悬浮窗权限")
        setNeutralButton("取消") { _, _ -> }
        setPositiveButton(R.string.permission_auth) { _, _ ->
          val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, "package:$packageName".toUri())
          intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
          applicationContext.startActivity(intent)
        }
      }.create())
      return
    }
    if (!NiubilityOverlay.isRunning) {
      (getSystemService(WINDOW_SERVICE) as WindowManager?)?.let { windowManager ->
        val overlay = NiubilityOverlay(applicationContext)
        windowManager.addView(
          overlay, WindowManager.LayoutParams(
            windowManager.maximumWindowMetrics.bounds.width(),
            windowManager.maximumWindowMetrics.bounds.height(),
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
          ).apply {
            gravity = Gravity.START or Gravity.TOP
          })
      }
    }

    showDialog(FakeDialog(applicationContext))
  }

  class FakeDialog(context: Context) : Dialog(context, R.style.TransparentDialogTheme) {
    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

      window?.apply {
        attributes?.width = 1
        attributes?.height = 1
        attributes = attributes
        setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
      }

      dismiss()
    }
  }
}