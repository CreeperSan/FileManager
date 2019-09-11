package com.creepersan.file.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.creepersan.file.R
import com.creepersan.file.bean.file.FileInfo
import com.creepersan.file.common.view_holder.IncludeSimpleDialogActionViewHolder
import com.creepersan.file.common.view_holder.IncludeSimpleDialogTitleViewHolder
import com.creepersan.file.manager.ToastManager
import java.io.File
import java.lang.StringBuilder
import java.text.DecimalFormat
import java.util.ArrayList

class FileRenameMultiDialog(context:Context) : BaseDialog(context, POSITION_CENTER, ANIMATION_CENTER){
    override fun getLayoutID(): Int = R.layout.dialog_file_fragment_rename_multi

    private val mHeaderViewHolder = IncludeSimpleDialogTitleViewHolder(dialogView.findViewById(R.id.dialogFileFragmentRenameMultiIncludeTitle))
    private val mActionViewHolder = IncludeSimpleDialogActionViewHolder(dialogView.findViewById(R.id.dialogFileFragmentRenameMultiIncludeAction))
    private val mFileNameEditText = dialogView.findViewById<EditText>(R.id.dialogFileFragmentRenameMultiFileNameEditText)
    private val mExtensionNameEditText = dialogView.findViewById<EditText>(R.id.dialogFileFragmentRenameMultiExtensionNameEditText)

    private val mFileInfoList = ArrayList<FileInfo>()
    private var mResultCallback : OnResultCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTitle()
        initAction()
    }

    private fun initTitle(){
        mHeaderViewHolder.setTitle(R.string.fileFragment_dialogRenameMultiTitle)
    }

    private fun initAction(){
        mActionViewHolder.setPositiveButton(R.string.common_dialogPositive, View.OnClickListener {
            if (mFileInfoList.isEmpty()){
                mResultCallback?.onResult()
                ToastManager.show(R.string.fileFragment_dialogRenameMultiNotSelectedFileHint)
                return@OnClickListener
            }
            val fileName = mFileNameEditText.text.toString()
            val extensionName = mExtensionNameEditText.text.toString()
            if (fileName.isEmpty() && extensionName.isEmpty()){ // 全都没改，则不需要操作
                mResultCallback?.onResult()
                mResultCallback?.onSuccess()
                return@OnClickListener
            }
            // 开始操作
            // 序号格式化器
            var successCount = 0
            var failCount = 0
            val formatStringBuilder = StringBuilder()
            var tmpSize = mFileInfoList.size
            do {
                tmpSize /= 10
                formatStringBuilder.append("0")
            }while (tmpSize > 0)
            val numFormatter =  DecimalFormat(formatStringBuilder.toString())
            mFileInfoList.forEachIndexed { index, tmpFileInfo ->
                val tmpFile = File(tmpFileInfo.path)
                val newFileName = if (fileName.isEmpty()){ "${tmpFile.nameWithoutExtension} - ${numFormatter.format(index+1)}" } else { "$fileName - ${numFormatter.format(index+1)}" }
                val parentPath = "${tmpFile.parent}/"
                val newExtensionName = when{
                    tmpFile.isDirectory -> ""
                    extensionName.isEmpty() -> tmpFile.extension
                    else -> extensionName
                }
                val newFile = when{
                    fileName.isEmpty() && newExtensionName.isNotEmpty() && tmpFile.isFile -> File("$parentPath${tmpFile.nameWithoutExtension}.$newExtensionName")
                    else -> File("$parentPath$newFileName${if (newExtensionName.isNotEmpty()){"."}else{""}}$newExtensionName")
                }
                if (newFile.exists() || !tmpFile.exists()){
                    failCount++
                }else if (tmpFile.renameTo(newFile)){
                    successCount++
                }else{
                    failCount++
                }
            }
            // 统计回调
            mResultCallback?.onResult()
            if (successCount >= mFileInfoList.size){
                mResultCallback?.onSuccess()
            }else if (failCount >= mFileInfoList.size){
                mResultCallback?.onFail()
            }else{
                mResultCallback?.onPartlySuccess()
            }
        })

        mActionViewHolder.setNegativeButton(R.string.common_dialogNegative, View.OnClickListener {
            closeDialog()
        })
    }

    fun setFileInfoList(list:List<FileInfo>):FileRenameMultiDialog{
        mFileNameEditText.setText("")
        mExtensionNameEditText.setText("")
        mFileInfoList.clear()
        mFileInfoList.addAll(list)
        return this
    }

    fun setResultCallback(callback:OnResultCallback?):FileRenameMultiDialog{
        mResultCallback = callback
        return this
    }

    interface OnResultCallback{
        fun onResult()
        fun onSuccess()
        fun onPartlySuccess()
        fun onFail()
    }

}