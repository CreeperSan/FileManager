package com.creepersan.file.fragment.main

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.file.R
import com.creepersan.file.activity.FragmentPageObserver
import com.creepersan.file.activity.MainActivity
import com.creepersan.file.bean.file.ApplicationInfo
import com.creepersan.file.common.view_holder.BaseViewHolder
import com.creepersan.file.extension.gone
import com.creepersan.file.extension.visible
import com.creepersan.file.manager.AsyncTask
import com.creepersan.file.manager.AsyncTaskManager
import com.creepersan.file.manager.ResourceManager
import kotlinx.android.synthetic.main.fragment_main_application.*

class ApplicationFragment(activityNotify: MainActivity.Controller, fragmentListObserver: FragmentPageObserver) : BaseMainFragment(activityNotify, fragmentListObserver){
    override fun getName(): String = ResourceManager.getString(R.string.applicationFragment_title)

    override fun getIcon(): Int = R.drawable.ic_applications

    override fun getLayoutID(): Int = R.layout.fragment_main_application

    private val mApplicationInfoList = ArrayList<ApplicationInfo>()
    private val mAdapter = ApplicationAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initHintView()
        initData()
        initRecyclerView()
    }

    private fun initToolbar(){
        mainApplicationToolbar.setNavigationOnClickListener {
            closeFragment()
        }
    }

    private fun initHintView(){
        mainApplicationHintView.visible()
    }

    private fun initData(){
        AsyncTaskManager.postTask(object : AsyncTask(){
            override fun runOnBackground(): ArrayList<ApplicationInfo> {
                val applicationInfoList = ArrayList<ApplicationInfo>()
                val packageManager = context?.packageManager
                for (packageInfo in packageManager?.getInstalledPackages(0)?: arrayListOf() ){
                    applicationInfoList.add(ApplicationInfo(packageInfo))
                }
                return applicationInfoList
            }

            override fun onRunOnUI(response: Any?) {
                mApplicationInfoList.clear()
                mApplicationInfoList.addAll(response as ArrayList<ApplicationInfo>)
                mainApplicationHintView.gone()
                mAdapter.notifyDataSetChanged()
            }

        })
    }

    private fun initRecyclerView(){
        mainApplicationRecyclerView.layoutManager = GridLayoutManager(context, 4)
        mainApplicationRecyclerView.adapter = mAdapter
    }




    private inner class ApplicationAdapter : RecyclerView.Adapter<ApplicationViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
            return ApplicationViewHolder(parent)
        }

        override fun getItemCount(): Int {
            return mApplicationInfoList.size
        }

        override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
            val applicationInfo = mApplicationInfoList[position]
            holder.setName(applicationInfo.name)
            holder.setIcon(applicationInfo.icon)
        }

    }

    private inner class ApplicationViewHolder(parentView:ViewGroup) : BaseViewHolder(R.layout.item_application_fragment_applicaiton, parentView){
        private val iconView = itemView.findViewById<ImageView>(R.id.itemApplicationFragmentApplicationIcon)
        private val titleView = itemView.findViewById<TextView>(R.id.itemApplicationFragmentApplication)

        fun setName(name:String){
            titleView.text = name
        }

        fun setIcon(drawable:Drawable){
            iconView.setImageDrawable(drawable)
        }
    }

}