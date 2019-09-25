package com.creepersan.file.manager

import android.util.Log
import com.creepersan.file.bean.file.FileInfo
import com.creepersan.file.global.GlobalFileInfoClipBoard
import java.io.File
import java.util.HashSet
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object AsyncIOTaskManager{
    const val TAG = "AsyncIOTaskManager"

    private val mSingleThreadPool = ThreadPoolExecutor(0, 1, 1L, TimeUnit.MINUTES, LinkedBlockingQueue<Runnable>())

    fun execute(task:BaseAsyncIOTask){
        mSingleThreadPool.execute(when(task){
            is CopyMoveAsyncIOTask -> {
                IOCopyMoveHandelRunnable(task)
            }
            is CreateAsyncIOTask -> {
                IOCreateHandelRunnable(task)
            }
            is DeleteAsyncIOTask -> {
                IODeleteHandelRunnable(task)
            }
            is RenameAsyncIOTask -> {
                IORenameHandelRunnable(task)
            }
            else -> {
                EmptyIOHandel(task)
            }
        })
    }

}

abstract class BaseAsyncIOTask{
    companion object{
        const val TYPE_IDLE = 0
        const val TYPE_COPY_MOVE = 1
        const val TYPE_CREATE = 3
        const val TYPE_DELETE = 4
        const val TYPE_RENAME = 5
    }

    abstract val type : Int
}

/**************************************  TASK  ****************************************************/

class CopyMoveAsyncIOTask(val actionFileList:List<CopyMoveFileInfo>) : BaseAsyncIOTask(){
    override val type: Int = TYPE_COPY_MOVE

    data class CopyMoveFileInfo(val fileInfo:FileInfo, val action:GlobalFileInfoClipBoard.Action, val targetFileInfo: FileInfo)
}

class CreateAsyncIOTask(val targetFileInfo:List<FileInfo>) : BaseAsyncIOTask(){
    override val type: Int = TYPE_CREATE

}

class DeleteAsyncIOTask(val actionFileList:List<FileInfo>) : BaseAsyncIOTask(){
    override val type: Int = TYPE_DELETE
}

class RenameAsyncIOTask(val actionFileList: List<FileInfo>, extensions:String) : BaseAsyncIOTask(){
    override val type: Int = TYPE_RENAME
}

/***********************************  Handel  *****************************************************/

private class EmptyIOHandel(val task:BaseAsyncIOTask) : Runnable{
    override fun run() {
        Log.e(AsyncIOTaskManager.TAG, "提交了一个空任务，不做处理")
    }
}

private class IOCopyMoveHandelRunnable(val task:CopyMoveAsyncIOTask) : Runnable{
    override fun run() {
        val tmpSuccessParentPathSet = HashSet<String>()
        // 遍历删除
        task.actionFileList.forEach { copyMoveFileInfo ->
            val scrFile = File(copyMoveFileInfo.fileInfo.path)
            val targetFile = File(copyMoveFileInfo.targetFileInfo.path)
            val action = copyMoveFileInfo.action
            when(action){
                GlobalFileInfoClipBoard.Action.MOVE -> { // 移动

                }
                GlobalFileInfoClipBoard.Action.COPY -> { // 复制

                }
            }
        }
        // 通知
        tmpSuccessParentPathSet.forEach {  parentPath ->
            BroadcastManager.notifyPathChange(parentPath)
        }
    }
}

private class IODeleteHandelRunnable(val task:DeleteAsyncIOTask) : Runnable{
    override fun run() {
        val tmpSuccessParentPathSet = HashSet<String>()
        // 遍历删除
        task.actionFileList.forEach {  fileInfo ->
            val file = File(fileInfo.path)
            if (file.exists()){
                if (file.delete()){
                    tmpSuccessParentPathSet.add(file.parent ?: "")
                }
            }
        }
        // 通知
        tmpSuccessParentPathSet.forEach {  parentPath ->
            BroadcastManager.notifyPathChange(parentPath)
        }
    }
}

private class IOCreateHandelRunnable(val task:CreateAsyncIOTask) : Runnable{
    override fun run() {
        val tmpSuccessParentPathSet = HashSet<String>()
        // 遍历
        task.targetFileInfo.forEach {  fileInfo ->
            val tmpFile = File(fileInfo.path)
            // 防止文件存在
            if (tmpFile.exists()){
                return@forEach
            }
            // 执行操作
            if (fileInfo.isDirectory){ // 文件夹
                if (tmpFile.mkdirs()){
                    tmpSuccessParentPathSet.add(tmpFile.parent ?: "")
                }
            }else{ // 文件
                if (tmpFile.createNewFile()){
                    tmpSuccessParentPathSet.add(tmpFile.parent ?: "")
                }
            }
        }
        // 通知
        tmpSuccessParentPathSet.forEach {  parentPath ->
            BroadcastManager.notifyPathChange(parentPath)
        }
    }
}

private class IORenameHandelRunnable(val task:RenameAsyncIOTask) : Runnable{
    override fun run() {

    }
}


