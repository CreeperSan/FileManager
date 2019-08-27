package com.creepersan.file.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.creepersan.file.fragment.file.BaseFileFragment
import java.util.ArrayList

class MainFragmentPagerAdapter : PagerAdapter(){
    private val mFragmentList = ArrayList<BaseFileFragment>()

    fun addFragment(fragment:BaseFileFragment){
        mFragmentList.add(fragment)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return `object` == view
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return mFragmentList[position]
    }

}