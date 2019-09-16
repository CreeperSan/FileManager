package com.creepersan.file.manager

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.creepersan.file.activity.BaseActivity
import com.creepersan.file.activity.CreateFileDirectoryActivity
import com.creepersan.file.application.FileApplication
import java.io.Serializable

object NavigatorManager {
    private val mContext = FileApplication.getInstance()

    private fun <T : BaseActivity> toActivity(clazz:Class<T>, data:Map<String, Any>?=null, isFinish:Boolean=false, requestCode:Int=Int.MIN_VALUE, activity: BaseActivity?=null){
        val intent = if (activity == null){
            Intent(mContext, clazz)
        } else {
            Intent(activity, clazz)
        }
        data?.forEach{ pair ->
            when(val value = pair.value){
                is Bundle -> intent.putExtra(pair.key, value)
                is Parcelable -> intent.putExtra(pair.key, value)
                is Serializable -> intent.putExtra(pair.key, value)
                is Boolean -> intent.putExtra(pair.key, value)
                is BooleanArray -> intent.putExtra(pair.key, value)
                is Byte -> intent.putExtra(pair.key, value)
                is ByteArray -> intent.putExtra(pair.key, value)
                is Char -> intent.putExtra(pair.key, value)
                is CharArray -> intent.putExtra(pair.key, value)
                is CharSequence -> intent.putExtra(pair.key, value)
                is Double -> intent.putExtra(pair.key, value)
                is DoubleArray -> intent.putExtra(pair.key, value)
                is Int -> intent.putExtra(pair.key, value)
                is IntArray -> intent.putExtra(pair.key, value)
                is Long -> intent.putExtra(pair.key, value)
                is LongArray -> intent.putExtra(pair.key, value)
                is Short -> intent.putExtra(pair.key, value)
                is ShortArray -> intent.putExtra(pair.key, value)
                is String -> intent.putExtra(pair.key, value)
            }
        }
        if (requestCode == Int.MIN_VALUE){
            if (activity == null){
                Intent(mContext, clazz)
            } else {
                Intent(activity, clazz)
            }
        }else{
            activity?.startActivityForResult(intent, requestCode)
        }
        if (isFinish){
            activity?.finish()
        }
    }

    fun toCreateFileDirectoryActivity(baseActivity: BaseActivity, path:String, requestCode: Int){
        toActivity(CreateFileDirectoryActivity::class.java, mapOf(
            CreateFileDirectoryActivity.INTENT_KEY_DIRECTORY_PATH to path
        ),requestCode = requestCode)
    }


}