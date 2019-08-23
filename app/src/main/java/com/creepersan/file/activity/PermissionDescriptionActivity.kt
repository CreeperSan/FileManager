package com.creepersan.file.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.file.R
import com.yanzhenjie.permission.runtime.Permission

class PermissionDescriptionActivity : BaseActivity() {

    companion object{
        val PERMISSION_STORAGE = Permission.Group.STORAGE

        const val INTENT_KEY_PERMISSION = "permission"
    }

    override fun getLayoutID(): Int = R.layout.activity_permission_description

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    /**
     * 权限的 Model
     */
    private data class PermissionItem(val permission:Int, val iconID: Int, val title: String, val content: String)

    /**
     * 权限的列表适配器
     */
    private class PermissionAdapter(val permissionList:List<PermissionItem>) : RecyclerView.Adapter<PermissionHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermissionHolder = PermissionHolder(parent)

        override fun getItemCount(): Int = permissionList.size

        override fun onBindViewHolder(holder: PermissionHolder, position: Int) {
            val permissionItem = permissionList[position]
            holder.setPermission(permissionItem.iconID, permissionItem.title, permissionItem.content)
        }

    }

    /**
     * 权限文本ViewHolder
     */
    private class PermissionHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_permission_description, parent, false)) {
        private val iconView = itemView.findViewById<ImageView>(R.id.itemPermissionDescriptionIcon)
        private val titleView = itemView.findViewById<TextView>(R.id.itemPermissionDescriptionTitle)
        private val contentView = itemView.findViewById<TextView>(R.id.itemPermissionDescriptionContent)

        fun setPermission(iconID:Int, title:String, content:String){
            iconView.setImageResource(iconID)
            titleView.text = title
            contentView.text = content
        }
    }

}