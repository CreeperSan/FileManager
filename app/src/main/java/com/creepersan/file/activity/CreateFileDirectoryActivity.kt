package com.creepersan.file.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.creepersan.file.R
import com.creepersan.file.manager.ToastManager
import kotlinx.android.synthetic.main.activity_create_file_directory.*

class CreateFileDirectoryActivity : BaseActivity() {
    private lateinit var directoryPath : String

    companion object{
        const val INTENT_KEY_DIRECTORY_PATH = "directory_path"
        const val INTENT_KEY_TYPE = "type"

        const val INTENT_TYPE_FILE = "file"
        const val INTENT_TYPE_DIRECTORY = "directory"
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
        if (!intent.hasExtra(INTENT_KEY_DIRECTORY_PATH)){
            ToastManager.show(R.string.createFileDirectoryActivity_hintNotSelectDirectory)
            finish()
        }else{
            directoryPath = intent.getStringExtra(INTENT_KEY_DIRECTORY_PATH) ?: "/"
            Log.e("TAG", directoryPath)
        }

    }

    private fun initToolbar(){
        createFileDirectoryToolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

}