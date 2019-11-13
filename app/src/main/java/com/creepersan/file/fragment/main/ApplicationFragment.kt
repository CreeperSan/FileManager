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
import com.creepersan.file.bean.file.FileInfo
import com.creepersan.file.common.view_holder.BaseViewHolder
import com.creepersan.file.dialog.*
import com.creepersan.file.extension.gone
import com.creepersan.file.extension.visible
import com.creepersan.file.global.GlobalApplicationInfo
import com.creepersan.file.global.GlobalFileInfoClipBoard
import com.creepersan.file.manager.*
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

    private val mApplicationInfoList = ArrayList<ApplicationInfo>()
    private val mAdapter = ApplicationAdapter()
    private var mApplicationMoreOperationDialog : BaseBottomSelectionDialog? = null
    private var mApplicationDetailDialog : ApplicationDetailDialog? = null
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
        GlobalApplicationInfo.getAllApplicationInfo(activity, object : GlobalApplicationInfo.ApplicationInfoListener{
            override fun onGetData(applicationInfoList: java.util.ArrayList<ApplicationInfo>) {
                mApplicationInfoList.clear()
                mApplicationInfoList.addAll(applicationInfoList)
                mainApplicationHintView.gone()
                mAdapter.notifyDataSetChanged()
            }
        }, true)
    }

    private fun initRecyclerView(){
        mainApplicationRecyclerView.layoutManager = GridLayoutManager(context, 4)
        mainApplicationRecyclerView.adapter = mAdapter
    }

    private fun getBottomSheetDialog():BaseBottomSelectionDialog{
        if (mApplicationMoreOperationDialog == null){
            mApplicationMoreOperationDialog = BaseBottomSelectionDialog(activity).setItemList(arrayListOf(
                BaseBottomSelectionDialogItem(OPERATION_OPEN, R.drawable.ic_exit, ResourceManager.getString(R.string.applicationFragment_menuOpen)),
                BaseBottomSelectionDialogItem(OPERATION_DETAIL, R.drawable.ic_info_outline, ResourceManager.getString(R.string.applicationFragment_menuDetail)),
                BaseBottomSelectionDialogItem(OPERATION_BACKUP, R.drawable.ic_backup, ResourceManager.getString(R.string.applicationFragment_menuBackup)),
                BaseBottomSelectionDialogItem(OPERATION_UNINSTALL, R.drawable.ic_delete, ResourceManager.getString(R.string.applicationFragment_menuDelete), titleTextColor = Color.RED, iconTintColor = Color.RED)
            )).setItemClickListener(object : BaseBottomSelectItemClickListener{
                override fun onItemClick(id: Int, item: BaseBottomSelectionDialogItem, dialog: BaseDialog) {
                    val applicationInfo = mTmpClickApplicationInfoReference.get() ?: return
                    when(id){
                        OPERATION_OPEN -> {
                            ApplicationManager.openApplication(applicationInfo) { e ->
                                showToast(ResourceManager.getString(R.string.applicationFragment_toastThisApplicationCanNotOpen))
                            }
                            getBottomSheetDialog().closeDialog()
                        }
                        OPERATION_DETAIL -> {
                            getDetailDialog(applicationInfo).show()
                            getBottomSheetDialog().closeDialog()
                        }
                        OPERATION_BACKUP -> {
                            AsyncIOTaskManager.execute(CopyMoveAsyncIOTask(arrayListOf(
                                CopyMoveAsyncIOTask.CopyMoveFileInfo(
                                    FileInfo(applicationInfo.sourceDir),
                                    GlobalFileInfoClipBoard.Action.COPY,
                                    FileInfo("${FileManager.getBackupApkDirectoryFileInfo().path}/${applicationInfo.name}_${applicationInfo.versionName}.apk")
                                )
                            )))
                            getBottomSheetDialog().closeDialog()
                        }
                        OPERATION_UNINSTALL -> {
                            ApplicationManager.uninstallApplication(activity, applicationInfo){
                                ToastManager.show(ResourceManager.getString(R.string.applicationFragment_toastCanNotUninstallThisApplication))
                            }
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
            mApplicationDetailDialog = ApplicationDetailDialog(activity)
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