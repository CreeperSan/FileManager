package com.creepersan.file.global

import android.content.Context
import com.creepersan.file.bean.file.ApplicationInfo
import com.creepersan.file.manager.AsyncTask
import com.creepersan.file.manager.AsyncTaskManager
import java.util.ArrayList

object GlobalApplicationInfo{

    private val mCacheApplicationInfoList = ArrayList<ApplicationInfo>()

    fun getAllApplicationInfo(context:Context, listener:ApplicationInfoListener, isForce:Boolean = false){
        if (isForce || mCacheApplicationInfoList.isEmpty()){
            AsyncTaskManager.postTask(object : AsyncTask<ArrayList<ApplicationInfo>>(){
                override fun runOnBackground(): ArrayList<ApplicationInfo> {
                    val applicationInfoList = ArrayList<ApplicationInfo>()
                    val packageManager = context.applicationContext.packageManager
                    for (packageInfo in packageManager?.getInstalledPackages(0)?: arrayListOf() ){
                        applicationInfoList.add(ApplicationInfo(packageInfo))
                    }
                    return applicationInfoList
                }

                override fun onRunOnUI(response: ArrayList<ApplicationInfo>?) {
                    // 更新数据
                    synchronized(this) {
                        mCacheApplicationInfoList.clear()
                        mCacheApplicationInfoList.addAll(response!!)
                    }
                    // 回调接口
                    listener.onGetData(mCacheApplicationInfoList)
                }

            })
        }else{
            listener.onGetData(mCacheApplicationInfoList)
        }
    }

    fun clearCache(){
        synchronized(this){
            mCacheApplicationInfoList.clear()
        }
    }

    interface ApplicationInfoListener{
        fun onGetData(applicationInfoList:ArrayList<ApplicationInfo>)
    }

}




