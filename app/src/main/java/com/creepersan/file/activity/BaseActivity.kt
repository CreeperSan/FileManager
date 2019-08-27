package com.creepersan.file.activity

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.creepersan.file.application.FileApplication
import java.io.Serializable

open class BaseActivity : AppCompatActivity(){

    open fun getLayoutID() : Int = 0
    open fun isLightTheme() : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初始化布局文件
        val layoutID = getLayoutID()
        if (layoutID != 0){
            setContentView(layoutID)
        }
        // 初始化状态栏颜色
        if (isLightTheme()){
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    /**
     * 跳转到指定Activity
     * @param clazz 指定的Activity类
     * @param data 跳转过去所携带的参数
     * @param requestCode 如果是需要返回结果的，则可以带上这个
     */
    protected fun <T : BaseActivity> toActivity(clazz:Class<T>, data:Map<String, Any>?=null, isFinish:Boolean=false, requestCode:Int=Int.MIN_VALUE){
        val intent = Intent(this, clazz)
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
            startActivity(intent)
        }else{
            startActivityForResult(intent, requestCode)
        }
        if (isFinish){
            finish()
        }
    }

    /**
     * 显示Toast
     * @param content 显示的文本
     * @param isShort 是否短时间的toast
     */
    protected fun showToast(content:String, isShort:Boolean=true){
        Toast.makeText(this, content, if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG ).show()
    }

    /**
     * 字符串标志转字符串
     * @return 字符串
     */
    protected fun Int.toResString():String{
        return getString(this)
    }

    /**
     * 获取Application实例
     * @return 实例化对象
     */
    protected fun getFileApplication():FileApplication{
        return FileApplication.getInstance()
    }

}