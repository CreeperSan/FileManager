package com.creepersan.file.fragment.main

import android.os.Bundle
import android.view.View
import com.creepersan.file.R
import com.creepersan.file.activity.CreateFileDirectoryActivity
import kotlinx.android.synthetic.main.fragment_main_file.*

class FileFragment : BaseFileFragment(){

    override fun getLayoutID(): Int {
        return R.layout.fragment_main_file
    }

    override fun getName(): String {
        return R.string.fileFragment_file_title.toResString()
    }

    override fun getIcon(): Int {
        return R.mipmap.ic_launcher_round
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
    }

    /**
     * 初始化标题栏
     */
    private fun initToolbar(){
        fileFragmentToolbar.setNavigationOnClickListener {

        }
        fileFragmentToolbar.inflateMenu(R.menu.file_fragment_toolbar)
        fileFragmentToolbar.setOnMenuItemClickListener {  menuItem ->
            when(menuItem.itemId){
                R.id.menuFileFragmentToolbarCreate -> {
                    activity().toActivity(CreateFileDirectoryActivity::class.java)
                }
            }
            true
        }
    }


}