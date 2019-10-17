package com.creepersan.file.manager

import android.app.Activity
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri
import com.creepersan.file.application.FileApplication
import com.creepersan.file.bean.file.ApplicationInfo
import com.creepersan.file.exception.NoActivityExistException
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

    fun uninstallApplication(activity:Activity, applicationInfo: ApplicationInfo, onError:((exception:Exception)->Unit)? = null){
        try {
            val uri = Uri.fromParts("package", applicationInfo.packageName, null)
            activity.startActivity(Intent(Intent.ACTION_DELETE, uri))
        }catch (e:Exception){
            onError?.invoke(e)
        }
    }

}