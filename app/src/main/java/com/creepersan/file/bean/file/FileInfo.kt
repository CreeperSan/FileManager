package com.creepersan.file.bean.file

import com.creepersan.file.manager.FileManager
import java.io.File
import java.util.ArrayList

class FileInfo{
    var path = ""
        private set
    var fullName = ""
        private set
    var name = ""
        private set
    var modifyTime = 0L
        private set
    var size = 0L
        private set
    var isExist = false
        private set
    var isDirectory = false
        private set
    var extensionName = ""
        private set
    var nameWithoutExtension = ""
        private set
    var isHidden = false

    fun getParentFileInfo():FileInfo{
        return FileInfo(File(path).parentFile!!)
    }

    constructor(path: String, isDirectory:Boolean):this (path){
        this.path = path
        this.isDirectory = isDirectory
    } // 用于创建不存在的文件
    constructor(path: String) : this(File(path))
    constructor(file: File){
        this.path = file.path
        this.name = file.nameWithoutExtension
        this.fullName = file.name
        this.extensionName = file.extension
        this.nameWithoutExtension = file.nameWithoutExtension
        this.modifyTime = file.lastModified()
        this.isExist = file.exists()
        this.isHidden = file.isHidden
        this.isDirectory = file.isDirectory
        this.size = when{
            !this.isExist -> 0L
            this.isExist && this.isDirectory -> file.listFiles()?.size?.toLong() ?: 0L
            this.isExist && !this.isDirectory -> file.length()
            else -> 0L
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is FileInfo){
            return this.path == other.path
        }else if (other is File){
            return File(this.path).absolutePath == other.absolutePath
        }
        return false
    }

}