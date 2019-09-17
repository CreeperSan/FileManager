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
    private val mFragmentListObserver = MainFragmentListObserver()
    private val mPagerAdapter = MainFragmentPagerAdapter(supportFragmentManager, mFragmentListObserver)
    private val mLeftDrawerAdapter = MainLeftDrawerRecyclerViewAdapter(mFragmentListObserver, mNotifier)
    private val mRightDrawerAdapter = MainRightDrawerRecyclerViewAdapter()

    override fun getLayoutID(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragment()
        initViewPager()
        initLeftDrawer()
        initRightDrawer()
        initFloatingActionButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPagerAdapter.destroy()
        mLeftDrawerAdapter.destroy()
        mRightDrawerAdapter.destroy()
    }

    private fun initFragment(){
        mFragmentListObserver.addFragment(
            FileFragment(mNotifier, mFragmentListObserver),
            FileFragment(mNotifier, mFragmentListObserver),
            FileFragment(mNotifier, mFragmentListObserver)
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
        if (mFragmentListObserver.getSize() > 0){
            mFragmentListObserver.getFragment(mainViewPager.currentItem)?.apply {
                if (this.onBackPressed()){
                    return
                }else{
                    if (mFragmentListObserver.getSize() > 1){
                        closeFragment(this)
                        return
                    }else{
                        super.onBackPressed()
                    }
                }
            }
        }else{
            super.onBackPressed()
        }
    }

    private fun closeFragment(fragment:BaseMainFragment){
        fragment.onPageClose()
        mFragmentListObserver.removeFragment(fragment)
        refreshFloatingActionButton()
    }

    internal fun refreshFloatingActionButton(){
        // 防止为空FragmentList
        if (mFragmentListObserver.getSize() <= 0){
            mainFloatingActionButton.hide()
            return
        }
        // 防止为空的Fragment
        val baseMainFragment = mFragmentListObserver.getFragment(mainViewPager.currentItem)
        // 设置
        if (baseMainFragment?.getFloatingActionButtonIsVisible() == true){
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
            mFragmentListObserver.getFragment(mPrevVisiblePage)?.onPageInvisible()
            mFragmentListObserver.getFragment(position)?.onPageVisible()
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
            if (mFragmentListObserver.getSize() <= 1){
                FileApplication.getInstance().exit()
            }else if (index >= 0 && index < mFragmentListObserver.getSize()){
                mFragmentListObserver.removeFragment(index)
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
            return mFragmentListObserver.getIndex(fragment)
        }

    }
}

class MainFragmentListObserver{
    private val mFragmentList = ArrayList<BaseMainFragment>()
    private val mSubscriberList = ArrayList<Subscriber>()

    fun getFragment(position:Int):BaseMainFragment? {
        if (position < 0 || position >= mFragmentList.size ){
            return null
        }
        return mFragmentList[position]
    }

    fun getSize():Int{
        return mFragmentList.size
    }

    fun subscribe(subscriber: Subscriber){
        if (!mSubscriberList.contains(subscriber)){
            mSubscriberList.add(subscriber)
        }
    }

    fun unsubscribe(subscriber: Subscriber){
        mSubscriberList.remove(subscriber)
    }

    fun addFragment(vararg fragment: BaseMainFragment){
        addFragment(fragment.toList())
    }

    fun removeFragment(vararg fragment:BaseMainFragment){
        removeFragment(fragment.toList())
    }

    fun removeFragment(index:Int){
        removeFragment(mFragmentList[index])
    }

    fun getIndex(fragment:BaseMainFragment):Int{
        return mFragmentList.indexOf(fragment)
    }

    private fun notifyFragmentListChange(){
        mSubscriberList.forEach {  subscriber ->
            subscriber.onListChange(mFragmentList)
        }
    }

    fun addFragment(fragmentList:List<BaseMainFragment>){
        fragmentList.forEach{ fragment ->
            if (!mFragmentList.contains(fragment)){
                mFragmentList.add(fragment)
            }
        }
        notifyFragmentListChange()
    }

    fun removeFragment(fragmentList:List<BaseMainFragment>){
        mFragmentList.removeAll(fragmentList)
        notifyFragmentListChange()
    }

    fun clearFragment(){
        mFragmentList.clear()
        notifyFragmentListChange()
    }

    fun notifyWindowUpdate(index:Int){
        mSubscriberList.forEach {  subscriber ->
            subscriber.onWindowUpdate(mFragmentList, index)
        }
    }

    interface Subscriber{
        fun onListChange(fragmentList:ArrayList<BaseMainFragment>)
        fun onWindowUpdate(fragmentList:ArrayList<BaseMainFragment>, index:Int)
    }

}
