package com.creepersan.file.fragment.main

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.file.R
import com.creepersan.file.activity.CreateFileDirectoryActivity
import com.creepersan.file.bean.file.FileInfo
import com.creepersan.file.bean.file.FilePageInfo
import com.creepersan.file.dialog.*
import com.creepersan.file.global.GlobalClipBoard
import com.creepersan.file.manager.FormatManager
import com.creepersan.file.manager.ResourceManager
import com.creepersan.file.manager.ToastManager
import kotlinx.android.synthetic.main.fragment_main_file.*
import java.lang.RuntimeException
import java.util.*

class FileFragment : BaseMainFragment(){
    companion object{
        private const val TYPE_FILE = 0
        private const val TYPE_DIRECTORY = 1

        private const val DIALOG_SELECTION_INFO = 0
        private const val DIALOG_SELECTION_COPY = 1
        private const val DIALOG_SELECTION_COPY_APPEND = 6
        private const val DIALOG_SELECTION_PASTE = 2
        private const val DIALOG_SELECTION_CUT = 3
        private const val DIALOG_SELECTION_DELETE = 4
        private const val DIALOG_SELECTION_RENAME = 5
    }

    private var isSelecting = false

    private val mSelectedPathFileInfoListMap = LinkedHashMap<String, FileInfo>()
    private val mFilePageInfo = FilePageInfo()
    private val mAdapter = FileAdapter()
    private lateinit var mSelectMoreOperationDialog : BaseBottomSelectionDialog
    private lateinit var mSearchDialog : FileFragmentSearchDialog


    override fun getLayoutID(): Int {
        return R.layout.fragment_main_file
    }

    override fun getName(): String {
        return R.string.fileFragment_title.toResString()
    }

    override fun getIcon(): Int {
        return R.drawable.ic_directory_dark_blue
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDialog()
        initToolbar()
        initRecyclerView()
    }

    private fun initDialog(){
        // 底部弹出的文件操作对话框
        mSelectMoreOperationDialog = BaseBottomSelectionDialog(mainActivity())
            .setItemList(arrayListOf(
                BaseBottomSelectionDialogItem(DIALOG_SELECTION_COPY, R.drawable.ic_content_copy, ResourceManager.getString(R.string.fileFragment_dialogOperationCopy)),
                BaseBottomSelectionDialogItem(DIALOG_SELECTION_COPY_APPEND, R.drawable.ic_content_copy, ResourceManager.getString(R.string.fileFragment_dialogOperationCopyAppend)),
                BaseBottomSelectionDialogItem(DIALOG_SELECTION_CUT, R.drawable.ic_content_cut, ResourceManager.getString(R.string.fileFragment_dialogOperationCut)),
                BaseBottomSelectionDialogItem(DIALOG_SELECTION_PASTE, R.drawable.ic_content_paste, ResourceManager.getString(R.string.fileFragment_dialogOperationPaste)),
                BaseBottomSelectionDialogItem(DIALOG_SELECTION_DELETE, R.drawable.ic_delete, ResourceManager.getString(R.string.fileFragment_dialogOperationDelete)),
                BaseBottomSelectionDialogItem(DIALOG_SELECTION_RENAME, R.drawable.ic_rename, ResourceManager.getString(R.string.fileFragment_dialogOperationRename)),
                BaseBottomSelectionDialogItem(DIALOG_SELECTION_INFO, R.drawable.ic_info, ResourceManager.getString(R.string.fileFragment_dialogOperationDetail))
            ))
            .setItemClickListener(object : BaseBottomSelectItemClickListener{
                override fun onItemClick(id: Int, item: BaseBottomSelectionDialogItem, dialog: BaseDialog) {
                    ToastManager.show("Click ${item.title}")
                    when(id){
                        DIALOG_SELECTION_COPY -> {
                            GlobalClipBoard.setFileInfo(mSelectedPathFileInfoListMap.values.toList())
                            setNotSelecting()
                        }
                    }
                }
            })
        // 上面弹出的搜索对话框
        mSearchDialog = FileFragmentSearchDialog(mainActivity())
    }

