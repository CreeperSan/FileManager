package com.creepersan.file.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.creepersan.file.fragment.main.BaseFileFragment
import java.util.ArrayList

class MainFragmentPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager){
    private val mFragmentList = ArrayList<BaseFileFragment>()

    fun addFragment(fragment:BaseFileFragment){
        mFragmentList.add(fragment)
        notifyDataSetChanged()
    }

    fun removeFragment(fragment:BaseFileFragment){
        mFragmentList.remove(fragment)
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    fun getFileFragment(pos:Int):BaseFileFragment?{
        if (pos in 0 until mFragmentList.size){
            return mFragmentList[pos]
        }
        return null
    }
}