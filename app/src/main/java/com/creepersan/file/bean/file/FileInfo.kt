package com.creepersan.file.bean.file

import com.creepersan.file.manager.StorageManager
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

    fun getParentFileInfo():FileInfo{
        return FileInfo(File(path).parentFile!!)
    }

    constructor(path: String) : this(File(path))
    constructor(file: File){
        this.path = file.path
        this.name = file.nameWithoutExtension
        this.fullName = file.name
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
            return StorageManager.STATE_ERROR_INVALID_FILE_NAME
        }
        // 尝试重命名
        val file = File(path)
        if (file.exists()){
            val newFile = File("${File(path).parent}/$name")
            if (newFile.exists()){
                return StorageManager.STATE_ERROR_ALREADY_EXIST
            }
            return if (file.renameTo(newFile)){
                StorageManager.STATE_SUCCESS
            }else{
                StorageManager.STATE_ERROR
            }
        }else{
            return StorageManager.STATE_ERROR_NOT_EXIST
        }
    }

    fun delete():Int{
        val tmpFile = File(path)
        if (tmpFile.exists()){
            return if(tmpFile.delete()){
                StorageManager.STATE_SUCCESS
            }else{
                StorageManager.STATE_ERROR
            }
        }else{
            return StorageManager.STATE_ERROR_NOT_EXIST
        }
    }

    fun create():Int{
        val tmpFile = File(path)
        if (tmpFile.exists()){
            return StorageManager.STATE_SUCCESS
        }else{
            if (isDirectory){
                return if (tmpFile.mkdirs()){
                    StorageManager.STATE_SUCCESS
                }else{
                    StorageManager.STATE_ERROR
                }
            }else{
                return if (tmpFile.createNewFile()){
                    StorageManager.STATE_SUCCESS
                }else{
                    StorageManager.STATE_ERROR
                }
            }
        }
    }

    fun copy(parentPath:String, override:Boolean=false):Int{
        return this.copy(FileInfo(parentPath), override)
    }

    fun copy(parentFolder:FileInfo, override:Boolean=false):Int{
        return this.moveAction(parentFolder, override, false)
    }

    private fun moveAction(parentDirectory:FileInfo, override: Boolean=false, isMove:Boolean):Int{
        val newFile = File(getParentFileInfo().path)
        val currentFile = File(path)
        when{
            !currentFile.exists() -> {
                return StorageManager.STATE_ERROR_NOT_EXIST
            }
            newFile.exists() && !override -> {
                return StorageManager.STATE_ERROR_ALREADY_EXIST
            }
            newFile.exists() && override -> {
                return try {
                    currentFile.copyTo(newFile, true)
                    if (isMove){
                        currentFile.delete()
                    }
                    StorageManager.STATE_SUCCESS
                } catch (e: Exception) {
                    e.printStackTrace()
                    StorageManager.STATE_ERROR
                }
            }
            !newFile.exists() -> {
                return try {
                    currentFile.copyTo(newFile, false)
                    if (isMove){
                        currentFile.delete()
                    }
                    StorageManager.STATE_SUCCESS
                } catch (e: Exception) {
                    e.printStackTrace()
                    StorageManager.STATE_ERROR
                }
            }
            else -> {
                return StorageManager.STATE_ERROR
            }
        }
    }


    fun move(parentPath:String, override:Boolean=false):Int{
        return this.move(FileInfo(parentPath), override)
    }

    fun move(parentFolder:FileInfo, override:Boolean=false):Int{
        return this.moveAction(parentFolder, override, true)
    }

    fun listFileInfo():ArrayList<FileInfo>{
        val resultList = ArrayList<FileInfo>()
        val currentDirectory = File(path)
        if (currentDirectory.exists() && currentDirectory.isDirectory){
            val listFileArray = currentDirectory.listFiles()
            listFileArray?.forEach { tmpFile ->
                resultList.add(FileInfo(tmpFile))
            }
            return resultList
        }else{
            return resultList
        }
    }

}