    /**
     * 初始化标题栏
     */
    private fun initToolbar(){
        fileFragmentToolbar.setNavigationOnClickListener {
            if (!onBackPressed()){
                closeFragment()
            }
        }
        fileFragmentToolbar.inflateMenu(R.menu.file_fragment_toolbar)
        fileFragmentToolbar.setOnMenuItemClickListener {  menuItem ->
            when(menuItem.itemId){
                R.id.menuFileFragmentToolbarCreate -> {
                    mainActivity().toActivity(CreateFileDirectoryActivity::class.java)
                }
                R.id.menuFileFragmentToolbarSearch -> {
                    mSearchDialog.show()
                }
            }
            true
        }
        refreshToolbarTitle()
    }

    private fun initRecyclerView(){
        fileFragmentRecyclerView.layoutManager = FileListLayoutManager()
        fileFragmentRecyclerView.adapter = mAdapter
    }

    private fun refreshToolbarTitle(){
        if (isSelecting){
            fileFragmentToolbar.title = String.format(ResourceManager.getString(R.string.fileFragment_titleSelectMode), mSelectedPathFileInfoListMap.size, mFilePageInfo.getCurrentPageFileCount())
        }else{
            fileFragmentToolbar.title = mFilePageInfo.getCurrentDirectoryInfo().directory.fullName
        }
    }

    private fun FileInfo.isSelected():Boolean{
        return mSelectedPathFileInfoListMap.containsKey(this.path)
    }

    private fun setSelecting(){
        isSelecting = true
        notifyFloatingActionButtonUpdate()
        refreshToolbarTitle()
    }

    private fun setNotSelecting(){
        isSelecting = false
        mSelectedPathFileInfoListMap.clear()
        mAdapter.notifyDataSetChanged()
        notifyFloatingActionButtonUpdate()
        refreshToolbarTitle()
    }

    private fun setFileInfoToggleSelect(fileInfo: FileInfo){
        if (fileInfo.isSelected()){
            mSelectedPathFileInfoListMap.remove(fileInfo.path)
        }else{
            mSelectedPathFileInfoListMap[fileInfo.path] = fileInfo
        }
        // 如果全部都没选上，就退出
        if (mSelectedPathFileInfoListMap.size <= 0){
            setNotSelecting()
        }
        // 更新标题
        refreshToolbarTitle()
    }

    override fun getFloatingActionButtonIsVisible(): Boolean {
        return isSelecting
    }

    override fun getFloatingActionButtonIcon(): Int {
        return R.drawable.ic_more_dot_vertical
    }

    override fun getFloatingActionButtonClickListener(): View.OnClickListener? {
        return View.OnClickListener {
            mSelectMoreOperationDialog.show()
        }
    }

