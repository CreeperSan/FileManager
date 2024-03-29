package com.creepersan.file.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.creepersan.file.activity.FragmentPageObserver
import com.creepersan.file.activity.FragmentPagerSubscriber
import com.creepersan.file.fragment.main.BaseMainFragment
import java.util.ArrayList

class MainFragmentPagerAdapter(fragmentManager: FragmentManager,private val observer: FragmentPageObserver) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_SET_USER_VISIBLE_HINT),
    FragmentPagerSubscriber {

    override fun onPageUpdate(index: Int, observer: FragmentPageObserver) {

    }

    override fun onPageChange(observer: FragmentPageObserver) {
        notifyDataSetChanged()
    }

    init {
        observer.subscribe(this)
    }

    fun init(){

    }

    fun destroy(){
        observer.unsubscribe(this)
    }

    override fun getItem(position: Int): Fragment {
        return observer.getFragment(position)
    }

    override fun getCount(): Int {
        return observer.getSize()
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

}