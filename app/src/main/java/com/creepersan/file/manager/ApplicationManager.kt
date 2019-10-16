package com.creepersan.file.manager

import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import com.creepersan.file.application.FileApplication
import com.creepersan.file.bean.file.ApplicationInfo
import java.io.File
import java.lang.Exception


object ApplicationManager {
    private val mApplicaiton = FileApplication.getInstance()

    fun openApplication(applicationInfo: ApplicationInfo, onError:((exception:Exception)->Unit)? = null){
        try {
            val intent = mApplicaiton.packageManager.getLaunchIntentForPackage(applicationInfo.packageName)
            mApplicaiton.startActivity(intent)
        }catch (e:Exception){
            onError?.invoke(e)
        }
    }

    fun getApplicationAPK():File{
        return File("/")
    }

}