package com.creepersan.file.fragment.main

import com.creepersan.file.R
import com.creepersan.file.activity.FragmentPageObserver
import com.creepersan.file.activity.MainActivity
import com.creepersan.file.manager.ResourceManager

class AboutFragment(activityNotify: MainActivity.Controller, fragmentListObserver: FragmentPageObserver) : BaseMainFragment(activityNotify, fragmentListObserver){
    override fun getName(): String = ResourceManager.getString(R.string.aboutFragment_title)

    override fun getIcon(): Int = R.drawable.ic_info_outline

    override fun getLayoutID(): Int = R.layout.fragment_main_about

}