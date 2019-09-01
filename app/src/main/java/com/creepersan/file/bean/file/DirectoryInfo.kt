package com.creepersan.file.bean.file

import java.util.ArrayList

class DirectoryInfo {

    val directory:FileInfo
    val fileList:ArrayList<FileInfo> = ArrayList()

    constructor(path:String):this(FileInfo(path))
    constructor(directory:FileInfo){
        this.directory = directory
        if (directory.isExist && directory.isDirectory){
            this.fileList.clear()
            this.fileList.addAll(this.directory.listFileInfo())
        }
    }



}