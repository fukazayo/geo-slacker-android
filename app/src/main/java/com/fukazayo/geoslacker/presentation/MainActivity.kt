package com.fukazayo.geoslacker.presentation

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fukazayo.geoslacker.R
import permissions.dispatcher.*

@RuntimePermissions
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

        grantedWithPermissionCheck()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun granted() {
        // Nothing to do.
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
}
