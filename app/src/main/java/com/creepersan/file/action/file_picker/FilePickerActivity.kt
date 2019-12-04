package com.creepersan.file.action.file_picker

import android.os.Bundle
import com.creepersan.file.R
import com.creepersan.file.activity.BaseActivity
import com.creepersan.file.bean.file.AppInfo
import com.creepersan.file.global.GlobalAppInfo
import java.util.ArrayList

class FilePickerActivity : BaseActivity(){

    companion object{
        const val INTENT_EXTENSION_NAME = "extension_name"
        const val INTENT_TITLE = "title"
        const val INTENT_IS_MULTI_SELECT = "is_not_select"
    }

    override fun getLayoutID(): Int  = R.layout.activity_file_picker

    private var mTitle = "选择文件"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!initIntent()){
            finish()
            return
        }
        initToolbar()
        initData()
    }

    private fun initIntent():Boolean{
        // 初始化标题
        if (intent.hasExtra(INTENT_TITLE)){
            mTitle = intent.getStringExtra(INTENT_TITLE)
        }
        return true
    }

    private fun initToolbar(){

    }

    private fun initData(){
        GlobalAppInfo.getAllApplicationInfo(this, object : GlobalAppInfo.ApplicationInfoListener{
            override fun onGetData(applicationInfoList: ArrayList<AppInfo>) {

            }
        })
    }


}