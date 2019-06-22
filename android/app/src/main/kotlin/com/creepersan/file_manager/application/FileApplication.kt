package com.creepersan.file_manager.application

import android.app.Activity
import android.app.Application
import io.flutter.view.FlutterMain

class FileApplication : Application(){
    private var mCurrentActivity: Activity? = null


    override fun onCreate() {
        super.onCreate()
        FlutterMain.startInitialization(this)
    }

    fun getCurrentActivity(): Activity? {
        return this.mCurrentActivity
    }

    fun setCurrentActivity(mCurrentActivity: Activity) {
        this.mCurrentActivity = mCurrentActivity
    }
}