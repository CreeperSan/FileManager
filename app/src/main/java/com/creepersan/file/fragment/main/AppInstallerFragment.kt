package com.creepersan.file.fragment.main

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.file.R
import com.creepersan.file.action.application_picker.ApplicationPickerActivity
import com.creepersan.file.activity.FragmentPageObserver
import com.creepersan.file.activity.MainActivity
import com.creepersan.file.common.view_holder.BaseViewHolder
import com.creepersan.file.dialog.ApplicationSelectDialog
import com.creepersan.file.extension.gone
import com.creepersan.file.extension.visible
import com.creepersan.file.res.RequestCode
import kotlinx.android.synthetic.main.fragment_main_app_installer.*
import java.util.ArrayList

class AppInstallerFragment(activityNotify: MainActivity.Controller, fragmentListObserver: FragmentPageObserver) : BaseMainFragment(activityNotify, fragmentListObserver) {

    override fun getName(): String {
        return "应用安装器"
    }

    override fun getIcon(): Int {
        return R.drawable.ic_fragment_app_installer
    }

    override fun getLayoutID(): Int {
        return R.layout.fragment_main_app_installer
    }

    private val mActionList = ArrayList<ActionApplication>()
    private val mAdapter = ItemAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initActionButton()
    }

    private fun initRecyclerView(){
        appInstallerRecyclerView.layoutManager = GridLayoutManager(activity, 3)
        appInstallerRecyclerView.adapter = mAdapter
    }

    private fun initActionButton(){
        appInstallerInstall.setOnClickListener {

        }
        appInstallerUninstall.setOnClickListener{

            startActivityForResult(Intent(activity, ApplicationPickerActivity::class.java).apply {
                putExtra(ApplicationPickerActivity.INTENT_TITLE, "选择要卸载的应用")
                putExtra(ApplicationPickerActivity.INTENT_FILTER, arrayOf(
                    ApplicationPickerActivity.FILTER_USER,
                    ApplicationPickerActivity.FILTER_ENABLE
                ))
            }, RequestCode.APPLICATION_PICKER)

//            ApplicationSelectDialog(activity)
//                .showDialog()
        }
        appInstallerProcess.setOnClickListener {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }



































    private open class ActionApplication(val name:String, val icon:Bitmap)
    private class InstallApplication(val path:String, name:String, icon: Bitmap) : ActionApplication(name, icon)
    private class UninstallApplication(val packageName:String, name:String, icon:Bitmap) : ActionApplication(name, icon)
    private class ItemViewHolder(parent:ViewGroup) : BaseViewHolder(R.layout.item_app_installer, parent){
        private val nameTextView = itemView.findViewById<TextView>(R.id.itemAppInstallerName)
        private val iconImageView = itemView.findViewById<ImageView>(R.id.itemAppInstallerIcon)
        private val hintIconImageView = itemView.findViewById<ImageView>(R.id.itemAppInstallerHintIcon)

        fun setIcon(icon:Bitmap){
            iconImageView.setImageBitmap(icon)
        }

        fun setIsInstall(isInstall:Boolean){
            if (isInstall){
                hintIconImageView.gone()
            }else{
                hintIconImageView.visible()
            }
        }

        fun setName(name:String){
            nameTextView.text = name
        }

        fun set0nClickListener(listener:View.OnClickListener?){
            itemView.setOnClickListener(listener)
        }

        fun set0nLongClickListener(listener:View.OnLongClickListener?){
            itemView.setOnLongClickListener(listener)
        }
    }
    private inner class ItemAdapter : RecyclerView.Adapter<ItemViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            return ItemViewHolder(parent)
        }

        override fun getItemCount(): Int {
            return mActionList.size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val item = mActionList[position]
            holder.setName(item.name)
            holder.setIcon(item.icon)
            when(item){
                is InstallApplication -> {
                    holder.setIsInstall(true)
                }
                is UninstallApplication -> {
                    holder.setIsInstall(false)
                }
                else -> {
                    holder.setIsInstall(false)
                }
            }
        }

    }

}