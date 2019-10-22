package com.creepersan.file.manager

import android.content.Context
import com.creepersan.file.application.FileApplication

object ConfigManager {

    private const val PREF_GLOBAL = "global"
    private const val GLOBAL_SHOW_HIDDEN_FILE = "show_hidden_file"

    private val mApplication by lazy { FileApplication.getInstance() }
    private val mGlobalPref by lazy { mApplication.getSharedPreferences(PREF_GLOBAL, Context.MODE_PRIVATE) }

    fun getGlobalShowHiddenFile():Boolean{
        return mGlobalPref.getBoolean(GLOBAL_SHOW_HIDDEN_FILE, false)
    }

    fun setGlobalShowHiddenFile(value:Boolean){
        mGlobalPref.edit().putBoolean(GLOBAL_SHOW_HIDDEN_FILE, value).apply()
    }

}