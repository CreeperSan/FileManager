package com.creepersan.file.manager

object IDManager{
    private var id = 0L

    @Synchronized
    fun generateID():Long{
        id += 1
        return id
    }


}