    /**********************************************************************************************/
    override fun onBackPressed(): Boolean {
        return when{
            isSelecting -> {
                setNotSelecting()
                true
            }
            mFilePageInfo.canPopDirectory() -> {
                mFilePageInfo.popDirectory()
                mAdapter.notifyDataSetChanged()
                refreshToolbarTitle()
                true
            }
            else -> {
                false
            }
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
            // 文件列表
            val count = mFilePageInfo.getCurrentPageFileCount()
            if (count <= 0){
                fileFragmentPageHintView.visible()
                fileFragmentRecyclerView.gone()
            }else{
                fileFragmentPageHintView.gone()
                fileFragmentRecyclerView.visible()
            }
            // 左上角导航图标
            if (mFilePageInfo.canPopDirectory()){
                fileFragmentToolbar.setNavigationIcon(R.drawable.ic_arrow_back_dark_blue)
            }else{
                fileFragmentToolbar.setNavigationIcon(R.drawable.ic_close)
            }
            return count
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val fileInfo = mFilePageInfo.getCurrentPageFile(position)
            val pos = holder.adapterPosition
            when{
                holder is DirectoryViewHolder -> {
                    holder.setTitle(fileInfo.fullName)
                    holder.setInfo("${fileInfo.size} 项  ${FormatManager.getFormatTime(fileInfo.modifyTime)}")
                    holder.setOnClickListener(View.OnClickListener {
                        if (isSelecting){
                            setFileInfoToggleSelect(fileInfo)
                            mAdapter.notifyItemChanged(pos)
                        }else{
                            mFilePageInfo.pushDirectory(fileInfo)
                            mAdapter.notifyDataSetChanged()
                            refreshToolbarTitle()
                        }
                    })
                    holder.setOnLongClickListener(View.OnLongClickListener {
                        setFileInfoToggleSelect(fileInfo)
                        setSelecting()
                        mAdapter.notifyItemChanged(pos)
                        true
                    })
                    if (fileInfo.isSelected()){
                        holder.setBackground(R.drawable.bg_file_select)
                        holder.setIcon(R.drawable.ic_check)
                    }else{
                        holder.setBackground(R.drawable.bg_file_directory)
                        holder.setIcon(R.drawable.ic_directory_dark_blue)
                    }
                }
                holder is FileViewHolder -> {
                    holder.setTitle(fileInfo.fullName)
                    holder.setInfo("${FormatManager.getFormatSize(fileInfo.size)}  ${FormatManager.getFormatTime(fileInfo.modifyTime)}")
                    holder.setOnClickListener(View.OnClickListener {
                        if (isSelecting){
                            setFileInfoToggleSelect(fileInfo)
                            mAdapter.notifyItemChanged(pos)
                        }else{

                        }
                    })
                    holder.setOnLongClickListener(View.OnLongClickListener {
                        setFileInfoToggleSelect(fileInfo)
                        setSelecting()
                        mAdapter.notifyItemChanged(pos)
                        true
                    })
                    if (fileInfo.isSelected()){
                        holder.setBackground(R.drawable.bg_file_select)
                        holder.setIcon(R.drawable.ic_check)
                    }else{
                        holder.setBackground(R.drawable.bg_file_file)
                        holder.setIcon(R.drawable.ic_file_grey)
                    }
                }
            }
        }

    }

    private inner class DirectoryViewHolder(parent:ViewGroup) : RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.item_file_directory, parent, false)){
        private val iconImageView = itemView.findViewById<ImageView>(R.id.itemFileDirectoryIcon)
        private val titleTextView = itemView.findViewById<TextView>(R.id.itemFileDirectoryTitle)
        private val infoTextView = itemView.findViewById<TextView>(R.id.itemFileDirectoryInfo)

        fun setIcon(resID:Int){
            iconImageView.setImageResource(resID)
        }

        fun setTitle(title:String){
            titleTextView.text = title
        }

        fun setInfo(info:String){
            infoTextView.text = info
        }

        fun setOnClickListener(onClickListener:View.OnClickListener){
            itemView.setOnClickListener(onClickListener)
        }

        fun setOnLongClickListener(onLongClickListener:View.OnLongClickListener){
            itemView.setOnLongClickListener(onLongClickListener)
        }

        fun setBackground(resID:Int){
            itemView.setBackgroundResource(resID)
        }
    }

    private inner class FileViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.item_file_file, parent, false)){
        private val iconImageView = itemView.findViewById<ImageView>(R.id.itemFileFileIcon)
        private val titleTextView = itemView.findViewById<TextView>(R.id.itemFileFileFileName)
        private val infoTextView = itemView.findViewById<TextView>(R.id.itemFileFileFileDescription)

        fun setIcon(resID:Int){
            iconImageView.setImageResource(resID)
        }

        fun setTitle(title:String){
            titleTextView.text = title
        }

        fun setInfo(info:String){
            infoTextView.text = info
        }

        fun setOnClickListener(onClickListener:View.OnClickListener){
            itemView.setOnClickListener(onClickListener)
        }

        fun setOnLongClickListener(onLongClickListener:View.OnLongClickListener){
            itemView.setOnLongClickListener(onLongClickListener)
        }

        fun setBackground(resID:Int){
            itemView.setBackgroundResource(resID)
        }

    }

}