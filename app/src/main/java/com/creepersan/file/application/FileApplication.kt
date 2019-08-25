package com.creepersan.file.application

import android.app.Application

class FileApplication : Application() {

    companion object{
        private lateinit var mInstance : FileApplication

        fun getInstance():FileApplication{
            return mInstance
        }

    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

    /**
     * 强行退出应用
     */
    fun exit(){
        System.exit(0)
    }

}