package com.creepersan.file.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.creepersan.file.R
import com.creepersan.file.activity.BaseActivity

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
     * 获取Activity
     */
    protected fun activity() : BaseActivity{
        return activity as BaseActivity
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

    fun View.visible(){
        this.visibility = View.VISIBLE
    }

    fun View.invisible(){
        this.visibility = View.INVISIBLE
    }

    fun View.gone(){
        this.visibility = View.INVISIBLE
    }


    /**
     * 显示Toast
     * @param content 显示的文本
     * @param isShort 是否短时间的toast
     */
    protected fun showToast(content:String, isShort:Boolean=true){
        Toast.makeText(activity, content, if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG ).show()
    }

}