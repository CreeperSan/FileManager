package com.creepersan.file.global

import android.content.Context
import com.creepersan.file.bean.file.AppInfo
import com.creepersan.file.manager.AsyncTask
import com.creepersan.file.manager.AsyncTaskManager
import java.util.ArrayList

object GlobalAppInfo{

    private val mCacheApplicationInfoList = ArrayList<AppInfo>()

    fun getAllApplicationInfo(context:Context, listener:ApplicationInfoListener, isForce:Boolean = false){
        if (isForce || mCacheApplicationInfoList.isEmpty()){
            AsyncTaskManager.postTask(object : AsyncTask<ArrayList<AppInfo>>(){
                override fun runOnBackground(): ArrayList<AppInfo> {
                    val applicationInfoList = ArrayList<AppInfo>()
                    val packageManager = context.applicationContext.packageManager
                    for (packageInfo in packageManager?.getInstalledPackages(0)?: arrayListOf() ){
                        applicationInfoList.add(AppInfo(packageInfo))
                    }
                    return applicationInfoList
                }

                override fun onRunOnUI(response: ArrayList<AppInfo>?) {
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
        fun onGetData(applicationInfoList:ArrayList<AppInfo>)
    }

}




