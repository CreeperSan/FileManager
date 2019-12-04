package com.creepersan.file.action.application_picker

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.file.R
import com.creepersan.file.activity.BaseActivity
import com.creepersan.file.bean.file.AppInfo
import com.creepersan.file.common.view_holder.BaseViewHolder
import com.creepersan.file.global.GlobalAppInfo
import kotlinx.android.synthetic.main.activity_application_picker.*
import java.util.ArrayList
import java.util.HashSet

class ApplicationPickerActivity : BaseActivity(){

    companion object{
        const val INTENT_TITLE = "title"
        const val INTENT_FILTER = "filter"

        const val RESULT_APPLICATION_PACKAGE_NAME_ARRAY = "data"

        const val FILTER_SYSTEM = "system"
        const val FILTER_USER = "user"
        const val FILTER_DISABLE = "disable"
        const val FILTER_ENABLE = "enable"
    }


    private var mTitle = "选择应用"
    private var mFilter = arrayOf<String>()
    private val mApplicationInfoList = ArrayList<AppInfo>()
    private val mSelectApplicationSet = HashSet<AppInfo>()
    private val mAdapter = ApplicationPickerAdapter()

    override fun getLayoutID(): Int = R.layout.activity_application_picker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initIntent()
        initToolbar()
        initRecyclerView()
        initData()
        initFloatActionButton()
    }

    private fun initIntent(){
        // 初始化标题
        if (intent.hasExtra(INTENT_TITLE)){
            mTitle = intent.getStringExtra(INTENT_TITLE)
        }
        // 初始化筛选器
        if (intent.hasExtra(INTENT_FILTER)){
            mFilter = intent.getStringArrayExtra(INTENT_FILTER)
        }
    }

    private fun initToolbar(){
        applicationPickerToolbar.title = mTitle
        applicationPickerToolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun initRecyclerView(){
        applicationPickerRecyclerView.layoutManager = GridLayoutManager(this, 4)
        applicationPickerRecyclerView.adapter = mAdapter
    }

    /**
     * 筛选的逻辑如下
     *
     * 比如说需要筛选出用户应用，
     * 那么碰到这个筛选项时，如果此应用不是用户应用，则不添加。
     */
    private fun initData(){
        GlobalAppInfo.getAllApplicationInfo(this, object : GlobalAppInfo.ApplicationInfoListener{
            override fun onGetData(applicationInfoList: ArrayList<AppInfo>) {
                val filterApplicationList = ArrayList<AppInfo>()
                // 筛选
                applicationInfoList.forEach {  appInfo ->
                    for (tmpFilter in mFilter){
                        when(tmpFilter){
                            // 如果不是用户应用，则不添加
                            FILTER_USER -> {
                                if (!appInfo.isUserApplication()){
                                    return@forEach
                                }
                            }
                            // 如果不是系统应用，则不添加
                            FILTER_SYSTEM -> {
                                if (!appInfo.isSystemApplication()){
                                    return@forEach
                                }
                            }
                            // 如果不是禁用的应用，则不添加
                            FILTER_DISABLE -> {
                                if (appInfo.isEnable){
                                    return@forEach
                                }
                            }
                            // 如果不是正在使用的应用，则阿布添加
                            FILTER_ENABLE -> {
                                if (!appInfo.isEnable){
                                    return@forEach
                                }
                            }
                        }
                    }
                    filterApplicationList.add(appInfo)
                }

                mApplicationInfoList.clear()
                mApplicationInfoList.addAll(filterApplicationList)

                mAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun initFloatActionButton(){
        applicationPickerFloatActionButton.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().apply {
                val applicationInfoArray = mSelectApplicationSet.toTypedArray()
                val array = Array(applicationInfoArray.size) { index -> applicationInfoArray[index].packageName }
                putExtra(RESULT_APPLICATION_PACKAGE_NAME_ARRAY, array)
            })
            finish()
        }
    }




    private inner class ApplicationPickerAdapter : RecyclerView.Adapter<ApplicationViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
            return ApplicationViewHolder(parent)
        }

        override fun getItemCount(): Int {
            return mApplicationInfoList.size
        }

        override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
            val applicationInfo = mApplicationInfoList[position]
            holder.setName(applicationInfo.name)
            holder.setDrawable(applicationInfo.icon)
            holder.setIsSelect(mSelectApplicationSet.contains(applicationInfo))
            holder.setOnClickListener(View.OnClickListener {
                if (mSelectApplicationSet.contains(applicationInfo)){
                    mSelectApplicationSet.remove(applicationInfo)
                }else{
                    mSelectApplicationSet.add(applicationInfo)
                }
                notifyItemChanged(holder.adapterPosition)
            })
        }

    }

    private class ApplicationViewHolder(parent: ViewGroup) : BaseViewHolder(R.layout.item_application_picker, parent){
        val iconImageView = itemView.findViewById<ImageView>(R.id.itemApplicationPickerIcon)
        val nameTextView = itemView.findViewById<TextView>(R.id.itemApplicationPickerName)

        fun setName(name:String){
            nameTextView.text = name
        }

        fun setIcon(resID:Int){
            iconImageView.setImageResource(resID)
        }

        fun setDrawable(drawable:Drawable?){
            iconImageView.setImageDrawable(drawable)
        }

        fun setImageBitmap(bitmap:Bitmap?){
            iconImageView.setImageBitmap(bitmap)
        }

        fun setIsSelect(isSelect:Boolean){
            itemView.setBackgroundColor(if (isSelect){
                Color.parseColor("#CCCCCC")
            }else{
                Color.TRANSPARENT
            })
        }

        fun setOnClickListener(listener: View.OnClickListener){
            itemView.setOnClickListener(listener)
        }

    }

}