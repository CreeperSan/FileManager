package com.creepersan.file.global

import com.creepersan.file.bean.file.FileInfo
import java.util.*

object GlobalFileInfoClipBoard {
    enum class Action{
        COPY,
        MOVE
    }

    private val mSubscriberList = ArrayList<GlobalClipBoardObserver>()
    private val mFileInfoClipBoardList = ArrayList<CopiedFileInfo>()

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

    fun isEmpty():Boolean{
        return mFileInfoClipBoardList.isEmpty()
    }

    fun isNotEmpty():Boolean{
        return mFileInfoClipBoardList.isNotEmpty()
    }

    private fun notifyFileInfoListChange(){
        mSubscriberList.forEach { subscribe ->
            subscribe.onClipboardChange()
        }
    }

    fun getCopiedFileInfo(pos:Int):CopiedFileInfo{
        return mFileInfoClipBoardList[pos]
    }

    fun getSize():Int{
        return mFileInfoClipBoardList.size
    }

    fun removeFileInfo(vararg fileInfo:FileInfo){
        removeFileInfo(fileInfo.toList())
    }

    fun addFileInfo(action: Action, vararg fileInfo:FileInfo){
        addFileInfo(action, fileInfo.toList())
    }

    fun setFileInfo(action: Action, vararg fileInfo:FileInfo){
        setFileInfo(action, fileInfo.toList())
    }

    fun clearFileInfo(){
        mFileInfoClipBoardList.clear()
        notifyFileInfoListChange()
    }

    fun addFileInfo(action:Action, fileInfoList:List<FileInfo>){
        var hasSameFileInfo : Boolean
        fileInfoList.forEach { needToAddFileInfo ->
            hasSameFileInfo = false
            val iterator = mFileInfoClipBoardList.iterator()
            while (iterator.hasNext()){
                val copiedFileInfo = iterator.next()
                if (copiedFileInfo.fileInfo == needToAddFileInfo){
                    hasSameFileInfo = true
                    break
                }
            }
            if (!hasSameFileInfo){
                mFileInfoClipBoardList.add(CopiedFileInfo(needToAddFileInfo, action))
            }
        }
        notifyFileInfoListChange()
    }

    fun setFileInfo(action:Action, fileInfo:List<FileInfo>){
        mFileInfoClipBoardList.clear()
        fileInfo.forEach{ needToAddFileInfo ->
            mFileInfoClipBoardList.add(CopiedFileInfo(needToAddFileInfo, action))
        }
        notifyFileInfoListChange()
    }

    fun getCopiedFileInfoList():ArrayList<CopiedFileInfo>{
        return mFileInfoClipBoardList
    }

    fun removeFileInfo(fileInfoList:List<FileInfo>){
        fileInfoList.forEach{ needToDeleteFileInfo ->
            val iterator = mFileInfoClipBoardList.iterator()
            while (iterator.hasNext()){
                val copiedFileInfo = iterator.next()
                if (copiedFileInfo.fileInfo == needToDeleteFileInfo){
                    iterator.remove()
                    break
                }
            }
        }
        notifyFileInfoListChange()
    }

    fun hasFileInfo(fileInfo:FileInfo):Boolean{
        val iterator = mFileInfoClipBoardList.iterator()
        while (iterator.hasNext()){
            val copiedFileInfo = iterator.next()
            if (copiedFileInfo.fileInfo == fileInfo){
                return true
            }
        }
        return false
    }

    interface GlobalClipBoardObserver{
        fun onClipboardChange()
    }

}

data class CopiedFileInfo(var fileInfo:FileInfo, var action:GlobalFileInfoClipBoard.Action)
