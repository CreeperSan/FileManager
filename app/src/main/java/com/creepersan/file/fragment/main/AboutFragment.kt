package com.creepersan.file.fragment.main

import android.os.Build
import android.os.Bundle
import android.view.View
import com.creepersan.file.R
import com.creepersan.file.activity.FragmentPageObserver
import com.creepersan.file.activity.MainActivity
import com.creepersan.file.extension.gone
import com.creepersan.file.extension.visible
import com.creepersan.file.manager.ResourceManager
import kotlinx.android.synthetic.main.fragment_main_about.*

class AboutFragment(activityNotify: MainActivity.Controller, fragmentListObserver: FragmentPageObserver) : BaseMainFragment(activityNotify, fragmentListObserver){
    override fun getName(): String = ResourceManager.getString(R.string.aboutFragment_title)

    override fun getIcon(): Int = R.drawable.ic_info_outline

    override fun getLayoutID(): Int = R.layout.fragment_main_about

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initVersionText()
    }

    private fun initToolbar(){
        mainAboutToolbar.setNavigationOnClickListener {
            closeFragment()
        }
    }

    private fun initVersionText(){
        val packageManager = context?.packageManager
        if (packageManager == null){
            mainAboutVersion.gone()
        }else{
            mainAboutVersion.visible()
            val packageInfo = packageManager.getPackageInfo(context?.packageName ?: "", 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                mainAboutVersion.text = String.format(R.string.aboutFragment_versionName.toResString(), packageInfo.versionName, packageInfo.longVersionCode)
            }else{
                mainAboutVersion.text = String.format(R.string.aboutFragment_versionName.toResString(), packageInfo.versionName, packageInfo.versionCode)
            }
        }
    }

}