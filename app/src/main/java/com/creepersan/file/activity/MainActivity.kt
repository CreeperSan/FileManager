package com.creepersan.file.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.creepersan.file.R
import com.creepersan.file.adapter.MainFragmentPagerAdapter
import com.creepersan.file.adapter.MainLeftDrawerRecyclerViewAdapter
import com.creepersan.file.adapter.MainRightDrawerRecyclerViewAdapter
import com.creepersan.file.fragment.BaseFragment
import com.creepersan.file.fragment.main.BaseMainFragment
import com.creepersan.file.fragment.main.FileFragment
import com.creepersan.file.global.GlobalClipBoard
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : BaseActivity(), ViewPager.OnPageChangeListener {

    private val mFragmentListObserver = MainFragmentListObserver()
    private val mPagerAdapter = MainFragmentPagerAdapter(supportFragmentManager, mFragmentListObserver)
    private val mLeftDrawerAdapter = MainLeftDrawerRecyclerViewAdapter(mPagerAdapter, mFragmentListObserver)
    private val mRightDrawerAdapter = MainRightDrawerRecyclerViewAdapter()
    private val mNotifier = MainActivityNotifier()

    override fun getLayoutID(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragment()
        initViewPager()
        initLeftDrawer()
        initRightDrawer()
        initFloatingActionButton()
    }

    private fun initFragment(){
        mFragmentListObserver.addFragment(FileFragment(), FileFragment(), FileFragment())
    }

    private fun initViewPager(){
        mainViewPager.offscreenPageLimit = Int.MAX_VALUE
        mainViewPager.adapter = mPagerAdapter

        mainViewPager.addOnPageChangeListener(this)
    }

    private fun initLeftDrawer(){
        mainLeftDrawerRecyclerView.layoutManager = LinearLayoutManager(this)
        mainLeftDrawerRecyclerView.adapter = mLeftDrawerAdapter
    }

    private fun initRightDrawer(){
        mainRightDrawerRecyclerView.layoutManager = LinearLayoutManager(this)
        mainRightDrawerRecyclerView.adapter = mRightDrawerAdapter

        mainRightDrawerClearAll.setOnClickListener {
            GlobalClipBoard.cleatFileInfo()
        }
    }

    private fun initFloatingActionButton(){
        refreshFloatingActionButton()
    }

    override fun onBackPressed() {
        mFragmentListObserver.getFragment(mainViewPager.currentItem).apply {
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
    }

    internal fun closeFragment(fragment:BaseMainFragment){
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

    /* 通知MainActivity操作的类 */
    inner class MainActivityNotifier{

        fun notifyFloatingActionButtonChange(){
            refreshFloatingActionButton()
        }

    }
}

class MainFragmentListObserver{
    private val mFragmentList = ArrayList<BaseMainFragment>()
    private val mSubscriberList = ArrayList<Subscriber>()

    fun getFragment(position:Int):BaseMainFragment {
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

    interface Subscriber{
        fun onListChange(fragmentList:ArrayList<BaseMainFragment>)
    }

}
