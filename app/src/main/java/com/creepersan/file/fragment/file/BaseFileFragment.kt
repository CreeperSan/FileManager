package com.creepersan.file.fragment.file

import com.creepersan.file.fragment.BaseFragment

abstract class BaseFileFragment : BaseFragment(){

    abstract fun getName() : String

    abstract fun getIcon() : Int

    open fun onBackPressed() : Boolean = false

}