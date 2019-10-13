package com.creepersan.file.bean.file

import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
import android.os.Build
import com.creepersan.file.application.FileApplication

class ApplicationInfo(packageInfo:PackageInfo){
    val icon : Drawable
    val name : String
    val packageName : String
    val versionName : String
    val versionCode : Long
    val dataPath : String
    val targetSDKVersion : Int
    val processName : String
    val publicSourceDir : String
    val sourceDir : String
    val firstInstallTime : Long

    init {
        val applicationInfo = packageInfo.applicationInfo
        val packageManager = FileApplication.getInstance().packageManager

        icon = packageManager.getApplicationIcon(applicationInfo)
        name = packageManager.getApplicationLabel(applicationInfo).toString()
        sourceDir = applicationInfo.sourceDir
        publicSourceDir = applicationInfo.publicSourceDir
        processName = applicationInfo.processName
        targetSDKVersion = applicationInfo.targetSdkVersion
        dataPath = applicationInfo.dataDir
        applicationInfo.targetSdkVersion
        packageName = packageInfo.packageName
        versionName  = packageInfo.versionName
        firstInstallTime = packageInfo.firstInstallTime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            versionCode = packageInfo.longVersionCode
        }else{
            versionCode = packageInfo.versionCode.toLong()
        }

    }

}