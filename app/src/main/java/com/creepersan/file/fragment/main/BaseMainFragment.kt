package com.creepersan.file.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.creepersan.file.activity.FragmentPageObserver
import com.creepersan.file.activity.MainActivity
import com.creepersan.file.fragment.BaseFragment

abstract class BaseMainFragment(protected val activityNotify: MainActivity.Controller, protected val fragmentListObserver: FragmentPageObserver) : BaseFragment(){

    abstract fun getName() : String

    abstract fun getIcon() : Int

    open fun onBackPressed() : Boolean = false

    /**********************************************************************************************/

    open fun getFloatingActionButtonIcon():Int = 0

    open fun getFloatingActionButtonIsVisible() : Boolean = false

    open fun getFloatingActionButtonClickListener() : View.OnClickListener? = null

    open fun getFloatingActionButtonLongClickListener() : View.OnLongClickListener? = null

    /**********************************************************************************************/

    open fun onPageClose(){}

    open fun onPageVisible(){}

    open fun onPageInvisible(){}

    protected fun closeFragment(){
        activityNotify.closePage(this)
    }
}