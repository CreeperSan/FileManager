package com.creepersan.file.manager

import android.telephony.mbms.FileInfo
import java.util.ArrayList
import java.util.HashMap

object AsyncIOTaskManager{

    fun execute(){
        
    }

}

class BaseAsyncTask<T> {
    companion object{
        const val ACTION_PASTE = 0
    }
    private val id = IDManager.generateID()
    private val mData = HashMap<String, Any>()


}
