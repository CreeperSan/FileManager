package com.creepersan.file_manager.bridge

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject
import java.io.File
import java.lang.Exception

class FileChannel(val context:Context) : MethodChannel.MethodCallHandler{
    companion object{
        const val CHANNEL_NAME = "com.creepersan.file_manager/FileChannel"

        const val METHOD_GET_STORAGE_ROOT_PATH = "getStorageRootPath"
        const val METHOD_CHECK_HAS_FILE_PERMISSION = "checkHasFilePermission"
        const val METHOD_REQUEST_FILE_PERMISSION = "requestFilePermission"
        const val METHOD_GET_DIRECTORY_FILE_LIST = "getDirectoryFileList"
        const val METHOD_GET_FILE_DETAIL = "getFileDetail"
        const val METHOD_COPY_FILE = "copyFile"
        const val METHOD_CUT_FILE = "cutFile"
        const val METHOD_DELETE_FILE = "deleteFile"
        const val METHOD_RENAME_FILE = "renameFile"
    }

    override fun onMethodCall(call: MethodCall?, result: MethodChannel.Result?) {
        call ?: return
        result ?: return
        when(call.method){
            METHOD_GET_STORAGE_ROOT_PATH -> getStorageRootPath(call, result)
            METHOD_CHECK_HAS_FILE_PERMISSION -> checkHasFilePermission(call, result)
            METHOD_REQUEST_FILE_PERMISSION -> requestFilePermission(call, result)
            METHOD_GET_DIRECTORY_FILE_LIST -> getDirectoryFileList(call, result)
            METHOD_GET_FILE_DETAIL -> getFileDetail(call, result)
            METHOD_COPY_FILE -> copyFile(call, result)
            METHOD_CUT_FILE -> cutFile(call, result)
            METHOD_DELETE_FILE -> deleteFile(call, result)
            METHOD_RENAME_FILE -> renameFile(call, result)
        }
    }

    private fun getStorageRootPath(call: MethodCall, result:MethodChannel.Result){
        result.success(Environment.getExternalStorageDirectory().path)
    }

    private fun checkHasFilePermission(call: MethodCall, result:MethodChannel.Result){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val permissionResult = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (permissionResult == PackageManager.PERMISSION_GRANTED){
                result.success(true)
            }else{
                result.success(false)
            }
        }else{
            result.success(true)
        }
    }

    private fun requestFilePermission(call: MethodCall, result: MethodChannel.Result){

    }

    private fun getDirectoryFileList(call: MethodCall, result: MethodChannel.Result){

    }


    private fun getFileDetail(call: MethodCall, result: MethodChannel.Result){
        val jsonObject = JSONObject()
        val filePath : String
        try {
            filePath = call.arguments as String
        }catch (e:Exception){
            result.error("Params Error", "Params should be file path", null)
            e.printStackTrace()
            return
        }
        val file = File(filePath)
        val isExist = file.exists()
        jsonObject.put("isExist", isExist)
        jsonObject.put("isSelected", false)
        jsonObject.put("isDirectory", if (isExist) { file.isDirectory } else false)
        jsonObject.put("path", if (isExist){ file.absolutePath } else "")
        jsonObject.put("name", if (isExist){ file.name } else "")
        jsonObject.put("fileSize", if (isExist){ file.length() }else 0L)
        jsonObject.put("modifyTimeStamp", if (isExist){ file.lastModified() }else 0L)
        result.success(jsonObject.toString())
    }

    private fun copyFile(call: MethodCall, result: MethodChannel.Result){

    }

    private fun cutFile(call: MethodCall, result: MethodChannel.Result){

    }

    private fun deleteFile(call: MethodCall, result: MethodChannel.Result){

    }

    private fun renameFile(call: MethodCall, result: MethodChannel.Result){

    }





}