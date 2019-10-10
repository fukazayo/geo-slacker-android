package com.fukazayo.geoslacker

import android.app.Application
import android.os.Build
import com.fukazayo.geoslacker.common.Logger
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Logger.logFile = File(this.getExternalFilesDir(null), "Logcat-${now()}.txt")
            .apply { if (!exists()) createNewFile() }
        Logger.initTree()

        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) packageInfo.longVersionCode else packageInfo.versionCode.toLong()
        val versionName = "${packageInfo.versionName} (${versionCode})"
        Logger.i("App is launched: $versionName")
    }

    private fun now() = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.JAPAN).format(Date().time)
}
