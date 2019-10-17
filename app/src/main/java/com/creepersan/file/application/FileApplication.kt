package com.creepersan.file.application

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import com.creepersan.file.activity.BaseActivity
import java.lang.ref.WeakReference

class FileApplication : Application(), Application.ActivityLifecycleCallbacks {
    private lateinit var mHandler : Handler
    private var mStackTopActivityReference = WeakReference<Activity>(null)

    companion object{
        private lateinit var mInstance : FileApplication

        fun getInstance():FileApplication{
            return mInstance
        }

    }

    override fun onCreate() {
        super.onCreate()
        initActivityLifeCycleListener()
        mInstance = this
        mHandler = Handler()
    }

    /**
     * 初始化Application中Activity生命周期监听
     */
    private fun initActivityLifeCycleListener(){
        registerActivityLifecycleCallbacks(this)
    }
    override fun onActivityPaused(p0: Activity) {
    }
    override fun onActivityStarted(p0: Activity) {
        mStackTopActivityReference = WeakReference(p0)
    }
    override fun onActivityDestroyed(p0: Activity) {
    }
    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }
    override fun onActivityStopped(p0: Activity) {
    }
    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        mStackTopActivityReference = WeakReference(p0)
    }
    override fun onActivityResumed(p0: Activity) {
        mStackTopActivityReference = WeakReference(p0)
    }

    fun getStackTopActivity():Activity?{
        return mStackTopActivityReference.get()
    }

    /**
     * 强行退出应用
     */
    fun exit(){
        System.exit(0)
    }

    fun runOnUIThread(runnable: Runnable){
        mHandler.post(runnable)
    }
}