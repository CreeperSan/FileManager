package com.creepersan.file.fragment.main

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.file.R
import com.creepersan.file.activity.CreateFileDirectoryActivity
import com.creepersan.file.bean.file.FilePageInfo
import com.creepersan.file.dialog.*
import com.creepersan.file.manager.ToastManager
import kotlinx.android.synthetic.main.fragment_main_file.*
import java.lang.RuntimeException

class FileFragment : BaseFileFragment(){
    companion object{
        private const val TYPE_FILE = 0
        private const val TYPE_DIRECTORY = 1
    }

    private val mFilePageInfo = FilePageInfo()
    private val mAdapter = FileAdapter()


    override fun getLayoutID(): Int {
        return R.layout.fragment_main_file
    }

    override fun getName(): String {
        return R.string.fileFragment_file_title.toResString()
    }

    override fun getIcon(): Int {
        return R.mipmap.ic_launcher_round
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initRecyclerView()
        initFloatingActionButton()
    }

    /**
     * 初始化标题栏
     */
    private fun initToolbar(){
        fileFragmentToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        fileFragmentToolbar.inflateMenu(R.menu.file_fragment_toolbar)
        fileFragmentToolbar.setOnMenuItemClickListener {  menuItem ->
            when(menuItem.itemId){
                R.id.menuFileFragmentToolbarCreate -> {
                    activity().toActivity(CreateFileDirectoryActivity::class.java)
                }
                R.id.menuFileFragmentToolbarSearch -> {
                    FileFragmentSearchDialog(activity())
                        .show()
                }
            }
            true
        }
    }

    private fun initRecyclerView(){
        fileFragmentRecyclerView.layoutManager = FileListLayoutManager()
        fileFragmentRecyclerView.adapter = mAdapter
    }

    private fun initFloatingActionButton(){
        fileFragmentFloatingActionButton.setOnClickListener {
            BaseBottomSelectionDialog(activity())
                .setItemList(arrayListOf(
                    BaseBottomSelectionDialogItem(0, R.drawable.ic_arrow_back_dark_blue, "操作1"),
                    BaseBottomSelectionDialogItem(1, R.drawable.ic_arrow_down_dart_blue, "操作2"),
                    BaseBottomSelectionDialogItem(2, R.drawable.ic_directory_dark_blue, "操作3"),
                    BaseBottomSelectionDialogItem(3, R.drawable.ic_launcher_background, "操作4")
                ))
                .setItemClickListener(object : BaseBottomSelectItemClickListener{
                    override fun onItemClick(id: Int, item: BaseBottomSelectionDialogItem, dialog: BaseDialog) {
                        ToastManager.show("Click ${item.title}")
                    }
                })
                .show()
        }
    }

    /**********************************************************************************************/
    override fun onBackPressed(): Boolean {
        if (mFilePageInfo.canPopDirectory()){
            mFilePageInfo.popDirectory()
            mAdapter.notifyDataSetChanged()
            return true
        }else{
            return false
        }
    }









    /**********************************************************************************************/
    private inner class FileListLayoutManager : GridLayoutManager(context, 2){
        init {
            spanSizeLookup = object : SpanSizeLookup(){
                override fun getSpanSize(position: Int): Int {
                    val fileInfo = mFilePageInfo.getCurrentPageFile(position)
                    return if (fileInfo.isDirectory){
                        1
                    }else{
                        2
                    }
                }
            }
        }
    }
    private inner class FileAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        override fun getItemViewType(position: Int): Int {
            val fileInfo = mFilePageInfo.getCurrentPageFile(position)
            return if (fileInfo.isDirectory){
                TYPE_DIRECTORY
            }else{
                TYPE_FILE
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when(viewType){
                TYPE_FILE -> {
                    FileViewHolder(parent)
                }
                TYPE_DIRECTORY -> {
                    DirectoryViewHolder(parent)
                }
                else -> throw RuntimeException("不存在的文件类型")
            }
        }

        override fun getItemCount(): Int {
            val count = mFilePageInfo.getCurrentPageFileCount()
            if (count <= 0){
                fileFragmentPageHintView.visible()
                fileFragmentRecyclerView.gone()
            }else{
                fileFragmentPageHintView.gone()
                fileFragmentRecyclerView.visible()
            }
            return count
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val fileInfo = mFilePageInfo.getCurrentPageFile(position)
            when{
                holder is DirectoryViewHolder -> {
                    holder.setTitle(fileInfo.fullName)
                    holder.setInfo(fileInfo.modifyTime.toString())
                    holder.setOnClickListener(View.OnClickListener {
                        mFilePageInfo.pushDirectory(fileInfo)
                        mAdapter.notifyDataSetChanged()
                    })
                }
                holder is FileViewHolder -> {
                    holder.setTitle(fileInfo.fullName)
                    holder.setInfo(fileInfo.modifyTime.toString())
                    holder.setOnClickListener(View.OnClickListener {

                    })
                }
            }
        }

    }

    private inner class DirectoryViewHolder(parent:ViewGroup) : RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.item_file_directory, parent, false)){
        private val iconImageView = itemView.findViewById<ImageView>(R.id.itemFileDirectoryIcon)
        private val titleTextView = itemView.findViewById<TextView>(R.id.itemFileDirectoryTitle)
        private val infoTextView = itemView.findViewById<TextView>(R.id.itemFileDirectoryInfo)

        fun setTitle(title:String){
            titleTextView.text = title
        }

        fun setInfo(info:String){
            infoTextView.text = info
        }

        fun setOnClickListener(onClickListener:View.OnClickListener){
            itemView.setOnClickListener(onClickListener)
        }
    }

    private inner class FileViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.item_file_file, parent, false)){
        private val iconImageView = itemView.findViewById<ImageView>(R.id.itemFileFileIcon)
        private val titleTextView = itemView.findViewById<TextView>(R.id.itemFileFileFileName)
        private val infoTextView = itemView.findViewById<TextView>(R.id.itemFileFileFileDescription)

        fun setTitle(title:String){
            titleTextView.text = title
        }

        fun setInfo(info:String){
            infoTextView.text = info
        }

        fun setOnClickListener(onClickListener:View.OnClickListener){
            itemView.setOnClickListener(onClickListener)
        }

    }

}