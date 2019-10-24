package com.creepersan.file.bean.file

import com.creepersan.file.manager.ConfigManager
import com.creepersan.file.manager.FileManager
import java.util.ArrayList

class DirectoryInfo {

    val directory:FileInfo
    val fileList:ArrayList<FileInfo> = ArrayList()

    constructor(path:String):this(FileInfo(path))
    constructor(directory:FileInfo){
        this.directory = directory
        if (directory.isExist && directory.isDirectory){
            refresh()
        }
    }

    fun refresh(){
        fileList.clear()
        val addFileInfoList = FileManager.listFileInfo(this.directory)
        // 如果不顯示隱藏文件，則去除隱藏文件
        if (!ConfigManager.getGlobalShowHiddenFile()){
            val addFileInfoListIterator = addFileInfoList.iterator()
            while (addFileInfoListIterator.hasNext()){
                val fileInfo = addFileInfoListIterator.next()
                if(fileInfo.isHidden){
                    addFileInfoListIterator.remove()
                }
            }
        }
        this.fileList.addAll(addFileInfoList)
    }



}