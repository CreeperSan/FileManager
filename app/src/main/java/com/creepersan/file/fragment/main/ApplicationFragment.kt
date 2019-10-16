package com.creepersan.file.fragment.main

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.file.R
import com.creepersan.file.activity.FragmentPageObserver
import com.creepersan.file.activity.MainActivity
import com.creepersan.file.bean.file.ApplicationInfo
import com.creepersan.file.common.view_holder.BaseViewHolder
import com.creepersan.file.dialog.*
import com.creepersan.file.extension.gone
import com.creepersan.file.extension.visible
import com.creepersan.file.manager.ApplicationManager
import com.creepersan.file.manager.AsyncTask
import com.creepersan.file.manager.AsyncTaskManager
import com.creepersan.file.manager.ResourceManager
import kotlinx.android.synthetic.main.fragment_main_application.*
import java.lang.ref.WeakReference
import java.util.HashMap

class ApplicationFragment(activityNotify: MainActivity.Controller, fragmentListObserver: FragmentPageObserver) : BaseMainFragment(activityNotify, fragmentListObserver){
    override fun getName(): String = ResourceManager.getString(R.string.applicationFragment_title)

    override fun getIcon(): Int = R.drawable.ic_applications

    override fun getLayoutID(): Int = R.layout.fragment_main_application

    companion object{
        const val OPERATION_DETAIL = 0
        const val OPERATION_OPEN = 1
        const val OPERATION_BACKUP = 2
        const val OPERATION_UNINSTALL = 3
    }

    private var mIsMultiSelectMode = false
    private val mApplicationInfoList = ArrayList<ApplicationInfo>()
    private val mAdapter = ApplicationAdapter()
    private var mApplicationMoreOperationDialog : BaseBottomSelectionDialog? = null
    private var mApplicationDetailDialog : ApplicationDetailDialog? = null
    private var mSelectApplicationInfoMap = HashMap<String, ApplicationInfo>()
    private var mTmpClickApplicationInfoReference = WeakReference<ApplicationInfo>(null)

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

    private fun getBottomSheetDialog():BaseBottomSelectionDialog{
        if (mApplicationMoreOperationDialog == null){
            mApplicationMoreOperationDialog = BaseBottomSelectionDialog(activity()).setItemList(arrayListOf(
                BaseBottomSelectionDialogItem(OPERATION_OPEN, R.drawable.ic_exit, "打开"),
                BaseBottomSelectionDialogItem(OPERATION_DETAIL, R.drawable.ic_info_outline, "详情"),
                BaseBottomSelectionDialogItem(OPERATION_BACKUP, R.drawable.ic_backup, "备份"),
                BaseBottomSelectionDialogItem(OPERATION_UNINSTALL, R.drawable.ic_delete, "卸载", titleTextColor = Color.RED, iconTintColor = Color.RED)
            )).setItemClickListener(object : BaseBottomSelectItemClickListener{
                override fun onItemClick(id: Int, item: BaseBottomSelectionDialogItem, dialog: BaseDialog) {
                    val applicationInfo = mTmpClickApplicationInfoReference.get() ?: return
                    when(id){
                        OPERATION_OPEN -> {
                            ApplicationManager.openApplication(applicationInfo) { e ->
                                showToast("此应用不支持打开")
                            }
                            getBottomSheetDialog().closeDialog()
                        }
                        OPERATION_DETAIL -> {
                            getDetailDialog(applicationInfo).show()
                            getBottomSheetDialog().closeDialog()
                        }
                        OPERATION_BACKUP -> {
                            getBottomSheetDialog().closeDialog()
                        }
                        OPERATION_UNINSTALL -> {
                            getBottomSheetDialog().closeDialog()
                        }
                    }
                }
            })
        }
        return mApplicationMoreOperationDialog!!
    }

    private fun getDetailDialog(info:ApplicationInfo? = null):ApplicationDetailDialog{
        if (mApplicationDetailDialog == null){
            mApplicationDetailDialog = ApplicationDetailDialog(activity())
        }
        info?.apply {
            mApplicationDetailDialog?.setApplicationInfo(this)
        }
        return mApplicationDetailDialog!!
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
            holder.setOnClickListener(View.OnClickListener {
                mTmpClickApplicationInfoReference = WeakReference(applicationInfo)
                getBottomSheetDialog().showDialog()
            })
            holder.setOnLongClickListener(View.OnLongClickListener {
                true
            })

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

        fun setOnClickListener(listener: View.OnClickListener){
            itemView.setOnClickListener(listener)
        }

        fun setOnLongClickListener(listener: View.OnLongClickListener){
            itemView.setOnLongClickListener(listener)
        }

    }

}