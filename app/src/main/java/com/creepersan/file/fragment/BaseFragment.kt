package com.creepersan.file.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.creepersan.file.activity.BaseActivity
import com.creepersan.file.manager.BroadcastManager
import com.creepersan.file.manager.ResourceManager
import com.creepersan.file.manager.ToastManager

open class BaseFragment : Fragment() {
    protected lateinit var mRootView : View
    private val mBroadcastReceiver by lazy { InnerBroadcastReceiver() }
    private var mIsRegisterReceiver = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutID = getLayoutID()
        if (layoutID != 0){
            mRootView = layoutInflater.inflate(layoutID, container, false)
        }
        if (layoutID != 0){
            return mRootView
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    protected val activity get() = super.getActivity() as BaseActivity

    /**
     * 获取布局文件ID
     * @return 布局文件ID，如果为0则不进行自动设置布局
     */
    open fun getLayoutID() : Int{
        return 0
    }

    /**
     * 字符串资源ID转换为字符串
     * @return 字符串
     */
    protected fun Int.toResString() : String{
        return ResourceManager.getString(this)
    }

    /**
     * 显示Toast
     * @param content 显示的文本
     * @param isShort 是否短时间的toast
     */
    protected fun showToast(content:String, isShort:Boolean=true){
        ToastManager.show(content, isShort)
    }

    /**
     * Fragment创建
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        initBroadcast()
        super.onCreate(savedInstanceState)
    }

    /**
     * Fragment销毁
     */
    override fun onDestroy() {
        deinitBroadcast()
        super.onDestroy()
    }

    open fun getBroadcastActions():Array<String>{
        return arrayOf()
    }

    private fun initBroadcast(){
        val actions = getBroadcastActions()
        if (actions.isEmpty()){
            return
        }

        val intentFilter = IntentFilter()
        actions.forEach {  action ->
            intentFilter.addAction(action)
        }
        context?.apply {
            mIsRegisterReceiver = true
            registerReceiver(mBroadcastReceiver, intentFilter)
        }
    }

    private fun deinitBroadcast(){
        if (mIsRegisterReceiver){
            context?.unregisterReceiver(mBroadcastReceiver)
        }
    }

    private inner class InnerBroadcastReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
            onBroadcast(intent.action ?: BroadcastManager.EMPTY_ACTION, intent)
        }

    }

    /**
     * 通知事件的回调，如果有监听的话可以重写
     */
    open fun onBroadcast(action:String, intent:Intent){

    }

}