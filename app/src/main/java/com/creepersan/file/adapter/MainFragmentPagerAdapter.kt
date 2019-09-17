package com.creepersan.file.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.creepersan.file.activity.MainFragmentListObserver
import com.creepersan.file.fragment.main.BaseMainFragment
import java.util.ArrayList

class MainFragmentPagerAdapter(fragmentManager: FragmentManager,private val observer: MainFragmentListObserver) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_SET_USER_VISIBLE_HINT),
    MainFragmentListObserver.Subscriber {

    override fun onWindowUpdate(fragmentList: ArrayList<BaseMainFragment>, index: Int) {

    }

    init {
        observer.subscribe(this)
    }

    fun init(){

    }

    fun destroy(){
        observer.unsubscribe(this)
    }

    override fun onListChange(fragmentList: ArrayList<BaseMainFragment>) {
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        return observer.getFragment(position)!!
    }

    override fun getCount(): Int {
        return observer.getSize()
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

}