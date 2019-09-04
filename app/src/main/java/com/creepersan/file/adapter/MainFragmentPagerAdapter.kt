package com.creepersan.file.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.creepersan.file.fragment.main.BaseMainFragment
import java.util.ArrayList

class MainFragmentPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_SET_USER_VISIBLE_HINT){
    private val mFragmentList = ArrayList<BaseMainFragment>()

    fun addFragment(fragment:BaseMainFragment){
        mFragmentList.add(fragment)
        notifyDataSetChanged()
    }

    fun removeFragment(fragment:BaseMainFragment){
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

    fun getFragment(pos:Int):BaseMainFragment?{
        if (pos in 0 until mFragmentList.size){
            return mFragmentList[pos]
        }
        return null
    }

    fun getFragmentSize():Int{
        return mFragmentList.size
    }
}