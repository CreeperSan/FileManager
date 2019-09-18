package com.creepersan.file.activity

import android.content.Context
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.creepersan.file.R
import com.creepersan.file.adapter.MainFragmentPagerAdapter
import com.creepersan.file.adapter.MainLeftDrawerRecyclerViewAdapter
import com.creepersan.file.adapter.MainRightDrawerRecyclerViewAdapter
import com.creepersan.file.application.FileApplication
import com.creepersan.file.fragment.main.BaseMainFragment
import com.creepersan.file.fragment.main.FileFragment
import com.creepersan.file.global.GlobalFileInfoClipBoard
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : BaseActivity(), ViewPager.OnPageChangeListener {
    private val mNotifier = Controller()
    private val mPagerAdapter by lazy { MainFragmentPagerAdapter(supportFragmentManager, mFragmentPageObserver) }
    private val mLeftDrawerAdapter by lazy { MainLeftDrawerRecyclerViewAdapter(mFragmentPageObserver, mNotifier) }
    private val mRightDrawerAdapter = MainRightDrawerRecyclerViewAdapter()
    private val mFragmentPageObserver = FragmentPageObserver()

    override fun getLayoutID(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewPager()
        initLeftDrawer()
        initRightDrawer()
        initFloatingActionButton()
        initFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPagerAdapter.destroy()
        mLeftDrawerAdapter.destroy()
        mRightDrawerAdapter.destroy()
    }

    private fun initFragment(){
        mFragmentPageObserver.addFragment(
            FileFragment(mNotifier, mFragmentPageObserver),
            FileFragment(mNotifier, mFragmentPageObserver),
            FileFragment(mNotifier, mFragmentPageObserver)
        )
    }

    private fun initViewPager(){
        mainViewPager.offscreenPageLimit = Int.MAX_VALUE
        mainViewPager.adapter = mPagerAdapter

        mainViewPager.addOnPageChangeListener(this)
    }

    private fun initLeftDrawer(){
        mLeftDrawerAdapter.initBaseData()
        mainLeftDrawerRecyclerView.layoutManager = LinearLayoutManager(this)
        mainLeftDrawerRecyclerView.adapter = mLeftDrawerAdapter
    }

    private fun initRightDrawer(){
        mainRightDrawerRecyclerView.layoutManager = LinearLayoutManager(this)
        mainRightDrawerRecyclerView.adapter = mRightDrawerAdapter

        mainRightDrawerClearAll.setOnClickListener {
            GlobalFileInfoClipBoard.clearFileInfo()
        }
    }

    private fun initFloatingActionButton(){
        refreshFloatingActionButton()
    }

    override fun onBackPressed() {
        if (mFragmentPageObserver.getSize() > 0){
            val fragment = mFragmentPageObserver.getFragment(mainViewPager.currentItem)
            if (fragment.onBackPressed()){
                return
            }else{
                if (mFragmentPageObserver.getSize() > 1){
                    closeFragment(fragment)
                    return
                }else{
                    super.onBackPressed()
                }
            }
        }else{
            super.onBackPressed()
        }
    }

    private fun closeFragment(fragment:BaseMainFragment){
        fragment.onPageClose()
        mFragmentPageObserver.removeFragment(fragment)
        refreshFloatingActionButton()
    }

    internal fun refreshFloatingActionButton(){
        // 防止为空FragmentList
        if (mFragmentPageObserver.getSize() <= 0){
            mainFloatingActionButton.hide()
            return
        }
        // 防止为空的Fragment
        val baseMainFragment = mFragmentPageObserver.getFragment(mainViewPager.currentItem)
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

    private var mPrevVisiblePage = 0
    override fun onPageScrollStateChanged(state: Int) {

    }
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }
    override fun onPageSelected(position: Int) {
        if (mPrevVisiblePage != position){
            mFragmentPageObserver.getFragment(mPrevVisiblePage).onPageInvisible()
            mFragmentPageObserver.getFragment(position).onPageVisible()
            mPrevVisiblePage = position
        }
        refreshFloatingActionButton()
    }

    /* 通知MainActivity操作的类 */
    inner class Controller{
        fun context():Context{
            return this@MainActivity
        }

        fun notifyFloatingActionButtonChange(){
            refreshFloatingActionButton()
        }

        fun setCurrentPage(index:Int){
            mainViewPager.setCurrentItem(index, true)
        }

        fun closePage(index:Int){
            if (mFragmentPageObserver.getSize() <= 1){
                FileApplication.getInstance().exit()
            }else if (index >= 0 && index < mFragmentPageObserver.getSize()){
                mFragmentPageObserver.removeFragment(index)
            }
        }

        fun closePage(fragment:BaseMainFragment){
            closeFragment(fragment)
        }

        fun closeLeftDrawer(){
            mainDrawerLayout.closeDrawer(GravityCompat.START)
        }

        fun closeRightDrawer(){
            mainDrawerLayout.closeDrawer(GravityCompat.END)
        }

        fun getSelfIndex(fragment:BaseMainFragment):Int{
            return mFragmentPageObserver.getIndex(fragment)
        }

    }
}

class FragmentPageObserver{
    private val mFragmentList = ArrayList<BaseMainFragment>()
    private val mSubscriberList = ArrayList<FragmentPagerSubscriber>()

    fun getFragment(position:Int):BaseMainFragment = mFragmentList[position]

    fun getSize():Int = mFragmentList.size

    fun getIndex(fragment:BaseMainFragment):Int = mFragmentList.indexOf(fragment)

    fun addFragment(vararg fragment: BaseMainFragment){
        mFragmentList.addAll(fragment)
        notifyFragmentListChange()
    }

    fun removeFragment(vararg fragment:BaseMainFragment){
        mFragmentList.removeAll(fragment.toList())
        notifyFragmentListChange()
    }

    fun removeFragment(index:Int){
        removeFragment(mFragmentList[index])
        notifyFragmentListChange()
    }

    fun clearFragment(){
        mFragmentList.clear()
        notifyFragmentListChange()
    }

    private fun notifyFragmentListChange(){
        mSubscriberList.forEach {  subscriber ->
            subscriber.onPageChange(this)
        }
    }

    fun notifyFragmentUpdate(index:Int){
        mSubscriberList.forEach {  subscriber ->
            subscriber.onPageUpdate(index, this)
        }
    }

    fun subscribe(subscriber:FragmentPagerSubscriber) = mSubscriberList.add(subscriber)

    fun unsubscribe(subscriber:FragmentPagerSubscriber) = mSubscriberList.remove(subscriber)


}

interface FragmentPagerSubscriber{

    fun onPageUpdate(index:Int, observer:FragmentPageObserver)

    fun onPageChange(observer:FragmentPageObserver)

}
