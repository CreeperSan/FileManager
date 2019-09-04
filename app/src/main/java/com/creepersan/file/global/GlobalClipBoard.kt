package com.creepersan.file.global

import com.creepersan.file.bean.file.FileInfo
import java.util.*

object GlobalClipBoard {
    private val mSubscriberList = ArrayList<GlobalClipBoardObserver>()
    private val mFileInfoClipBoardList = ArrayList<FileInfo>()

    fun subscribe(subscriber:GlobalClipBoardObserver){
        if (!mSubscriberList.contains(subscriber)){
            mSubscriberList.add(subscriber)
        }
    }

    fun unsubscribe(subscriber:GlobalClipBoardObserver){
        mSubscriberList.remove(subscriber)
    }

    fun clearSubscriber(){
        mSubscriberList.clear()
    }

    private fun notifyFileInfoListChange(){
        mSubscriberList.forEach { subscribe ->
            subscribe.onClipboardChange()
        }
    }

    fun getFileInfo(pos:Int):FileInfo{
        return mFileInfoClipBoardList[pos]
    }

    fun getSize():Int{
        return mFileInfoClipBoardList.size
    }

    fun removeFileInfo(vararg fileInfo:FileInfo){
        removeFileInfo(fileInfo.toList())
    }

    fun addFileInfo(vararg fileInfo:FileInfo){
        addFileInfo(fileInfo.toList())
    }

    fun setFileInfo(vararg fileInfo:FileInfo){
        setFileInfo(fileInfo.toList())
    }

    fun cleatFileInfo(){
        mFileInfoClipBoardList.clear()
        notifyFileInfoListChange()
    }

    fun addFileInfo(fileInfoList:List<FileInfo>){
        fileInfoList.forEach { fileInfo ->
            if(!mFileInfoClipBoardList.contains(fileInfo)){
                mFileInfoClipBoardList.add(fileInfo)
            }
        }
        notifyFileInfoListChange()
    }

    fun setFileInfo(fileInfo:List<FileInfo>){
        mFileInfoClipBoardList.clear()
        mFileInfoClipBoardList.addAll(fileInfo)
        notifyFileInfoListChange()
    }

    fun removeFileInfo(fileInfoList:List<FileInfo>){
        mFileInfoClipBoardList.removeAll(fileInfoList)
        notifyFileInfoListChange()
    }

    fun hasFileInfo(fileInfo:FileInfo):Boolean{
        return mFileInfoClipBoardList.contains(fileInfo)
    }

    interface GlobalClipBoardObserver{
        fun onClipboardChange()
    }

}