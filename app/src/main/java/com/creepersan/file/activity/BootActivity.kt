package com.creepersan.file.activity

import android.os.Bundle
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission

class BootActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermission()

    }

    /**
     * 初始化权限
     */
    private fun initPermission(){
        AndPermission.with(this)
            .runtime()
            .permission(Permission.Group.STORAGE)
            .onGranted {
                toActivity(MainActivity::class.java)
            }
            .onDenied {

            }
    }

}