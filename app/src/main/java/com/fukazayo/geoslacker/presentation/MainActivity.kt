package com.fukazayo.geoslacker.presentation

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fukazayo.geoslacker.R
import com.fukazayo.geoslacker.common.Constants
import permissions.dispatcher.*

@RuntimePermissions
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) packageInfo.longVersionCode else packageInfo.versionCode.toLong()
        val versionName = "${packageInfo.versionName} (${versionCode})"
        title = "$title $versionName"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            addNotificationChannel()
        }

        grantedWithPermissionCheck()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun granted() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MainFragment.newInstance())
            .commitNow()
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onPermissionDenied() {
        Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show()
        finish()
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onShowRationale(request: PermissionRequest) {
        showRationaleDialog(R.string.permission_rationale, request)
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onNeverAskAgain() {
        Toast.makeText(this, R.string.permission_never_ask_again, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun showRationaleDialog(messageResId: Int, request: PermissionRequest) {
        AlertDialog.Builder(this)
            .setPositiveButton(R.string.permission_allow) { _, _ -> request.proceed() }
            .setNegativeButton(R.string.permission_deny) { _, _ -> request.cancel() }
            .setCancelable(false)
            .setMessage(messageResId)
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addNotificationChannel() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            "Notification",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        channel.enableLights(true)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

        manager.createNotificationChannel(channel)
    }
}
