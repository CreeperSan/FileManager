package com.creepersan.file.bean.file

import com.creepersan.file.manager.StorageManager
import java.io.File
import java.util.*

class FilePageInfo{

    private val mDirectoryStack = LinkedList<DirectoryInfo>()

    constructor():this(StorageManager.getExternalStoragePath())
    constructor(directoryPath:String):this(DirectoryInfo(directoryPath))
    constructor(directoryInfo:DirectoryInfo){
        mDirectoryStack.clear()
        mDirectoryStack.add(directoryInfo)
    }

    fun pushDirectory(fileInfo: FileInfo){
        val file = File(fileInfo.path)
        if (file.exists() && file.isDirectory){
            mDirectoryStack.addLast(DirectoryInfo(fileInfo))
        }
    }

    fun popDirectory(){
        if (mDirectoryStack.isNotEmpty()){
            mDirectoryStack.removeLast()
        }
    }

    fun getCurrentPageFileCount():Int{
        if (mDirectoryStack.isNotEmpty()){
            return mDirectoryStack.last.fileList.size
        }
        return 0
    }

    fun getCurrentPageFileList():List<FileInfo>{
        val resultList = ArrayList<FileInfo>()
        if (mDirectoryStack.isNotEmpty()){
            resultList.addAll(mDirectoryStack.last.fileList)
        }
        return resultList
    }

    fun getCurrentPageFile(pos:Int):FileInfo{
        return mDirectoryStack.last.fileList[pos]
    }

    fun getCurrentDirectoryInfo():DirectoryInfo{
        return mDirectoryStack.last
    }

}