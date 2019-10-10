package com.fukazayo.geoslacker.common

import android.text.format.DateFormat
import com.fukazayo.geoslacker.BuildConfig
import timber.log.Timber
import java.io.File
import java.util.*

object Logger {
    var logFile: File? = null

    fun writeFile(message: String?) {
        logFile?.appendText("${DateFormat.format("yyyy-MM-dd HH:mm:ss", Date())} $message\n")
    }

    fun initTree() = Timber.plant(if (BuildConfig.DEBUG) DebugTree else ReleaseTree)

    fun d(msg: String) = Timber.d(msg)

    fun i(msg: String) = Timber.i(msg)

    fun w(msg: String) = Timber.w(msg)

    fun w(msg: String, t: Throwable) = Timber.w(t, msg)

    fun w(t: Throwable?) = Timber.w(t)

    fun e(msg: String) = Timber.e(msg)

    fun e(msg: String, t: Throwable) = Timber.e(t, msg)

    fun e(t: Throwable) = Timber.e(t)
}
