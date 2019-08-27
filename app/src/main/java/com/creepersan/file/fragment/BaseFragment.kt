package com.creepersan.file.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    protected lateinit var mRootView : View

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
        return getString(this)
    }

}