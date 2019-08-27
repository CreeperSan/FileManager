package com.creepersan.file.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.creepersan.file.R
import com.creepersan.file.adapter.MainFragmentPagerAdapter
import com.creepersan.file.fragment.file.FileFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val mPagerAdapter = MainFragmentPagerAdapter()

    override fun getLayoutID(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewPager()
    }

    private fun initViewPager(){
        mPagerAdapter.addFragment(FileFragment())
        mPagerAdapter.addFragment(FileFragment())
        mPagerAdapter.addFragment(FileFragment())
        mainViewPager.offscreenPageLimit = Int.MAX_VALUE
        mainViewPager.adapter = mPagerAdapter
    }
}
