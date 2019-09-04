package com.creepersan.file.fragment.main

import android.view.View
import com.creepersan.file.activity.MainActivity
import com.creepersan.file.fragment.BaseFragment

abstract class BaseMainFragment : BaseFragment(){

    abstract fun getName() : String

    abstract fun getIcon() : Int

    open fun onBackPressed() : Boolean = false

    protected fun mainActivity():MainActivity{
        return activity as MainActivity
    }

    protected fun notifyFloatingActionButtonUpdate(){
        mainActivity().refreshFloatingActionButton()
    }

    open fun getFloatingActionButtonIcon():Int = 0

    open fun getFloatingActionButtonIsVisible() : Boolean = false

    open fun getFloatingActionButtonClickListener() : View.OnClickListener? = null

    open fun getFloatingActionButtonLongClickListener() : View.OnLongClickListener? = null

    protected fun closeFragment(){
        mainActivity().closeFragment(this)
    }
}