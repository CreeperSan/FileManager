package com.creepersan.file.manager

import android.os.Environment
import com.creepersan.file.bean.file.FileInfo
import java.util.ArrayList

/**
 * 文件操作类
 */
object StorageManager {

    const val STATE_SUCCESS = 0
    const val STATE_ERROR = -1
    const val STATE_ERROR_NOT_EXIST = -2
    const val STATE_ERROR_ALREADY_EXIST = -3
    const val STATE_ERROR_INVALID_FILE_NAME = -4

    fun getExternalStoragePath():String{
        return Environment.getExternalStorageDirectory().path
    }

    fun getExternalStorageDirectory():FileInfo{
        return FileInfo(Environment.getExternalStorageDirectory())
    }

    fun getRootPath():String{
        return "/";
    }

}