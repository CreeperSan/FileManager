package com.creepersan.file.dialog

import android.content.Context
import android.telephony.mbms.FileInfo
import com.creepersan.file.R

class FileDetailDialog(context: Context) : BaseDialog(context, POSITION_CENTER, ANIMATION_CENTER){
    override fun getLayoutID(): Int = R.layout.dialog_file_fragment_file_detail

    fun setFileInfo(vararg fileInfo:FileInfo){

    }

    fun setFileInfo(fileInfoList:List<FileInfo>){

    }

}