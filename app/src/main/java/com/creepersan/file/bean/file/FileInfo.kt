package com.creepersan.file.bean.file

import com.creepersan.file.manager.FileManager
import java.io.File

class FileInfo{
    var path = ""
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

    constructor(path: String) : this(File(path))
    constructor(file: File){
        this.path = file.path
        this.name = file.nameWithoutExtension
        this.extensionName = file.extension
        this.nameWithoutExtension = file.nameWithoutExtension
        this.modifyTime = file.lastModified()
        this.isExist = file.exists()
        this.isDirectory = file.isDirectory
        this.size = when{
            !this.isExist -> 0L
            this.isExist && this.isDirectory -> file.listFiles().size.toLong()
            this.isExist && !this.isDirectory -> file.length()
            else -> 0L
        }
        this.size = file.length()
    }

    fun rename(name:String):Int{
        // 检查文件名
        if (name.contains("/")){
            return FileManager.STATE_ERROR_INVALID_FILE_NAME
        }
        // 尝试重命名
        val file = File(path)
        if (file.exists()){
            val newFile = File("${File(path).parent}/$name")
            if (newFile.exists()){
                return FileManager.STATE_ERROR_ALREADY_EXIST
            }
            return if (file.renameTo(newFile)){
                FileManager.STATE_SUCCESS
            }else{
                FileManager.STATE_ERROR
            }
        }else{
            return FileManager.STATE_ERROR_NOT_EXIST
        }
    }

}