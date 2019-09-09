package com.creepersan.file.view.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.creepersan.file.R
import com.creepersan.file.extension.gone
import com.creepersan.file.extension.visible

class PageHintView : LinearLayout{
    private val mRootView : View
    private val mImageView : ImageView
    private val mHintTextView : TextView
    private val mButtonTextView : TextView

    private var mImageRes : Int = 0
    private var mHintTextString : String = ""
    private var mButtonTextString : String = ""
    private var mButtonClickAction : OnClickListener? = null

    constructor(context:Context):this(context, null)
    constructor(context:Context, attr:AttributeSet?):this(context, attr, 0)
    constructor(context:Context, attr:AttributeSet?, defStyle:Int):super(context, attr, defStyle){
        // findView
        mRootView = View.inflate(context, R.layout.view_page_hint_view, this)
        mImageView = mRootView.findViewById(R.id.viewPageHintViewImage)
        mHintTextView = mRootView.findViewById(R.id.viewPageHintViewHintText)
        mButtonTextView = mRootView.findViewById(R.id.viewPageHintViewButton)
        // 初始化配置
        attr?.apply {
            val typedView = context.obtainStyledAttributes(this, R.styleable.PageHintView)
            mImageRes = typedView.getResourceId(R.styleable.PageHintView_image, 0)
            mHintTextString = typedView.getString(R.styleable.PageHintView_hintText) ?: mHintTextString
            mButtonTextString = typedView.getString(R.styleable.PageHintView_buttonText) ?: mButtonTextString
            typedView.recycle()
        }
        // 设置View
        mImageView.setImageResource(mImageRes)
        mHintTextView.text = mHintTextString
        mButtonTextView.text = mButtonTextString
        if (mButtonTextString == ""){
            mButtonTextView.gone()
        }else{
            mButtonTextView.visible()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    /**
     * 设置点击时间
     */
    fun setClickAction(clickListener:OnClickListener){
        mButtonClickAction = clickListener
    }

    /**
     * 设置图片
     */
    fun setImageRes(imgRes:Int){
        mImageRes = imgRes
        mImageView.setImageResource(imgRes)
        invalidate()
    }

    /**
     * 设置提示文本
     */
    fun setHintText(hintString:String){
        mHintTextString = hintString
        mHintTextView.text = hintString
        invalidate()
    }

    /***
     * 设置按钮文本
     */
    fun setButtonText(buttonString:String){
        mButtonTextString = buttonString
        mButtonTextView.text = buttonString
        invalidate()
    }

}