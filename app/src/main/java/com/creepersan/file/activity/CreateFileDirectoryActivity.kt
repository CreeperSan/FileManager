package com.creepersan.file.activity

import android.app.Activity
import android.os.Bundle
import com.creepersan.file.R
import kotlinx.android.synthetic.main.activity_create_file_directory.*

class CreateFileDirectoryActivity : BaseActivity() {

    override fun getLayoutID(): Int {
        return R.layout.activity_create_file_directory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
    }

    private fun initToolbar(){
        createFileDirectoryToolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

}