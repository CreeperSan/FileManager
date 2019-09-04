package com.creepersan.file.dialog

import android.content.Context
import com.creepersan.file.R

class FileFragmentSearchDialog(context:Context) : BaseDialog(
    context,
    position = POSITION_TOP,
    animation = ANIMATION_TOP
) {

    override fun getLayoutID(): Int = R.layout.dialog_file_fragment_search

}