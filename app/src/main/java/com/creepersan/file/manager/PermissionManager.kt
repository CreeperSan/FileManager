package com.creepersan.file.manager

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.creepersan.file.activity.PermissionDescriptionActivity
import com.creepersan.file.application.FileApplication
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import java.util.ArrayList

object PermissionManager {
    val PERMISSION_STORAGE = Permission.Group.STORAGE


    /***
     * 检查是否存在权限
     * @param context 上下文
     * @param permission 权限标志
     * @return 返回没有权限了列表，如果有权限，那就返回空的列表
     */
    fun checkHasPermission(context:Context, permission:Array<String>) : ArrayList<String>{
        val permissionDeniedList = ArrayList<String>()
        permission.forEach { tmpPermission ->
            if(PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(context, tmpPermission)){
                permissionDeniedList.add(tmpPermission)
            }
        }
        return permissionDeniedList
    }

    /**
     * 请求权限，如果失败则默认进入权限描述页面
     * @param context 上下文
     * @param onSuccess 成功的回调
     * @param permissions 所有的权限
     */
    fun requestPermission(context: Context, onSuccess:()->Unit, onFail:(()->Unit)?=null, permissionArray:Array<String>){
        AndPermission.with(context)
            .runtime()
            .permission(permissionArray)
            .onGranted {
                onSuccess.invoke()
            }
            .onDenied {
                if (onFail == null){
                    FileApplication.getInstance().exit()
                }else{
                    onFail.invoke()
                }
            }
            .start()
    }


}