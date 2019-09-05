package com.creepersan.file.manager

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object FormatManager {
    private val timeFormatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    private val decimalFormatter = DecimalFormat("#0.00")

    const val FILE_SIZE_UNIT_AUTO = 0
    const val FILE_SIZE_UNIT_B = 1
    const val FILE_SIZE_UNIT_KB = 2
    const val FILE_SIZE_UNIT_MB = 3
    const val FILE_SIZE_UNIT_GB = 4
    const val FILE_SIZE_UNIT_TB = 5

    // 格式为 YYYY/MM/dd
    fun getFormatTime(time:Long):String{
        return timeFormatter.format(time)
    }

    fun getFormatSize(size:Long, unit:Int= FILE_SIZE_UNIT_AUTO):String{
        return when{
            FILE_SIZE_UNIT_B == unit || size < 1024 -> {
                "${decimalFormatter.format(size)} B"
            }
            FILE_SIZE_UNIT_KB== unit || size < 1024*1024 -> {
                "${decimalFormatter.format(size/1024f)} KB"
            }
            FILE_SIZE_UNIT_MB== unit || size < 1024*1024*1024 -> {
                "${decimalFormatter.format(size/1024f/1024f)} MB"
            }
            FILE_SIZE_UNIT_GB== unit || size < 1024*1024*1024*1024L -> {
                "${decimalFormatter.format(size/1024f/1024f/1024f)} GB"
            }
            FILE_SIZE_UNIT_TB== unit || size < 1024*1024*1024*1024L*1024 -> {
                "${decimalFormatter.format(size/1024f/1024f/1024f/1024f)} TB"
            }
            else -> {
                "${decimalFormatter.format(size)} B"
            }
        }
    }

}