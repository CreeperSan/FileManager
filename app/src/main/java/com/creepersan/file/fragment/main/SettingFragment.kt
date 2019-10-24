package com.creepersan.file.fragment.main

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.creepersan.file.R
import com.creepersan.file.activity.FragmentPageObserver
import com.creepersan.file.activity.MainActivity
import com.creepersan.file.bean.setting.CategorySettingItem
import com.creepersan.file.bean.setting.CheckBoxSettingItem
import com.creepersan.file.fragment.main.setting.SettingListAdapter
import com.creepersan.file.manager.ConfigManager
import com.creepersan.file.manager.ResourceManager
import kotlinx.android.synthetic.main.fragment_main_setting.*

class SettingFragment(activityNotify: MainActivity.Controller, fragmentListObserver: FragmentPageObserver) : BaseMainFragment(activityNotify, fragmentListObserver){
    override fun getName(): String = ResourceManager.getString(R.string.settingFragment_title)

    override fun getIcon(): Int = R.drawable.ic_setting

    override fun getLayoutID(): Int = R.layout.fragment_main_setting

    private val mAdapter = SettingListAdapter()

    companion object{
        private const val FILE = 100
        private const val FILE_SHOW_HIDDEN = 101
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initRecyclerView()
        initData()
    }

    private fun initToolbar(){
        mainSettingToolbar.setNavigationOnClickListener {
            closeFragment()
        }
    }

    private fun initRecyclerView(){
        mainSettingRecyclerView.layoutManager = LinearLayoutManager(activity)
        mainSettingRecyclerView.adapter = mAdapter
    }

    private fun initData(){
        mAdapter.addItem(
            CategorySettingItem(FILE_SHOW_HIDDEN, ResourceManager.getString(R.string.settingFragment_fileSetting)),
            CheckBoxSettingItem(FILE_SHOW_HIDDEN, ResourceManager.getString(R.string.settingFragment_fileSetting_showHiddenFile), ConfigManager.getGlobalShowHiddenFile(),
                icon = R.drawable.ic_eye_black,
                description = ResourceManager.getString(R.string.settingFragment_fileSetting_showHiddenFileDescription),
                onSelectChange = { _, newValue, pos ->
                    ConfigManager.setGlobalShowHiddenFile(newValue)
                    mAdapter.notifyItemChanged(pos)
                }
            )
        )
    }

}