package com.creepersan.file.fragment.main

import android.os.Bundle
import android.view.View
import com.creepersan.file.R
import com.creepersan.file.activity.FragmentPageObserver
import com.creepersan.file.activity.MainActivity
import com.creepersan.file.manager.ResourceManager
import kotlinx.android.synthetic.main.fragment_main_home.*

class HomeFragment(activityNotify: MainActivity.Controller, fragmentListObserver: FragmentPageObserver) : BaseMainFragment(activityNotify, fragmentListObserver){

    override fun getName(): String = ResourceManager.getString(R.string.homeFragment_title)

    override fun getIcon(): Int = R.drawable.ic_home

    override fun getLayoutID(): Int = R.layout.fragment_main_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCard()
    }

    private fun initCard(){
        mainHomeFileFragment.setOnClickListener {
            fragmentListObserver.addFragment(FileFragment(activityNotify, fragmentListObserver))
        }
        mainHomeApplicationFragment.setOnClickListener {
            fragmentListObserver.addFragment(ApplicationFragment(activityNotify, fragmentListObserver))
        }
    }
}