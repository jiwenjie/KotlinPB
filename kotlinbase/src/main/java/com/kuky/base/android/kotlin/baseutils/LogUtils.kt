package com.kuky.base.android.kotlin.baseutils

import android.util.Log

/**
 * @author kuky.
 * @description
 */
object LogUtils {
    private var className: String? = null
    private var methodName: String? = null
    private var lineNumber: Int? = null

    private fun isDebuggable(): Boolean = true

    private fun createLog(logMsg: String): String {
        return "$methodName($className:$lineNumber): $logMsg"
    }

    private fun getMethodName(throwable: Throwable) {
        className = throwable.stackTrace[1].fileName
        methodName = throwable.stackTrace[1].methodName
        lineNumber = throwable.stackTrace[1].lineNumber
    }

    @JvmStatic
    fun e(msg: String) {
        if (!isDebuggable()) return
        getMethodName(Throwable())
        Log.e(className, createLog(msg))
    }

    @JvmStatic
    fun w(msg: String) {
        if (!isDebuggable()) return
        getMethodName(Throwable())
        Log.w(className, createLog(msg))
    }

    @JvmStatic
    fun i(msg: String) {
        if (!isDebuggable()) return
        getMethodName(Throwable())
        Log.i(className, createLog(msg))
    }

    @JvmStatic
    fun d(msg: String) {
        if (!isDebuggable()) return
        getMethodName(Throwable())
        Log.d(className, createLog(msg))
    }

    @JvmStatic
    fun v(msg: String) {
        if (!isDebuggable()) return
        getMethodName(Throwable())
        Log.v(className, createLog(msg))
    }
}