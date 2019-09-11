package com.creepersan.file.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import com.creepersan.file.R

abstract class BaseDialog(context: Context,
                        protected var position: Int = POSITION_CENTER,
                        protected var animation: Int = ANIMATION_NONE
) : Dialog(context) {

    companion object{
        const val POSITION_CENTER = 0
        const val POSITION_BOTTOM = 1
        const val POSITION_TOP = 2

        const val ANIMATION_NONE = 0
        const val ANIMATION_TOP = 1
        const val ANIMATION_CENTER = 2
        const val ANIMATION_BOTTOM = 3
    }

    protected lateinit var dialogView : View

    abstract fun getLayoutID():Int

    init {
        initLayout()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置View
        setContentView(dialogView)
        // 初始化
        initPosition()
        initBackgroundColor()
        initLayoutParams()
        initWindowAnimation()
    }

    private fun initLayout(){
        dialogView = layoutInflater.inflate(getLayoutID(), null)
    }

    private fun initPosition(){
        window?.apply {
            when(position){
                POSITION_CENTER -> {
                    this.setGravity(Gravity.CENTER)
                }
                POSITION_BOTTOM -> {
                    this.setGravity(Gravity.CENTER or Gravity.BOTTOM)
                }
                POSITION_TOP -> {
                    this.setGravity(Gravity.CENTER or Gravity.TOP)
                }
            }
        }
    }

    private fun initBackgroundColor(){
        window?.setBackgroundDrawableResource(android.R.color.transparent);
    }

    private fun initLayoutParams(){
        window?.apply {
            val layoutParams = attributes
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            attributes = layoutParams
        }
    }

    private fun initWindowAnimation(){
        window?.apply {
            when(animation){
                ANIMATION_TOP -> {
                    this.setWindowAnimations(R.style.windowAnimationTop)
                }
                ANIMATION_CENTER -> {
                    this.setWindowAnimations(R.style.windowAnimationCenter)
                }
                ANIMATION_BOTTOM -> {
                    this.setWindowAnimations(R.style.windowAnimationBottom)
                }
            }
        }
    }

    fun setDismissListener(listener:DialogInterface.OnDismissListener?):BaseDialog{
        setOnDismissListener(listener)
        return this
    }

    fun setCancelListener(listener:DialogInterface.OnCancelListener?):BaseDialog{
        setOnCancelListener(listener)
        return this
    }

    override fun hide() {
        closeDialog()
    }

    fun closeDialog(){
        cancel()
    }

    fun showDialog(){
        show()
    }

}