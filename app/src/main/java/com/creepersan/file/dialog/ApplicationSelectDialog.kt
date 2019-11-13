package com.creepersan.file.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.creepersan.file.R
import com.creepersan.file.common.view_holder.IncludeSimpleDialogActionViewHolder
import com.creepersan.file.view.common.PageHintView

class ApplicationSelectDialog(
    context: Context
) : BaseDialog(context, POSITION_CENTER, ANIMATION_CENTER) {
    private val mPageHintView = dialogView.findViewById<PageHintView>(R.id.dialogApplicationPageHint)
    private val mRecyclerView = dialogView.findViewById<RecyclerView>(R.id.dialogApplicationRecyclerView)
    private val mIncludeActionButton = IncludeSimpleDialogActionViewHolder(dialogView.findViewById(R.id.dialogApplicationActionButtonLayout))

    override fun getLayoutID(): Int {
        return R.layout.dialog_application_select
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActionButton()
    }

    private fun initActionButton(){
        mIncludeActionButton.setPositiveButton("确定", View.OnClickListener {

        })
    }



}