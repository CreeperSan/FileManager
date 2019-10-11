package com.creepersan.file.fragment.main

import android.os.Bundle
import android.view.View
import com.creepersan.file.R
import com.creepersan.file.activity.FragmentPageObserver
import com.creepersan.file.activity.MainActivity
import com.creepersan.file.extension.gone
import com.creepersan.file.extension.visible
import com.creepersan.file.manager.AsyncTask
import com.creepersan.file.manager.AsyncTaskManager
import com.creepersan.file.manager.ResourceManager
import kotlinx.android.synthetic.main.fragment_main_application.*

class ApplicationFragment(activityNotify: MainActivity.Controller, fragmentListObserver: FragmentPageObserver) : BaseMainFragment(activityNotify, fragmentListObserver){
    override fun getName(): String = ResourceManager.getString(R.string.applicationFragment_title)

    override fun getIcon(): Int = R.drawable.ic_applications

    override fun getLayoutID(): Int = R.layout.fragment_main_application

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initHintView()
        initData()
    }

    private fun initToolbar(){
        mainApplicationToolbar.setNavigationOnClickListener {
            closeFragment()
        }
    }

    private fun initHintView(){
        mainApplicationHintView.visible()
    }

    private fun initData(){
        AsyncTaskManager.postTask(object : AsyncTask(){
            override fun runOnBackground(): Any? {
                val packageManager = context?.packageManager
                for (info in packageManager?.getInstalledPackages(0)?: arrayListOf() ){

                }
                return Unit
            }

            override fun onRunOnUI(response: Any?) {
                mainApplicationHintView.gone()
            }

        })
    }

}