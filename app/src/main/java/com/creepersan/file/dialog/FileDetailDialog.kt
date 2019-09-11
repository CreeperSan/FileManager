package com.creepersan.file.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.creepersan.file.R
import com.creepersan.file.bean.file.FileInfo
import com.creepersan.file.common.view_holder.IncludeSimpleDialogActionViewHolder
import com.creepersan.file.common.view_holder.IncludeSimpleDialogTitleViewHolder
import com.creepersan.file.extension.gone
import com.creepersan.file.extension.visible
import com.creepersan.file.manager.FormatManager
import com.creepersan.file.manager.ResourceManager
import java.io.File
import java.util.ArrayList

class FileDetailDialog(context: Context) : BaseDialog(context, POSITION_CENTER, ANIMATION_CENTER){
    override fun getLayoutID(): Int = R.layout.dialog_file_fragment_file_detail

    private val mTitleViewHolder = IncludeSimpleDialogTitleViewHolder(dialogView.findViewById(R.id.dialogFileFragmentFileDetailIncludeTitle))
    private val mActionViewHolder = IncludeSimpleDialogActionViewHolder(dialogView.findViewById(R.id.dialogFileFragmentFileDetailIncludeActionButton))
    private val mFileIconImageView = dialogView.findViewById<ImageView>(R.id.dialogFileFragmentFileDetailIcon)
    private val mFileNameTextView = dialogView.findViewById<TextView>(R.id.dialogFileFragmentFileDetailName)
    private val mTypeCategoryTextView = dialogView.findViewById<TextView>(R.id.dialogFileFragmentFileDetailCategoryType)
    private val mTypeValueTextView = dialogView.findViewById<TextView>(R.id.dialogFileFragmentFileDetailValueType)
    private val mSizeCategoryTextView = dialogView.findViewById<TextView>(R.id.dialogFileFragmentFileDetailCategorySize)
    private val mSizeValueTextView = dialogView.findViewById<TextView>(R.id.dialogFileFragmentFileDetailValueSize)
    private val mModifyTimeCategoryTextView = dialogView.findViewById<TextView>(R.id.dialogFileFragmentFileDetailCategoryModifyTime)
    private val mModifyTimeValueTextView = dialogView.findViewById<TextView>(R.id.dialogFileFragmentFileDetailValueModifyTime)
    private val mReadWritePermissionCategoryTextView = dialogView.findViewById<TextView>(R.id.dialogFileFragmentFileDetailCategoryReadWritePermission)
    private val mReadWritePermissionValueTextView = dialogView.findViewById<TextView>(R.id.dialogFileFragmentFileDetailValueReadWritePermission)
    private val mIsHiddenCategoryTextView = dialogView.findViewById<TextView>(R.id.dialogFileFragmentFileDetailCategoryIsHidden)
    private val mIsHiddenValueTextView = dialogView.findViewById<TextView>(R.id.dialogFileFragmentFileDetailValueHidden)
    private val mPathCategoryTextView = dialogView.findViewById<TextView>(R.id.dialogFileFragmentFileDetailCategoryPath)
    private val mPathValueTextView = dialogView.findViewById<TextView>(R.id.dialogFileFragmentFileDetailValuePath)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDialogTitle()
        setDialogAction()
    }

    private fun setDialogTitle(){
        mTitleViewHolder.setTitle(ResourceManager.getString(R.string.fileFragment_dialogFileDetailTitle))
    }

    private fun setDialogAction(){
        mActionViewHolder.setPositiveButton(ResourceManager.getString(R.string.common_dialogPositive), View.OnClickListener {
            hide()
        })
    }

    fun setFileInfo(vararg fileInfo: FileInfo):FileDetailDialog{
        return setFileInfo(fileInfo.toList())
    }

    fun setFileInfo(fileInfoList:List<FileInfo>):FileDetailDialog{
        val fileList = ArrayList<File>()
        fileInfoList.forEach{ fileInfo ->
            fileList.add(File(fileInfo.path))
        }
        // 初始化UI
        setFileBaseDetail(fileList)
        setFileType(fileList)
        setFileSize(fileList)
        setModifyTime(fileList)
        setReadWritePermission(fileList)
        setHidden(fileList)
        setPath(fileList)
        return this
    }

    private fun setFileBaseDetail(fileList:List<File>){
        when{
            fileList.isEmpty() -> {
                mFileIconImageView.setImageResource(R.drawable.ic_forbidden)
                mFileNameTextView.text = ResourceManager.getString(R.string.fileFragment_dialogFileDetailHintNoFileSelected)
            }
            fileList.size == 1 -> {
                val file = fileList[0]
                if (file.isDirectory){
                    mFileIconImageView.setImageResource(R.drawable.ic_directory_dark_blue)
                    mFileNameTextView.text = file.name
                }else{
                    mFileIconImageView.setImageResource(R.drawable.ic_file_grey)
                    mFileNameTextView.text = file.name
                }
            }
            fileList.size > 1 -> {
                mFileIconImageView.setImageResource(R.drawable.ic_file_sets)
                mFileNameTextView.text = String.format(ResourceManager.getString(R.string.fileFragment_dialogFileDetailHintMultiSelected), fileList.size)
            }
        }
    }

    private fun setFileType(fileList:List<File>){
        when{
            fileList.isEmpty() -> {
                mTypeValueTextView.text = ResourceManager.getString(R.string.fileFragment_dialogFileDetailTypeUnknown)
            }
            fileList.size == 1 -> {
                val file = fileList[0]
                if (file.isDirectory){
                    mTypeValueTextView.text = ResourceManager.getString(R.string.fileFragment_dialogFileDetailTypeDirectory)
                }else{
                    mTypeValueTextView.text = ResourceManager.getString(R.string.fileFragment_dialogFileDetailTypeFile)
                }
            }
            fileList.size > 1 -> {
                mTypeValueTextView.text = ResourceManager.getString(R.string.fileFragment_dialogFileDetailTypeMulti)
            }
        }
    }

    private fun setFileSize(fileList:List<File>){
        when{
            fileList.isEmpty() -> {
                mSizeCategoryTextView.gone()
                mSizeValueTextView.gone()
            }
            fileList.size == 1 -> {
                mSizeCategoryTextView.visible()
                mSizeValueTextView.visible()
                val file = fileList[0]
                if (file.isDirectory){
                    mSizeValueTextView.text = String.format(ResourceManager.getString(R.string.fileFragment_dialogFileDetailSizeDirectory), file.listFiles()?.size ?: 0)
                }else{
                    mSizeValueTextView.text = String.format(ResourceManager.getString(R.string.fileFragment_dialogFileDetailSizeFile), FormatManager.getFormatSize(file.length()), file.length())
                }
            }
            fileList.size > 1 -> {
                mSizeCategoryTextView.visible()
                mSizeValueTextView.visible()
                var fileCount = 0
                var directoryCount = 0
                fileList.forEach {  file ->
                    if (file.isDirectory){
                        directoryCount += 1
                    }else{
                        fileCount += 1
                    }
                }
                mSizeValueTextView.text = String.format(ResourceManager.getString(R.string.fileFragment_dialogFileDetailSizeMulti), fileCount, directoryCount)
            }
        }
    }

    private fun setModifyTime(fileList: List<File>){
        when{
            fileList.size == 1 -> {
                mModifyTimeCategoryTextView.visible()
                mModifyTimeValueTextView.visible()
                mModifyTimeValueTextView.text = FormatManager.getFormatTime(fileList[0].lastModified())
            }
            else -> {
                mModifyTimeCategoryTextView.gone()
                mModifyTimeValueTextView.gone()
            }
        }
    }

    private fun setReadWritePermission(fileList: List<File>){
        when{
            fileList.size == 1 -> {
                mReadWritePermissionCategoryTextView.visible()
                mReadWritePermissionValueTextView.visible()
                val file = fileList[0]
                val strBuilder = StringBuilder()
                if (file.canRead()){
                    strBuilder.append(ResourceManager.getString(R.string.fileFragment_dialogFileDetailReadWritePermissionCanRead))
                }
                if (file.canWrite()){
                    strBuilder.append(ResourceManager.getString(R.string.fileFragment_dialogFileDetailReadWritePermissionCanWrite))
                }
                if (file.canExecute()){
                    strBuilder.append(ResourceManager.getString(R.string.fileFragment_dialogFileDetailReadWritePermissionCanExecute))
                }
                mReadWritePermissionValueTextView.text = strBuilder.toString()
            }
            else -> {
                mReadWritePermissionCategoryTextView.gone()
                mReadWritePermissionValueTextView.gone()
            }
        }
    }

    private fun setHidden(fileList: List<File>){
        when{
            fileList.size == 1 -> {
                mIsHiddenCategoryTextView.visible()
                mIsHiddenValueTextView.visible()
                val file = fileList[0]
                if (file.isHidden){
                    mIsHiddenValueTextView.text = ResourceManager.getString(R.string.fileFragment_dialogFileDetailReadHiddenTrue)
                }else{
                    mIsHiddenValueTextView.text = ResourceManager.getString(R.string.fileFragment_dialogFileDetailReadHiddenFalse)
                }
            }
            else -> {
                mIsHiddenCategoryTextView.gone()
                mIsHiddenValueTextView.gone()
            }
        }
    }

    private fun setPath(fileList: List<File>){
        when{
            fileList.size == 1 -> {
                mPathCategoryTextView.visible()
                mPathValueTextView.visible()
                val file = fileList[0]
                mPathValueTextView.text = file.path
            }
            else -> {
                mPathCategoryTextView.gone()
                mPathValueTextView.gone()
            }
        }
    }
}