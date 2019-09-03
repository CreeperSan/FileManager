package com.creepersan.file.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.creepersan.file.manager.PermissionManager
import com.yanzhenjie.permission.runtime.Permission

class BootActivity : BaseActivity(){

    companion object{
        const val REQUEST_CODE_PERMISSION = 0
    }

    private val mPermissionList = arrayOf(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val deniedPermissionList = PermissionManager.checkHasPermission(this, mPermissionList)
        if (deniedPermissionList.isEmpty()){ // 拥有权限
            onPermissionGrained()
        }else{ // 没有权限
            toActivity(PermissionDescriptionActivity::class.java, mapOf(
                PermissionDescriptionActivity.INTENT_KEY_PERMISSION to PermissionManager.PERMISSION_STORAGE
            ), false, REQUEST_CODE_PERMISSION)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_CODE_PERMISSION -> {
                if (resultCode == Activity.RESULT_OK){
                    onPermissionGrained()
                }
            }
        }
    }

    private fun onPermissionGrained(){
        toActivity(MainActivity::class.java, isFinish = true)
    }


}