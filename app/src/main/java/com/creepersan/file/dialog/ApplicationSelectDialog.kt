package com.creepersan.file.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.file.R
import com.creepersan.file.bean.file.ApplicationInfo
import com.creepersan.file.common.view_holder.IncludeSimpleDialogActionViewHolder
import com.creepersan.file.common.view_holder.IncludeSimpleDialogTitleViewHolder
import com.creepersan.file.extension.gone
import com.creepersan.file.extension.visible
import com.creepersan.file.fragment.main.ApplicationFragment
import com.creepersan.file.global.GlobalApplicationInfo
import com.creepersan.file.view.common.PageHintView
import kotlinx.android.synthetic.main.dialog_application_select.*
import java.util.ArrayList
import java.util.HashSet

class ApplicationSelectDialog(
    context: Context
) : BaseDialog(context, POSITION_CENTER, ANIMATION_CENTER) {
    private val mIncludeActionButton = IncludeSimpleDialogActionViewHolder(dialogView.findViewById(R.id.dialogApplicationActionButtonLayout))
    private val mAdapter by lazy { ApplicationSelectDialogAdapter() }

    private val mApplicationInfoList = ArrayList<ApplicationInfo>()

    private val mSelectSet = HashSet<ApplicationInfo>()

    override fun getLayoutID(): Int {
        return R.layout.dialog_application_select
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActionButton()
        initRecyclerView()
        initHint()
        initTitle()
        initData()
    }

    private fun initActionButton(){
        mIncludeActionButton.setPositiveButton("确定", View.OnClickListener {
            closeDialog()
        })
    }

    private fun initRecyclerView(){
        dialogApplicationRecyclerView.layoutManager = GridLayoutManager(context, 3)
        dialogApplicationRecyclerView.adapter = mAdapter
    }

    private fun initHint(){
        dialogApplicationPageHint.visible()
        dialogApplicationRecyclerView.gone()

        dialogApplicationPageHint.setHintText("加载中")
    }

    private fun initTitle(){
        IncludeSimpleDialogTitleViewHolder(dialogApplicationTitle).setTitle("选择应用")
    }

    private fun initData(){
        GlobalApplicationInfo.getAllApplicationInfo(context, object : GlobalApplicationInfo.ApplicationInfoListener{
            override fun onGetData(applicationInfoList: ArrayList<ApplicationInfo>) {
                dialogApplicationPageHint.gone()
                dialogApplicationRecyclerView.visible()

                mSelectSet.clear()
                mApplicationInfoList.clear()
                mApplicationInfoList.addAll(applicationInfoList)
            }
        })
    }








    private inner class ApplicationSelectDialogAdapter : RecyclerView.Adapter<ApplicationFragment.ApplicationViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationFragment.ApplicationViewHolder {
            return ApplicationFragment.ApplicationViewHolder(parent)
        }

        override fun getItemCount(): Int {
            return mApplicationInfoList.size
        }

        override fun onBindViewHolder(holder: ApplicationFragment.ApplicationViewHolder, position: Int) {
            val item = mApplicationInfoList[position]
            holder.setName(item.name)
            holder.setIcon(item.icon)
            holder.setCheck(mSelectSet.contains(item))
            holder.setOnClickListener(View.OnClickListener {
                if (mSelectSet.contains(item)){
                    mSelectSet.remove(item)
                }else{
                    mSelectSet.add(item)
                }
                mAdapter.notifyDataSetChanged()
            })
        }

    }

}