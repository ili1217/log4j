package com.zhaoww.log4j

import android.text.TextUtils
import org.apache.log4j.Logger

internal object Log4j {

    init {
        Log4jConfigure.configure()
    }

    private var switchLog = true
    private var LOG4J_TAG = "--- ${Log4j::class.simpleName} ---"

    @JvmStatic
    fun setSwitchLog(switchLog: Boolean) {
        Log4j.switchLog = switchLog
    }

    @JvmStatic
    fun isSwitchLog(): Boolean {
        return switchLog
    }

    @JvmStatic
    fun debug(message: String?) {
        debug(LOG4J_TAG, message)
    }

    @JvmStatic
    fun debug(tag: String, message: String?) {
        if (switchLog) {
            getLogger(tag).debug(message)
        }
    }

    @JvmStatic
    fun debug(tag: String, message: String?, exception: Throwable?) {
        if (switchLog) {
            getLogger(tag).debug(message, exception)
        }
    }

    @JvmStatic
    fun info(message: String?) {
        info(LOG4J_TAG, message)
    }

    @JvmStatic
    fun info(tag: String, message: String?) {
        if (switchLog) {
            getLogger(tag).info(message)
        }
    }

    @JvmStatic
    fun info(tag: String, message: String?, exception: Throwable?) {
        if (switchLog) {
            getLogger(tag).info(message, exception)
        }
    }

    @JvmStatic
    fun warn(message: String?) {
        warn(LOG4J_TAG, message)
    }

    @JvmStatic
    fun warn(tag: String, message: String?) {
        if (switchLog) {
            getLogger(tag).warn(message)
        }
    }

    @JvmStatic
    fun warn(tag: String, message: String?, exception: Throwable?) {
        if (switchLog) {
            getLogger(tag).warn(message, exception)
        }
    }

    @JvmStatic
    fun error(message: String?) {
        error(LOG4J_TAG, message)
    }

    @JvmStatic
    fun error(tag: String, message: String?) {
        if (switchLog) {
            getLogger(tag).error(message)
        }
    }

    @JvmStatic
    fun error(tag: String, message: String?, exception: Throwable?) {
        if (switchLog) {
            getLogger(tag).error(message, exception)
        }
    }

    @JvmStatic
    fun trace(message: String?) {
        trace(LOG4J_TAG, message)
    }

    @JvmStatic
    fun trace(tag: String, message: String?) {
        if (switchLog) {
            getLogger(tag).trace(message)
        }
    }

    @JvmStatic
    fun trace(tag: String, message: String?, exception: Throwable?) {
        if (switchLog) {
            getLogger(tag).trace(message, exception)
        }
    }

    private fun getLogger(tag: String): Logger {
        val logger: Logger = if (TextUtils.isEmpty(tag)) {
            Logger.getRootLogger()
        } else {
            Logger.getLogger(tag)
        }
        return logger
    }

}