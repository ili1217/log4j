package com.zhaoww.log4j

import android.os.Environment
import android.util.Log
import org.apache.log4j.Level
import java.io.File

internal class Log4jConfigure {

    companion object {

        private const val TAG = "Log4jConfigure"
        private fun log4jConfigure(fileName: String = WeConfig.wLogFileName) {
            val logConfigurator = LogConfigurator()
            // 创建日志文件 - 设置文件名
            if (isSdcardMounted()) {
                val file = File(WeConfig.wFileDirLogDaily)
                val mkdirs = createOrExistsDir(file)
                Log.i(
                    TAG,
                    "create dir -> $mkdirs file name ${WeConfig.wFileDirLogDaily + fileName}"
                )
                logConfigurator.fileName = WeConfig.wFileDirLogDaily + fileName
            } else {
                logConfigurator.fileName =
                    WeConfig.wApp?.getExternalFilesDir(null)!!.absolutePath + File.separator + fileName
            }
            Log.i(TAG, "file name -> ${logConfigurator.fileName}")
            // 设置root日志输出级别 默认为DEBUG
            logConfigurator.setRootLevel(Level.DEBUG)
            // 设置日志输出级别
            logConfigurator.setLevel("org.apache", Level.INFO)
            //以下设置是按指定大小来生成新的文件
            logConfigurator.setMaxBackupSize(4)
            logConfigurator.setMaxFileSize(WeConfig.wMaxFileSize)
            //以下设置是按天生成新的日志文件,与以上两句互斥,MAX_FILE_SIZE将不在起作用
            //文件名形如 MyApp.log.2016-06-02,MyApp.log.2016-06-03
            logConfigurator.setUseDailyRollingFileAppender(true)
            // 以下为通用配置
            // 设置所有消息是否被立刻输出 默认为true,false 不输出
            logConfigurator.setImmediateFlush(true)
            logConfigurator.setRootLevel(Level.DEBUG)
            logConfigurator.setFilePattern("%d\t%p/%c:\t%m%n")
            //是否本地控制台打印输出 默认为true ，false不输出
            logConfigurator.setUseLogCatAppender(true)
            // 设置是否启用文件附加,默认为true。false为覆盖文件
            logConfigurator.setUseFileAppender(true)
            logConfigurator.configure()
            Log.i(TAG, "Log4j config finish")
        }

        fun configure() {
            log4jConfigure()
        }

        private fun isSdcardMounted(): Boolean {
            return (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()).also {
                Log.d(TAG, "isSdcardMounted: $it")
            }
        }

        private fun createOrExistsDir(file: File?): Boolean {
            return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
        }
    }

}