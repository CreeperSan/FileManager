package com.creepersan.file.manager

import com.creepersan.file.application.FileApplication
import java.util.concurrent.Executors

object AsyncTaskManager{

    /**
     * 暂时的解决方案，性能低下
     */
    fun postTask(asyncTask:AsyncTask){
        object : Thread(){
            override fun run() {
                super.run()
                val response = asyncTask.runOnBackground()
                FileApplication.getInstance().runOnUIThread(Runnable {
                    asyncTask.onRunOnUI(response)
                })
            }
        }.start()
    }

}

abstract class AsyncTask{
    abstract fun runOnBackground():Any?
    open fun onRunOnUI(response:Any?){}
}