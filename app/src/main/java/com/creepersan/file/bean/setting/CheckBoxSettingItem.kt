package com.creepersan.file.bean.setting

import androidx.annotation.DrawableRes

class CheckBoxSettingItem(
    id: Int,
    name: String,
    var value: Boolean,
    @DrawableRes var icon: Int = 0,
    var description: String = "",
    var onSelectChange: ((oldValue: Boolean, newValue: Boolean, position:Int) -> Unit)? = null
) : BaseSettingItem(id, name)