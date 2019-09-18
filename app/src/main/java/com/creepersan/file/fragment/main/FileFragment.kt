package com.creepersan.file.fragment.main

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.file.R
import com.creepersan.file.activity.CreateFileDirectoryActivity
import com.creepersan.file.activity.FragmentPageObserver
import com.creepersan.file.activity.MainActivity
import com.creepersan.file.bean.file.FileInfo
import com.creepersan.file.bean.file.FilePageInfo
import com.creepersan.file.dialog.*
import com.creepersan.file.extension.gone
import com.creepersan.file.extension.visible
import com.creepersan.file.global.GlobalFileInfoClipBoard
import com.creepersan.file.manager.FormatManager
import com.creepersan.file.manager.ResourceManager
import com.creepersan.file.manager.ToastManager
import kotlinx.android.synthetic.main.fragment_main_file.*
import java.io.File
import java.lang.RuntimeException
import java.util.*

class FileFragment(activityNotify: MainActivity.Controller, fragmentListObserver: FragmentPageObserver) : BaseMainFragment(activityNotify, fragmentListObserver), View.OnClickListener, View.OnLongClickListener, GlobalFileInfoClipBoard.GlobalClipBoardObserver{

    companion object{
        private const val TYPE_FILE = 0
        private const val TYPE_DIRECTORY = 1

        private const val DIALOG_SELECTION_INFO = 0
        private const val DIALOG_SELECTION_COPY = 1
        private const val DIALOG_SELECTION_COPY_APPEND = 2
        private const val DIALOG_SELECTION_PASTE = 3
        private const val DIALOG_SELECTION_CUT = 4
        private const val DIALOG_SELECTION_CUT_APPEND = 5
        private const val DIALOG_SELECTION_DELETE = 6
        private const val DIALOG_SELECTION_RENAME = 7
    }

    private var isSelecting = false

    private val mSelectedPathFileInfoListMap = LinkedHashMap<String, FileInfo>()
    private val mFilePageInfo = FilePageInfo()
    private val mAdapter = FileAdapter()

    private lateinit var mSelectMoreOperationDialog : BaseBottomSelectionDialog
    private lateinit var mSearchDialog : FileFragmentSearchDialog
    private lateinit var mSimpleAlertDialog : SimpleAlertDialog
    private lateinit var mFileDetailDialog : FileDetailDialog
    private lateinit var mFileRenameSingleDialog : FileRenameSingleDialog
    private lateinit var mFileRenameMultiDialog : FileRenameMultiDialog

    override fun getLayoutID(): Int {
        return R.layout.fragment_main_file
    }

    override fun getName(): String {
        val directoryName = mFilePageInfo.getCurrentDirectoryInfo().directory.fullName
        if (directoryName.isEmpty()){
            return R.string.fileFragment_title.toResString()
        }
        return directoryName
    }

    override fun getIcon(): Int {
        return R.drawable.ic_directory_dark_blue
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClipBoardEvent()
        initDialog()
        initToolbar()
        initRecyclerView()
    }

    private fun initClipBoardEvent(){
        GlobalFileInfoClipBoard.subscribe(this)
    }

