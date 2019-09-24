package com.creepersan.file.manager

import android.content.Intent
import com.creepersan.file.application.FileApplication

object BroadcastManager {
    private val context by lazy { FileApplication.getInstance() }

    const val EMPTY_ACTION = ""

    // 通知文件夹下面的内容发生了变化
    const val PATH_CHANGE_ACTION = "com.creepersan.file.broadcast.PATH_CHANGE"
    private const val PATH_CHANGE_KEY_PATH = "path"

    fun notifyPathChange(path:String){
        broadcast(PATH_CHANGE_ACTION,
            PATH_CHANGE_KEY_PATH to path
        )
    }

    private fun broadcast(action:String, vararg pairs: Pair<String, Any>){
        val intent = Intent(action)
        pairs.forEach {  pair ->
            when(pair.second){
                is String -> intent.putExtra(pair.first, pair.second as String)
                is Int -> intent.putExtra(pair.first, pair.second as Int)
                is Double -> intent.putExtra(pair.first, pair.second as Double)
                is Float -> intent.putExtra(pair.first, pair.second as Float)
                is Long -> intent.putExtra(pair.first, pair.second as Long)
            }
        }
        context.sendBroadcast(intent)
    }


}