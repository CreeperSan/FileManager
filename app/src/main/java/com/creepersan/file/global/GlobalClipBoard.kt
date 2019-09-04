package com.creepersan.file.global

import android.telephony.mbms.FileInfo
import java.util.*

object GlobalClipBoard {
    private val mFileInfoClipBoardList = ArrayList<FileInfo>()

    fun getFileInfo(pos:Int):FileInfo{
        return mFileInfoClipBoardList[pos]
    }

    fun getSize():Int{
        return mFileInfoClipBoardList.size
    }

    fun addFileInfo(vararg fileInfo:FileInfo){
        mFileInfoClipBoardList.addAll(fileInfo)
    }

    fun addFileInfo(fileInfoList:List<FileInfo>){
        mFileInfoClipBoardList.addAll(fileInfoList)
    }

    fun removeFileInfo(vararg fileInfo:FileInfo){
        mFileInfoClipBoardList.removeAll(fileInfo)
    }

    fun removeFileInfo(fileInfoList:List<FileInfo>){
        mFileInfoClipBoardList.removeAll(fileInfoList)
    }

    fun hasFileInfo(fileInfo:FileInfo):Boolean{
        return mFileInfoClipBoardList.contains(fileInfo)
    }

}