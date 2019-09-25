package com.creepersan.file.application

import android.app.Application
import android.os.Handler

class FileApplication : Application() {
    private lateinit var mHandler : Handler

    companion object{
        private lateinit var mInstance : FileApplication

        fun getInstance():FileApplication{
            return mInstance
        }

    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        mHandler = Handler()
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