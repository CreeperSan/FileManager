package com.creepersan.file.manager

import com.creepersan.file.application.FileApplication
import java.util.concurrent.Executors

object AsyncTaskManager{

    /**
     * 暂时的解决方案，性能低下
     */
    fun <T> postTask(asyncTask:AsyncTask<T>){
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

abstract class AsyncTask<T>{
    abstract fun runOnBackground():T?
    open fun onRunOnUI(response:T?){}
}