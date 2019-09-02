package com.creepersan.file.manager

import android.os.Environment
import com.creepersan.file.bean.file.FileInfo
import java.io.File
import java.util.*

/**
 * 文件操作类
 */
object FileManager {

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

    /**
     * 重命名
     * 重命名不会移动文件，新的命名文件也只是只能局限在当前目录下
     * @param fileInfo 源文件
     * @param name 新的文件名
     */
    fun rename(fileInfo:FileInfo, name:String):Int{
        // 检查文件名
        if (name.contains("/")){
            return STATE_ERROR_INVALID_FILE_NAME
        }
        // 尝试重命名
        val file = File(fileInfo.path)
        if (file.exists()){
            val newFile = File("${File(fileInfo.path).parent}/$name")
            if (newFile.exists()){
                return STATE_ERROR_ALREADY_EXIST
            }
            return if (file.renameTo(newFile)){
                STATE_SUCCESS
            }else{
                STATE_ERROR
            }
        }else{
            return STATE_ERROR_NOT_EXIST
        }
    }

    /**
     * 删除文件
     */
    fun delete(fileInfo:FileInfo):Int{
        val tmpFile = File(fileInfo.path)
        if (tmpFile.exists()){
            return if(tmpFile.delete()){
                STATE_SUCCESS
            }else{
                STATE_ERROR
            }
        }else{
            return STATE_ERROR_NOT_EXIST
        }
    }

    /**
     * 创建文件
     */
    fun create(fileInfo: FileInfo):Int{
        val tmpFile = File(fileInfo.path)
        if (tmpFile.exists()){
            return STATE_SUCCESS
        }else{
            if (fileInfo.isDirectory){
                return if (tmpFile.mkdirs()){
                    STATE_SUCCESS
                }else{
                    STATE_ERROR
                }
            }else{
                return if (tmpFile.createNewFile()){
                    STATE_SUCCESS
                }else{
                    STATE_ERROR
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
        val newFile = File(parentDirectory.getParentFileInfo().path)
        val currentFile = File(parentDirectory.path)
        when{
            !currentFile.exists() -> {
                return FileManager.STATE_ERROR_NOT_EXIST
            }
            newFile.exists() && !override -> {
                return FileManager.STATE_ERROR_ALREADY_EXIST
            }
            newFile.exists() && override -> {
                return try {
                    currentFile.copyTo(newFile, true)
                    if (isMove){
                        currentFile.delete()
                    }
                    FileManager.STATE_SUCCESS
                } catch (e: Exception) {
                    e.printStackTrace()
                    FileManager.STATE_ERROR
                }
            }
            !newFile.exists() -> {
                return try {
                    currentFile.copyTo(newFile, false)
                    if (isMove){
                        currentFile.delete()
                    }
                    FileManager.STATE_SUCCESS
                } catch (e: Exception) {
                    e.printStackTrace()
                    FileManager.STATE_ERROR
                }
            }
            else -> {
                return FileManager.STATE_ERROR
            }
        }
    }


    fun move(parentPath:String, override:Boolean=false):Int{
        return this.move(FileInfo(parentPath), override)
    }

    fun move(parentFolder:FileInfo, override:Boolean=false):Int{
        return this.moveAction(parentFolder, override, true)
    }

    fun listFileInfo(fileInfo:FileInfo): ArrayList<FileInfo> {
        val resultList = ArrayList<FileInfo>()
        val currentDirectory = File(fileInfo.path)
        if (currentDirectory.exists() && currentDirectory.isDirectory){
            // 初始化变量
            val listFileArray = currentDirectory.listFiles()
            val fileList = ArrayList<FileInfo>()
            val directoryList = ArrayList<FileInfo>()
            // 读取文件列表
            listFileArray?.forEach { tmpFile ->
                val tmpFileInfo = FileInfo(tmpFile)
                if (tmpFileInfo.isDirectory){
                    directoryList.add(tmpFileInfo)
                }else{
                    fileList.add(tmpFileInfo)
                }
            }
            // 进行排序
            val tmpDirectoryList = directoryList.sortedBy { sortFile -> sortFile.name.toLowerCase(Locale.getDefault()) }
            val tmpFileList = fileList.sortedBy { sortFile -> sortFile.name.toLowerCase(Locale.getDefault()) }
            // 数据综合
            resultList.addAll(tmpDirectoryList)
            resultList.addAll(tmpFileList)
            return resultList
        }else{
            return resultList
        }
    }


}