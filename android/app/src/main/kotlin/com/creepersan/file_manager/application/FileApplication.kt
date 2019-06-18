package com.creepersan.file_manager.application

import android.app.Application
import com.creepersan.file_manager.bridge.FileChannel

class FileApplication : Application(){
    private val mFileChannel by lazy { FileChannel(this) }

    override fun onCreate() {
        super.onCreate()
    }

}