package com.fukazayo.geoslacker.common

import android.annotation.SuppressLint
import android.util.Log
import com.fukazayo.geoslacker.common.Logger.writeFile
import timber.log.Timber
import java.io.PrintWriter
import java.io.StringWriter
import java.util.regex.Pattern

private const val CALLER_STACK_TRACE = 10
private val LOG_TAG_DEFAULT = Logger::class.java.name
private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")

private val callerClass: String
    get() {
        val stackTraceElements = Thread.currentThread().stackTrace
        return if (stackTraceElements != null && stackTraceElements.size >= CALLER_STACK_TRACE) {
            Thread.currentThread().stackTrace[CALLER_STACK_TRACE].className
                .let { tag -> tag.substring(tag.lastIndexOf('.') + 1) }
                .let { tag ->
                    val m = ANONYMOUS_CLASS.matcher(tag)
                    if (m.find()) m.replaceAll("") else tag
                }
        } else {
            LOG_TAG_DEFAULT
        }
    }

object DebugTree : Timber.DebugTree() {
    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, _tag: String?, msg: String, t: Throwable?) {
        val tag = callerClass

        when (priority) {
            Log.DEBUG -> Log.d(tag, msg, t)
            Log.INFO -> Log.i(tag, msg, t)
            Log.WARN -> Log.w(tag, msg, t)
            Log.ERROR -> {
                Log.e(tag, msg, t)
            }
            else -> Log.wtf(tag, msg, t)
        }

        writeFile(msg)
        t?.let { writeFile(it.stackTraceString()) }
    }

    private fun Throwable.stackTraceString(): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        this.printStackTrace(pw)
        return sw.toString()
    }
}

object ReleaseTree : Timber.Tree() {
    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, _tag: String?, msg: String, t: Throwable?) {
    }
}
