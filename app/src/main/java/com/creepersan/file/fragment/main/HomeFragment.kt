package com.creepersan.file.fragment.main

import com.creepersan.file.R
import com.creepersan.file.activity.FragmentPageObserver
import com.creepersan.file.activity.MainActivity
import com.creepersan.file.manager.ResourceManager

class HomeFragment(activityNotify: MainActivity.Controller, fragmentListObserver: FragmentPageObserver) : BaseMainFragment(activityNotify, fragmentListObserver){

    override fun getName(): String = ResourceManager.getString(R.string.homeFragment_title)

    override fun getIcon(): Int = R.drawable.ic_home

    override fun getLayoutID(): Int = R.layout.fragment_main_home

}