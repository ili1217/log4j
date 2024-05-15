package com.zhaoww.log4j

import android.app.Application
import android.os.Environment
import java.io.File

object WeConfig {

    private val FILE_ROOT: String =
        Environment.getExternalStorageDirectory().absolutePath + File.separator

    /** 设置日志文件的路径 */
    var wFileDirLogDaily: String = FILE_ROOT + "daily/"

    /** 设置日志文件名 */
    @JvmStatic
    var wLogFileName = "we_test.log"

    /** 设置日志文件在滚动之前的最大大小 */
    @JvmStatic
    var wMaxFileSize: Long = 1024 * 1024 * 10

    var wApp :Application? = null
        get() {
            if (field == null) {
                throw NullPointerException("wApp is null")
            }
            return field
        }

    @JvmStatic
    fun init(application: Application, block: () -> Unit): WeConfig {
        wApp = application
        block()
        return this
    }

//    双重校验锁
//    companion object {
//        val instance: WwConfig by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
//            WlHelper()
//        }
//    }

//    静态内部类模式
//    companion object {
//        val instance = Holder.holder
//    }
//
//    private object Holder {
//        val holder = WwConfig()
//    }

//    线程安全的懒汉单例
//    companion object {
//        private var instance: WwConfig? = null
//            get() {
//                if (field == null) {
//                    field = WwConfig()
//                }
//                return field
//            }
//
//        //使用同步锁注解
//        @Synchronized
//        fun get(): WwConfig {
//            return instance!!
//        }
//    }

//    懒汉单例
//    companion object {
//        private var instance: WwConfig? = null
//            //这里使用的是自定义访问器
//            get() {
//                if (field == null) {
//                    field = WwConfig()
//                }
//                return field
//            }
//
//        fun get(): WwConfig {
//            return instance!!
//        }
//    }


}