package com.creepersan.file.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.creepersan.file.R
import com.creepersan.file.bean.file.ApplicationInfo
import com.creepersan.file.common.view_holder.IncludeSimpleDialogActionViewHolder
import com.creepersan.file.common.view_holder.IncludeSimpleDialogTitleViewHolder
import com.creepersan.file.manager.FormatManager

class ApplicationDetailDialog(context: Context) : BaseDialog(context, POSITION_CENTER, ANIMATION_CENTER){
    override fun getLayoutID(): Int = R.layout.dialog_application_detail

    private val mTitleViewHolder = IncludeSimpleDialogTitleViewHolder(dialogView.findViewById(R.id.dialogApplicationFragmentTitleLayout))
    private val mActionViewHolder = IncludeSimpleDialogActionViewHolder(dialogView.findViewById(R.id.dialogApplicationFragmentActionLayout))
    private val mIconImageView = dialogView.findViewById<ImageView>(R.id.dialogApplicationFragmentIcon)
    private val mTitleTextView = dialogView.findViewById<TextView>(R.id.dialogApplicationFragmentName)
    private var mVersionTextView = dialogView.findViewById<TextView>(R.id.dialogApplicationFragmentIconVersionValue)
    private var mPackageNameTextView = dialogView.findViewById<TextView>(R.id.dialogApplicationFragmentIconPackageNameValue)
    private var mFirstInstallTimeTextView = dialogView.findViewById<TextView>(R.id.dialogApplicationFragmentIconFirstInstallTimeValue)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDialogTitle()
        initDialogAction()
    }

    private fun initDialogTitle(){
        mTitleViewHolder.setTitle("应用详情")
    }

    private fun initDialogAction(){
        mActionViewHolder.setPositiveButton("确定", View.OnClickListener {
            closeDialog()
        })
    }

    fun setApplicationInfo(applicationInfo:ApplicationInfo){
        mIconImageView.setImageDrawable(applicationInfo.icon)
        mTitleTextView.text = applicationInfo.name
        mVersionTextView.text = "${applicationInfo.versionName} ( ${applicationInfo.versionCode} )"
        mPackageNameTextView.text = applicationInfo.packageName
        mFirstInstallTimeTextView.text = FormatManager.getFormatTimeFull(applicationInfo.firstInstallTime)
    }

}