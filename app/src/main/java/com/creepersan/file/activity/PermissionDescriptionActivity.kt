package com.creepersan.file.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.file.R
import com.creepersan.file.manager.PermissionManager
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.activity_permission_description.*
import java.util.ArrayList

class PermissionDescriptionActivity : BaseActivity() {

    companion object{
        val PERMISSION_STORAGE = Permission.Group.STORAGE

        const val INTENT_KEY_PERMISSION = "permission"
    }

    override fun getLayoutID(): Int = R.layout.activity_permission_description

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initIntent()
    }

    private fun initIntent(){
        if (!intent.hasExtra(INTENT_KEY_PERMISSION)){ // 如果没有提交需要什么权限的话，就直接结束
            setResult(Activity.RESULT_OK)
            finish()
        }else{ // 如果有的话，就取出数据
            val permissionArray = intent.getStringArrayExtra(INTENT_KEY_PERMISSION)
            if (permissionArray == null || permissionArray.isEmpty()){
                finish()
                return
            }
            initLater(permissionArray)
        }
    }

    /**
     * 初始化接下来的内容
     * @param permissionArray 要请求的权限
     */
    private fun initLater(permissionArray:Array<String>){
        initSystemBarColor()
        initList(permissionArray)
        initButton()
    }

    /**
     * 设置状态栏文字图标颜色为黑色
     */
    private fun initSystemBarColor(){
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }

    /**
     * 初始化列表
     * @param permissionArray 要请求的权限
     */
    private lateinit var mAdapter : PermissionAdapter
    private fun initList(permissionArray:Array<String>){
        val permissionList = ArrayList<PermissionItem>()
        permissionArray.forEach {  permissionString ->
            permissionList.add(parsePermissionItem(permissionString))
        }
        mAdapter = PermissionAdapter(permissionList)

        permissionDescriptionPermissionList.layoutManager = LinearLayoutManager(this)
        permissionDescriptionPermissionList.adapter = mAdapter
    }

    /**
     * 列表权限文本转权限列表实体类
     */
    private fun parsePermissionItem(permission:String) : PermissionItem{
        return when(permission){
            Permission.WRITE_EXTERNAL_STORAGE -> {
                PermissionItem(permission, R.drawable.ic_permission_write_storage, R.string.permission_titleWriteStorage.toResString(), R.string.permission_contentWriteStorage.toResString())
            }
            Permission.READ_EXTERNAL_STORAGE -> {
                PermissionItem(permission, R.drawable.ic_permission_read_storage, R.string.permission_titleReadStorage.toResString(), R.string.permission_contentReadStorage.toResString())
            }
            else -> {
                PermissionItem(permission, R.drawable.ic_permission_unknown, R.string.permission_titleUnknown.toResString(), R.string.permission_contentUnknown.toResString())
            }
        }
    }

    /**
     * 初始化按钮
     */
    private fun initButton(){
        permissionDescriptionAllowAccessButton.setOnClickListener {
            PermissionManager.requestPermission(this, { // success
                setResult(Activity.RESULT_OK)
                finish()
            }, null, mAdapter.getPermissionArray())
        }
    }

    /**
     * 返回事件
     */
    override fun onBackPressed() {}

    /**
     * 权限的 Model
     */
    private data class PermissionItem(val permissionString:String, val iconID: Int, val title: String, val content: String)

    /**
     * 权限的列表适配器
     */
    private class PermissionAdapter(private val permissionList:List<PermissionItem>) : RecyclerView.Adapter<PermissionHolder>(){

        fun getPermissionArray() : Array<String>{
            val array = Array<String>(permissionList.size) { "" }
            permissionList.forEachIndexed { index, permissionItem ->
                array[index] = permissionItem.permissionString
            }
            return array
        }

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