    private fun initDialog(){
        // 底部弹出的文件操作对话框
        mSelectMoreOperationDialog = BaseBottomSelectionDialog(activity())
            .setItemList(arrayListOf(
                BaseBottomSelectionDialogItem(DIALOG_SELECTION_COPY, R.drawable.ic_content_copy, ResourceManager.getString(R.string.fileFragment_dialogOperationCopy)),
                BaseBottomSelectionDialogItem(DIALOG_SELECTION_COPY_APPEND, R.drawable.ic_content_copy, ResourceManager.getString(R.string.fileFragment_dialogOperationCopyAppend)),
                BaseBottomSelectionDialogItem(DIALOG_SELECTION_CUT, R.drawable.ic_content_cut, ResourceManager.getString(R.string.fileFragment_dialogOperationCut)),
                BaseBottomSelectionDialogItem(DIALOG_SELECTION_CUT_APPEND, R.drawable.ic_content_cut, ResourceManager.getString(R.string.fileFragment_dialogOperationCutAppend)),
                BaseBottomSelectionDialogItem(DIALOG_SELECTION_PASTE, R.drawable.ic_content_paste, ResourceManager.getString(R.string.fileFragment_dialogOperationPaste)),
                BaseBottomSelectionDialogItem(DIALOG_SELECTION_DELETE, R.drawable.ic_delete, ResourceManager.getString(R.string.fileFragment_dialogOperationDelete)),
                BaseBottomSelectionDialogItem(DIALOG_SELECTION_RENAME, R.drawable.ic_rename, ResourceManager.getString(R.string.fileFragment_dialogOperationRename)),
                BaseBottomSelectionDialogItem(DIALOG_SELECTION_INFO, R.drawable.ic_info, ResourceManager.getString(R.string.fileFragment_dialogOperationDetail))
            ))
            .setItemClickListener(object : BaseBottomSelectItemClickListener{
                override fun onItemClick(id: Int, item: BaseBottomSelectionDialogItem, dialog: BaseDialog) {
                    mSelectMoreOperationDialog.cancel()
                    when(id){
                        DIALOG_SELECTION_COPY -> { //////////////////////////////////////////////////
                            GlobalFileInfoClipBoard.setFileInfo(GlobalFileInfoClipBoard.ACTION_COPY, mSelectedPathFileInfoListMap.values.toList())
                            setNotSelecting()
                        }
                        DIALOG_SELECTION_COPY_APPEND -> { //////////////////////////////////////////////////
                            GlobalFileInfoClipBoard.addFileInfo(GlobalFileInfoClipBoard.ACTION_COPY, mSelectedPathFileInfoListMap.values.toList())
                            setNotSelecting()
                        }
                        DIALOG_SELECTION_CUT -> { //////////////////////////////////////////////////
                            GlobalFileInfoClipBoard.setFileInfo(GlobalFileInfoClipBoard.ACTION_CUT, mSelectedPathFileInfoListMap.values.toList())
                            setNotSelecting()
                        }
                        DIALOG_SELECTION_CUT_APPEND -> { //////////////////////////////////////////////////
                            GlobalFileInfoClipBoard.addFileInfo(GlobalFileInfoClipBoard.ACTION_CUT, mSelectedPathFileInfoListMap.values.toList())
                            setNotSelecting()
                        }
                        DIALOG_SELECTION_PASTE -> { //////////////////////////////////////////////////

                        }
                        DIALOG_SELECTION_DELETE -> { //////////////////////////////////////////////////
                            when{
                                mSelectedPathFileInfoListMap.isEmpty() -> {
                                    ToastManager.show(R.string.fileFragment_dialogSimpleAlertDeleteToastNoSelectFile)
                                    return
                                }
                                mSelectedPathFileInfoListMap.size == 1 -> {
                                    val fileInfo = mSelectedPathFileInfoListMap.values.toList()[0]
                                    mSimpleAlertDialog.setMessage(String.format(ResourceManager.getString(R.string.fileFragment_dialogSimpleAlertDeleteHint), fileInfo.fullName))
                                }
                                mSelectedPathFileInfoListMap.size > 1 -> {
                                    val fileCount = mSelectedPathFileInfoListMap.size
                                    mSimpleAlertDialog.setMessage(
                                        String.format(ResourceManager.getString(R.string.fileFragment_dialogSimpleAlertDeleteHint),
                                            String.format(ResourceManager.getString(R.string.fileFragment_dialogSimpleAlertDeleteHintMultiUnit), fileCount)
                                        )
                                    )
                                }
                            }
                            mSimpleAlertDialog.setDialogTitle(R.string.fileFragment_dialogSimpleAlertDeleteTitle)
                            mSimpleAlertDialog.setPositiveAction(R.string.common_dialogPositive, View.OnClickListener {
                                ToastManager.show("删除中...")
                                setNotSelecting()
                                mSimpleAlertDialog.cancel()
                            })
                            mSimpleAlertDialog.setNegativeAction(R.string.common_dialogNegative, View.OnClickListener {
                                setNotSelecting()
                                mSimpleAlertDialog.cancel()
                            })
                            mSimpleAlertDialog.show()
                        }
                        DIALOG_SELECTION_RENAME -> { //////////////////////////////////////////////////
                            when{
                                mSelectedPathFileInfoListMap.isEmpty() -> {
                                    ToastManager.show(R.string.fileFragment_dialogRenameSingleNotSelectedFileHint)
                                }
                                mSelectedPathFileInfoListMap.size == 1 -> {
                                    val fileInfo = mSelectedPathFileInfoListMap.values.toList()[0]
                                    mFileRenameSingleDialog.setFileInfo(fileInfo, object : FileRenameSingleDialog.OnResultCallback{
                                        override fun onResult() {
                                            mFileRenameSingleDialog.cancel()
                                        }

                                        override fun onFail(fileInfo: FileInfo) {
                                            ToastManager.show(R.string.fileFragment_dialogRenameSingleFailHint)
                                        }

                                        override fun onSuccess(fileInfo: FileInfo, newFileInfo: FileInfo) {
                                            ToastManager.show(R.string.fileFragment_dialogRenameSingleSuccessHint)
                                            mFilePageInfo.refreshTop()
                                            mAdapter.notifyDataSetChanged()
                                        }

                                    }).show()
                                }
                                mSelectedPathFileInfoListMap.size > 1 -> {
                                    mFileRenameMultiDialog
                                        .setFileInfoList(mSelectedPathFileInfoListMap.values.toList())
                                        .setResultCallback(object : FileRenameMultiDialog.OnResultCallback{
                                            override fun onResult() {
                                                mFileRenameMultiDialog.closeDialog()
                                            }

                                            override fun onSuccess() {
                                                ToastManager.show(R.string.fileFragment_dialogRenameMultiSuccessHint)
                                                mFilePageInfo.refreshTop()
                                                mAdapter.notifyDataSetChanged()
                                            }

                                            override fun onPartlySuccess() {
                                                ToastManager.show(R.string.fileFragment_dialogRenameMultiPartlySuccessHint)
                                                mFilePageInfo.refreshTop()
                                                mAdapter.notifyDataSetChanged()
                                            }

                                            override fun onFail() {
                                                ToastManager.show(R.string.fileFragment_dialogRenameMultiFailHint)
                                            }

                                        })
                                        .showDialog()
                                }
                            }
                        }
                        DIALOG_SELECTION_INFO -> { //////////////////////////////////////////////////
                            mFileDetailDialog
                                .setFileInfo(mSelectedPathFileInfoListMap.values.toList())
                                .show()
                        }
                    }
                }
            })
        // 上面弹出的搜索对话框
        mSearchDialog = FileFragmentSearchDialog(activity())
        // 中间的弹窗对话框
        mSimpleAlertDialog = SimpleAlertDialog(activity())
        // 文件详情对话框
        mFileDetailDialog = FileDetailDialog(activity())
        // 单个文件重命名对话框
        mFileRenameSingleDialog = FileRenameSingleDialog(activity())
        mFileRenameSingleDialog.setCancelListener(DialogInterface.OnCancelListener {
            setNotSelecting()
        })
        // 多个文件重命名对话框
        mFileRenameMultiDialog = FileRenameMultiDialog(activity())
        mFileRenameMultiDialog.setOnCancelListener(DialogInterface.OnCancelListener {
            setNotSelecting()
        })
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
                    activity().toActivity(CreateFileDirectoryActivity::class.java)
                }
                R.id.menuFileFragmentToolbarSearch -> {
                    mSearchDialog.show()
                }
                R.id.menuFileFragmentToolbarRefresh -> {
                    mFilePageInfo.refreshTop()
                    mAdapter.notifyDataSetChanged()
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
        activityNotify.notifyFloatingActionButtonChange()
        refreshToolbarTitle()
    }

