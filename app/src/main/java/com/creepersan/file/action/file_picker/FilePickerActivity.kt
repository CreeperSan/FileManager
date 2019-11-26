package com.creepersan.file.action.file_picker

import android.os.Bundle
import com.creepersan.file.R
import com.creepersan.file.activity.BaseActivity
import com.creepersan.file.bean.file.ApplicationInfo
import com.creepersan.file.global.GlobalApplicationInfo
import java.util.ArrayList

class FilePickerActivity : BaseActivity(){

    companion object{

    }

    override fun getLayoutID(): Int  = R.layout.activity_file_picker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initIntent()
        initData()
    }

    private fun initIntent(){

    }

    private fun initData(){
        GlobalApplicationInfo.getAllApplicationInfo(this, object : GlobalApplicationInfo.ApplicationInfoListener{
            override fun onGetData(applicationInfoList: ArrayList<ApplicationInfo>) {

            }
        })
    }


}