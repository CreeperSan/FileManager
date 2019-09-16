package com.creepersan.file.activity

import android.app.Activity
import android.os.Bundle
import com.creepersan.file.R
import kotlinx.android.synthetic.main.activity_create_file_directory.*

class CreateFileDirectoryActivity : BaseActivity() {

    companion object{
        const val INTENT_KEY_DIRECTORY_PATH = "directory_path"
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_create_file_directory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initIntent()
        initToolbar()
    }

    private fun initIntent(){

    }

    private fun initToolbar(){
        createFileDirectoryToolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

}