    private fun setNotSelecting(){
        isSelecting = false
        mSelectedPathFileInfoListMap.clear()
        mAdapter.notifyDataSetChanged()
        activityNotify.notifyFloatingActionButtonChange()
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

    /********************************* GlobalClipBoardEvent ***************************************/

    override fun onClipboardChange() {
        activityNotify.notifyFloatingActionButtonChange()
    }

    /********************************* FloatingActionButton ***************************************/

    override fun getFloatingActionButtonIsVisible(): Boolean {
        return isSelecting || GlobalFileInfoClipBoard.isNotEmpty()
    }

    override fun getFloatingActionButtonIcon(): Int {
        if (isSelecting){
            return R.drawable.ic_content_copy
        }else if (GlobalFileInfoClipBoard.isNotEmpty()){
            return R.drawable.ic_content_paste
        }else {
            return R.drawable.ic_more_dot_vertical
        }
    }

    override fun getFloatingActionButtonClickListener(): View.OnClickListener? {
        return this
    }

    override fun getFloatingActionButtonLongClickListener(): View.OnLongClickListener? {
        return this
    }

    override fun onClick(p0: View?) {
        when{
            isSelecting -> { // 浮动按钮是选择
                GlobalFileInfoClipBoard.setFileInfo(GlobalFileInfoClipBoard.ACTION_COPY, mSelectedPathFileInfoListMap.values.toList())
                setNotSelecting()
            }
            GlobalFileInfoClipBoard.isNotEmpty() -> { // 浮动按钮是粘贴
                val copiedFileInfoList = GlobalFileInfoClipBoard.getCopiedFileInfoList()
                var failCount = 0
                val allCount = copiedFileInfoList.size
                    copiedFileInfoList.forEach { copiedFileInfo ->
                    val sourceFileInfo = copiedFileInfo.fileInfo
                    val targetFileInfo = FileInfo("${mFilePageInfo.getCurrentDirectoryInfo().directory.path}/${sourceFileInfo.fullName}")
                    val sourceFile = File(sourceFileInfo.path)
                    val targetFile = File(targetFileInfo.path)
                    if (sourceFileInfo.path == targetFileInfo.path){ // 同一个文件，直接跳过
                        failCount += 1
                        return
                    }
                    if (targetFileInfo.isExist){ // 文件已经存在，暂时是直接跳过
                        failCount += 1
                        return
                    }
                    when(copiedFileInfo.action){
                        GlobalFileInfoClipBoard.ACTION_CUT -> {
                            if (!sourceFile.renameTo(targetFile)){
                                failCount += 1
                            }
                        }
                        GlobalFileInfoClipBoard.ACTION_COPY -> {
                            try {
                                sourceFile.copyTo(targetFile, false)
                            }catch (e:FileSystemException){
                                failCount += 1
                            }
                        }
                    }
                }
                GlobalFileInfoClipBoard.clearFileInfo()
                when{
                    failCount == 0 -> {
                        ToastManager.show("操作成功")
                    }
                    failCount in 1 until allCount -> {
                        ToastManager.show("操作完成，部分操作失败")
                    }
                    failCount >= failCount -> {
                        ToastManager.show("操作失败")
                    }
                    else -> {
                        ToastManager.show("操作完成")
                    }
                }
            }
        }
    }

    override fun onLongClick(p0: View?): Boolean {
        mSelectMoreOperationDialog.show()
        return true
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
                refreshContentPager()
                fragmentListObserver.notifyFragmentUpdate(fragmentListObserver.getIndex(this@FileFragment))
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

        override fun getItemCount(): Int = mFilePageInfo.getCurrentPageFileCount()

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
                            refreshContentPager()
                            fragmentListObserver.notifyFragmentUpdate(fragmentListObserver.getIndex(this@FileFragment))
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
                        holder.setInfoTextColor(ResourceManager.getColor(R.color.textHintColorLight))
                    }else{
                        holder.setBackground(R.drawable.bg_file_directory)
                        holder.setIcon(R.drawable.ic_directory_dark_blue)
                        holder.setInfoTextColor(ResourceManager.getColor(R.color.textHintColorDark))
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
                        holder.setInfoTextColor(ResourceManager.getColor(R.color.textHintColorLight))
                    }else{
                        holder.setBackground(R.drawable.bg_file_file)
                        holder.setIcon(R.drawable.ic_file_grey)
                        holder.setInfoTextColor(ResourceManager.getColor(R.color.textHintColorDark))
                    }
                }
            }
        }
    }

    private fun refreshContentPager(){
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
    }

    private inner class DirectoryViewHolder(parent:ViewGroup) : RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.item_file_directory, parent, false)){
        private val iconImageView = itemView.findViewById<ImageView>(R.id.itemFileDirectoryIcon)
        private val titleTextView = itemView.findViewById<TextView>(R.id.itemFileDirectoryTitle)
        private val infoTextView = itemView.findViewById<TextView>(R.id.itemFileDirectoryInfo)

        fun setIcon(resID:Int){
            iconImageView.setImageResource(resID)
        }

        fun setInfoTextColor(color:Int){
            infoTextView.setTextColor(color)
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

        fun setInfoTextColor(@ColorInt color:Int){
            infoTextView.setTextColor(color)
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