package com.creepersan.file.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.creepersan.file.R
import com.creepersan.file.bean.file.FileInfo
import com.creepersan.file.common.view_holder.IncludeSimpleDialogActionViewHolder
import com.creepersan.file.common.view_holder.IncludeSimpleDialogTitleViewHolder
import com.creepersan.file.manager.ToastManager
import java.io.File

class FileRenameSingleDialog(context: Context) : BaseDialog(context, POSITION_CENTER, ANIMATION_CENTER) {
    override fun getLayoutID(): Int = R.layout.dialog_file_fragment_rename_single

    private val mTitleViewHolder = IncludeSimpleDialogTitleViewHolder(dialogView.findViewById(R.id.dialogFileFragmentRenameSingleIncludeTitle))
    private val mActionViewHolder = IncludeSimpleDialogActionViewHolder(dialogView.findViewById(R.id.dialogFileFragmentRenameSingleIncludeAction))
    private val mEditText = dialogView.findViewById<EditText>(R.id.dialogFileFragmentRenameSingleEditText)
    private lateinit var mFile : File
    private var mResultCallback : OnResultCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTitle()
        initAction()
    }

    private fun initTitle(){
        mTitleViewHolder.setTitle(R.string.fileFragment_dialogRenameSingleTitle)
    }

    private fun initAction(){
        mActionViewHolder.setPositiveButton(R.string.common_dialogPositive, View.OnClickListener {
            val newFileName = mEditText.text.toString()
            if (newFileName.isEmpty()){
                ToastManager.show(R.string.fileFragment_dialogRenameSingleFileNameCannotEmptyHint)
                return@OnClickListener
            }
            val originFilePath = mFile.path
            val newFile = File("${originFilePath.substring(0, originFilePath.lastIndexOf("/"))}/$newFileName")
            if (newFile.exists()){
                ToastManager.show(R.string.fileFragment_dialogRenameSingleFileNameAlreadyExistHint)
                return@OnClickListener
            }
            if (mFile.renameTo(newFile)){
                mResultCallback?.onResult()
                mResultCallback?.onSuccess(FileInfo(mFile), FileInfo(newFile))
            }else{
                mResultCallback?.onResult()
                mResultCallback?.onFail(FileInfo(mFile))
            }
        })
        mActionViewHolder.setNegativeButton(R.string.common_dialogNegative, View.OnClickListener {
            hide()
        })
    }

    fun setFileInfo(fileInfo: FileInfo, callback:OnResultCallback?):FileRenameSingleDialog{
        mFile = File(fileInfo.path)
        mResultCallback = callback
        return this
    }

    /**
     * 调用 show() 方法之前必须先调用 setFileInfo(FileInfo) 方法
     */
    override fun show() {
        if (!mFile.exists()){
            ToastManager.show(R.string.fileFragment_dialogRenameSingleFileNameNotExistHint)
            return
        }
        mEditText.setText(mFile.name)
        super.show()
    }

    interface OnResultCallback{
        fun onResult()
        fun onFail(fileInfo: FileInfo)
        fun onSuccess(fileInfo: FileInfo, newFileInfo: FileInfo)
    }

}