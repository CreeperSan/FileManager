package com.creepersan.file.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.creepersan.file.R
import com.creepersan.file.adapter.MainFragmentPagerAdapter
import com.creepersan.file.adapter.MainLeftDrawerRecyclerViewAdapter
import com.creepersan.file.fragment.main.BaseMainFragment
import com.creepersan.file.fragment.main.FileFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), ViewPager.OnPageChangeListener {

    private val mPagerAdapter = MainFragmentPagerAdapter(supportFragmentManager)
    private val mLeftDrawerAdapter = MainLeftDrawerRecyclerViewAdapter(mPagerAdapter)

    override fun getLayoutID(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewPager()
        initLeftDrawer()
        initRightDrawer()
        initFloatingActionButton()
    }

    private fun initViewPager(){
        mPagerAdapter.addFragment(FileFragment())
        mPagerAdapter.addFragment(FileFragment())
        mPagerAdapter.addFragment(FileFragment())
        mainViewPager.offscreenPageLimit = Int.MAX_VALUE
        mainViewPager.adapter = mPagerAdapter

        mainViewPager.addOnPageChangeListener(this)
    }

    private fun initLeftDrawer(){
        mainLeftDrawerRecyclerView.layoutManager = LinearLayoutManager(this)
        mainLeftDrawerRecyclerView.adapter = mLeftDrawerAdapter
        mLeftDrawerAdapter.notifyFragmentListChange()
    }

    private fun initRightDrawer(){

    }

    private fun initFloatingActionButton(){
        refreshFloatingActionButton()
    }

    override fun onBackPressed() {
        mPagerAdapter.getFragment(mainViewPager.currentItem)?.apply {
            if (this.onBackPressed()){
                return
            }else{
                closeFragment(this)
                return
            }
        }
        super.onBackPressed()
    }

    internal fun closeFragment(fragment:BaseMainFragment){
        mPagerAdapter.removeFragment(fragment)
        refreshFloatingActionButton()
    }

    internal fun refreshFloatingActionButton(){
        // 防止为空FragmentList
        if (mPagerAdapter.getFragmentSize() <= 0){
            mainFloatingActionButton.hide()
            return
        }
        // 防止为空的Fragment
        val baseMainFragment = mPagerAdapter.getFragment(mainViewPager.currentItem)
        if (baseMainFragment == null){
            mainFloatingActionButton.hide()
            return
        }
        // 设置
        if (baseMainFragment.getFloatingActionButtonIsVisible()){
            mainFloatingActionButton.setImageResource(baseMainFragment.getFloatingActionButtonIcon())
            mainFloatingActionButton.setOnClickListener(baseMainFragment.getFloatingActionButtonClickListener())
            mainFloatingActionButton.setOnLongClickListener(baseMainFragment.getFloatingActionButtonLongClickListener())
            mainFloatingActionButton.show()
        }else{
            mainFloatingActionButton.hide()
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }
    override fun onPageSelected(position: Int) {
        refreshFloatingActionButton()
    }
}
