package com.creepersan.file_manager.formatter

import java.text.SimpleDateFormat
import java.util.*

fun Double.to2String():String{
    return String.format("%.2f", this)
}

/**
 * 时间戳转时间
 */
val TIME_FORMATTER = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
fun Long.toDateString():String{
    return TIME_FORMATTER.format(this);
}

/**
 * 文件大小转换
 */
fun Long.toFileSizeString(isExist:Boolean = true, directoryFileCount:Int = -1):String{
    if (isExist){
        if (directoryFileCount >= 0){
            return "$directoryFileCount 文件"
        }
        return when{
            this > 1024L * 1024 * 1024 * 1024 -> {
                return "${(this / 1024.0 / 1024 / 1024 / 1024).to2String()} tb"
            }
            this > 1024L * 1024 * 1024 -> {
                return "${(this / 1024.0 / 1024 / 1024 ).to2String()} gb"
            }
            this > 1024L * 1024 -> {
                return "${(this / 1024.0 / 1024 ).to2String()} mb"
            }
            this > 1024L -> {
                return "${(this / 1024.0).to2String()} kb"
            }
            this < 2 -> {
                return "$this byte"
            }
            else -> {
                return "$this bytes"
            }
        }
    }else{
        return "不存在"
    }
}

