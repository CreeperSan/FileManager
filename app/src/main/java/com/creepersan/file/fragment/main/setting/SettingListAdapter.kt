package com.creepersan.file.fragment.main.setting

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.file.bean.setting.BaseSettingItem
import com.creepersan.file.bean.setting.CategorySettingItem
import com.creepersan.file.bean.setting.CheckBoxSettingItem
import java.lang.Exception
import java.util.ArrayList

class SettingListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object{
        private const val TYPE_CATEGORY = 0
        private const val TYPE_CHECKBOX = 1
    }

    private val mSettingItemList = ArrayList<BaseSettingItem>()

    override fun getItemViewType(position: Int): Int {
        return when(mSettingItemList[position]){
            is CategorySettingItem -> TYPE_CATEGORY
            is CheckBoxSettingItem -> TYPE_CHECKBOX
            else -> throw Exception("未定義的設置類型")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_CATEGORY -> SettingCategoryViewHolder(parent)
            TYPE_CHECKBOX -> SettingCheckBoxViewHolder(parent)
            else -> throw Exception("未定義的設置類型")
        }
    }

    fun addItem(vararg item:BaseSettingItem){
        mSettingItemList.addAll(item)
    }

    fun clear(){
        mSettingItemList.clear()
    }

    override fun getItemCount(): Int = mSettingItemList.size

    override fun onBindViewHolder(tmpHolder: RecyclerView.ViewHolder, position: Int) {
        when(val item = mSettingItemList[position]){
            is CategorySettingItem -> {
                val holder = tmpHolder as SettingCategoryViewHolder
                holder.setText(item.name)
            }
            is CheckBoxSettingItem -> {
                val holder = tmpHolder as SettingCheckBoxViewHolder
                holder.setText(item.name)
                holder.setDescription(item.description)
                holder.setIcon(item.icon)
                holder.setCheckBoxCheck(item.value)
                if (item.onSelectChange == null){
                    holder.setOnClickListener(null)
                }else{
                    holder.setOnClickListener(View.OnClickListener{ v->
                        item.value = !item.value
                        notifyItemChanged(holder.adapterPosition)
                        item.onSelectChange?.invoke(!item.value, item.value, position)
                    })
                }
            }
        }
    }


}