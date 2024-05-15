package com.zhaoww.log4j

import org.apache.log4j.DailyRollingFileAppender
import org.apache.log4j.Layout
import org.apache.log4j.Level
import org.apache.log4j.LogManager
import org.apache.log4j.Logger
import org.apache.log4j.PatternLayout
import org.apache.log4j.RollingFileAppender
import org.apache.log4j.helpers.LogLog
import java.io.IOException

internal class LogConfigurator(
    var fileName: String = WeConfig.wLogFileName,
    // 设置root日志输出级别 默认为DEBUG
    private var rootLevel: Level = Level.DEBUG,
    /**
     * 设置 输出到日志文件的文字格式 默认 %d %-5p [%c{2}]-[%L] %m%n
     */
    private var filePattern: String = "%d - [%p::%c::%C] - %m%n",
    /**
     * 设置最大产生的文件个数
     */
    private var maxBackupSize: Int = 5,
    /**
     * 设置总文件大小 (1M)
     */
    private var maxFileSize: Long = (1024 * 1024 * 10).toLong()
) {

    /**
     * 设置输出到控制台的文字格式 默认%m%n
     */
    private var logCatPattern = "%m%n"

    /**
     * 设置所有消息是否被立刻输出 默认为true,false 不输出
     */
    private var immediateFlush = true

    /**
     * 是否本地控制台打印输出 默认为true ，false不输出
     */
    private var useLogCatAppender = true

    /**
     * 设置是否启用文件附加,默认为true。false为覆盖文件
     */
    private var useFileAppender = true

    /**
     * 设置是否重置配置文件，默认为true
     */
    private var resetConfiguration = true

    /**
     * 是否显示内部初始化日志,默认为false
     */
    private var internalDebugging = false

    /**
     * 以下设置是按天生成新的日志文件
     * 与以上两句互斥,MAX_FILE_SIZE将不在起作用
     */
    private var useDailyRollingFileAppender = false

    /**
     * 日志生成文件添加的拓展名
     */
    private val DATE_PATTERN = "'-'yyyy-MM-dd"

    fun configure() {
        val root: Logger = Logger.getRootLogger()
        if (isResetConfiguration()) {
            LogManager.getLoggerRepository().resetConfiguration()
        }
        LogLog.setInternalDebugging(isInternalDebugging())
        if (isUseFileAppender()) {
            configureFileAppender()
        }
        if (isUseLogCatAppender()) {
            configureLogCatAppender()
        }
        root.level = rootLevel
    }

    /**
     * Sets the level of logger with name `loggerName`. Corresponds
     * to log4j.properties `log4j.logger.org.apache.what.ever=ERROR`
     */
    fun setLevel(loggerName: String?, level: Level?) {
        Logger.getLogger(loggerName).level = level
    }

    private fun configureFileAppender() {
        if (isUseDailyRollingFileAppender()) {
            setDailyRollingFileAppender()
        } else {
            setRollingFileAppender()
        }
    }

    /**
     * 文件大小到达指定尺寸时产生一个新的文件
     */
    private fun setRollingFileAppender() {
        val root: Logger = Logger.getRootLogger()
        val rollingFileAppender: RollingFileAppender
        val fileLayout: Layout = PatternLayout(getFilePattern())
        rollingFileAppender = try {
            RollingFileAppender(fileLayout, fileName)
        } catch (e: IOException) {
            throw RuntimeException("Exception configuring log system", e)
        }
        rollingFileAppender.maxBackupIndex = getMaxBackupSize()
        //在日志文件到达设置的最大值后，将会自动滚动，即将原来的内容移到mylog.log.1文件
        rollingFileAppender.maximumFileSize = getMaxFileSize()
        rollingFileAppender.immediateFlush = isImmediateFlush()
        root.addAppender(rollingFileAppender)
    }

    /**
     * 每天生成一个新的日志文件
     */
    private fun setDailyRollingFileAppender() {
        val root: Logger = Logger.getRootLogger()
        val rollingFileAppender: DailyRollingFileAppender
        val fileLayout: Layout = PatternLayout(getFilePattern())
        try {
            rollingFileAppender =
                DailyRollingFileAppender(fileLayout, fileName, DATE_PATTERN)
        } catch (e: IOException) {
            e.printStackTrace()
            throw RuntimeException("Exception configuring log system", e)
        }
        rollingFileAppender.immediateFlush = isImmediateFlush()
        root.addAppender(rollingFileAppender)
    }

    private fun configureLogCatAppender() {
        val root: Logger = Logger.getRootLogger()
        val logCatLayout: Layout =
            PatternLayout(getLogCatPattern())
        val logCatAppender = LogCatAppender(logCatLayout)
        root.addAppender(logCatAppender)
    }

    /**
     * Return the log level of the root logger
     *
     * @return Log level of the root logger
     */
    fun getRootLevel(): Level {
        return rootLevel
    }

    /**
     * Sets log level for the root logger
     *
     * @param level Log level for the root logger
     */
    fun setRootLevel(level: Level) {
        rootLevel = level
    }

    private fun getFilePattern(): String {
        return filePattern
    }

    fun setFilePattern(filePattern: String) {
        this.filePattern = filePattern
    }

    private fun getLogCatPattern(): String {
        return logCatPattern
    }

    fun setLogCatPattern(logCatPattern: String) {
        this.logCatPattern = logCatPattern
    }

    /**
     * Returns the maximum number of backed up log files
     *
     * @return Maximum number of backed up log files
     */
    private fun getMaxBackupSize(): Int {
        return maxBackupSize
    }

    /**
     * Sets the maximum number of backed up log files
     *
     * @param maxBackupSize Maximum number of backed up log files
     */
    fun setMaxBackupSize(maxBackupSize: Int) {
        this.maxBackupSize = maxBackupSize
    }

    /**
     * Returns the maximum size of log file until rolling
     *
     * @return Maximum size of log file until rolling
     */
    private fun getMaxFileSize(): Long {
        return maxFileSize
    }

    /**
     * Sets the maximum size of log file until rolling
     *
     * @param maxFileSize Maximum size of log file until rolling
     */
    fun setMaxFileSize(maxFileSize: Long) {
        this.maxFileSize = maxFileSize
    }

    private fun isImmediateFlush(): Boolean {
        return immediateFlush
    }

    fun setImmediateFlush(immediateFlush: Boolean) {
        this.immediateFlush = immediateFlush
    }

    /**
     * Returns true, if FileAppender is used for logging
     *
     * @return True, if FileAppender is used for logging
     */
    private fun isUseFileAppender(): Boolean {
        return useFileAppender
    }

    /**
     * @param useFileAppender the useFileAppender to set
     */
    fun setUseFileAppender(useFileAppender: Boolean) {
        this.useFileAppender = useFileAppender
    }

    /**
     * Returns true, if LogcatAppender should be used
     *
     * @return True, if LogcatAppender should be used
     */
    private fun isUseLogCatAppender(): Boolean {
        return useLogCatAppender
    }

    /**
     * If set to true, LogCatAppender will be used for logging
     *
     * @param useLogCatAppender If true, LogCatAppender will be used for logging
     */
    fun setUseLogCatAppender(useLogCatAppender: Boolean) {
        this.useLogCatAppender = useLogCatAppender
    }

    fun setResetConfiguration(resetConfiguration: Boolean) {
        this.resetConfiguration = resetConfiguration
    }

    /**
     * Resets the log4j configuration before applying this configuration.
     * Default is true.
     *
     * @return True, if the log4j configuration should be reset before applying
     * this configuration.
     */
    private fun isResetConfiguration(): Boolean {
        return resetConfiguration
    }

    fun setInternalDebugging(internalDebugging: Boolean) {
        this.internalDebugging = internalDebugging
    }

    private fun isInternalDebugging(): Boolean {
        return internalDebugging
    }

    private fun isUseDailyRollingFileAppender(): Boolean {
        return useDailyRollingFileAppender
    }

    fun setUseDailyRollingFileAppender(useDailyRollingFileAppender: Boolean) {
        this.useDailyRollingFileAppender = useDailyRollingFileAppender
    }
}