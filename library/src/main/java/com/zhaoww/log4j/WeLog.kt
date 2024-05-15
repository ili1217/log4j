package com.zhaoww.log4j

object WeLog {

    private const val logFlag = true
    // private final static boolean LOG_PERMISSION = XXPermissions.isGranted(Utils.getApp(), "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE");

    @JvmStatic
    fun info(str: String?) {
        if (logFlag) {
            val name = getFunctionName()
            Log4j.info(name, str)
        }
    }

    @JvmStatic
    fun debug(str: String?) {
        if (logFlag) {
            val name = getFunctionName()
            Log4j.debug(name, str)
        }
    }

    @JvmStatic
    fun warn(str: String?) {
        if (logFlag) {
            val name = getFunctionName()
            Log4j.warn(name, str)
        }
    }

    @JvmStatic
    fun trace(str: String?) {
        if (logFlag) {
            val name = getFunctionName()
            Log4j.trace(name, str)
        }
    }

    @JvmStatic
    fun error(str: String?) {
        if (logFlag) {
            val name = getFunctionName()
            Log4j.error(name, str)
        }
    }

    @JvmStatic
    fun error(str: String?, exception: Throwable?) {
        if (logFlag) {
            val name = getFunctionName()
            Log4j.error(name, str, exception)
        }
    }

    /**
     * 获取方法名
     */
    private fun getFunctionName(): String {
        val sts = Thread.currentThread().stackTrace
        for (st in sts) {
            if (st.isNativeMethod) {
                continue
            }
            if (st.className == Thread::class.java.name) {
                continue
            }
            if (st.className == WeLog::class.java.name) {
                continue
            }
            return ("[ "
                    + Thread.currentThread().name + ": "
                    + st.fileName.replace(".java", "") + ":" + st.lineNumber + " ]")
        }
        return ""
    }
}