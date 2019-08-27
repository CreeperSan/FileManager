package com.creepersan.file.fragment.file

import android.os.Bundle
import android.view.View
import com.creepersan.file.R
import com.creepersan.file.fragment.BaseFragment
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
    }

    private fun onBackPressed(){

    }

}