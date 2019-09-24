package com.creepersan.file.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.creepersan.file.R
import com.creepersan.file.manager.BroadcastManager
import com.creepersan.file.manager.ToastManager
import kotlinx.android.synthetic.main.activity_create_file_directory.*
import java.io.File

class CreateFileDirectoryActivity : BaseActivity() {
    private lateinit var type : String
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
        initView()
        initButton()
    }

    private fun initIntent(){
        // 路径
        if (!intent.hasExtra(INTENT_KEY_DIRECTORY_PATH)){
            ToastManager.show(R.string.createFileDirectoryActivity_hintNotSelectDirectory)
            finish()
        }else{
            directoryPath = intent.getStringExtra(INTENT_KEY_DIRECTORY_PATH) ?: "/"
        }
        // 类型
        type = intent.getStringExtra(INTENT_KEY_TYPE) ?: INTENT_TYPE_FILE
    }

    private fun initToolbar(){
        createFileDirectoryToolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun initView(){
        when(type){
            INTENT_TYPE_FILE -> {
                createFileDirectoryActionImage.setImageResource(R.drawable.img_create_file_action)
            }
            INTENT_TYPE_DIRECTORY -> {
                createFileDirectoryActionImage.setImageResource(R.drawable.img_create_directory_action)
            }
            else -> {
                finish()
            }
        }
    }

    private fun initButton(){
        createFileDirectoryCommitButton.setOnClickListener {
            val fileName = createFileDirectoryFileNameEditText.text.toString()
            // 防止文件名为空
            if (fileName.isEmpty()){
                ToastManager.show(R.string.createFileDirectoryActivity_hintFileNameCanNotBeEmpty)
                return@setOnClickListener
            }
            // 防止文件重复
            val file = File("$directoryPath/$fileName")
            if (file.exists()){
                ToastManager.show(R.string.createFileDirectoryActivity_hintFileAlreadyExist)
                return@setOnClickListener
            }
            // 创建文件
            when(type){
                INTENT_TYPE_FILE -> {
                    if (file.createNewFile()){
                        BroadcastManager.notifyPathChange(directoryPath)
                    }
                }
                INTENT_TYPE_DIRECTORY -> {
                    if (file.mkdirs()){
                        BroadcastManager.notifyPathChange(directoryPath)
                    }
                }
                else -> {
                    setResult(Activity.RESULT_CANCELED)
                }
            }
            finish()
        }
    }

}