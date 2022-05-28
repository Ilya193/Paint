package com.xlwe.paint.core

import com.nvt.color.ColorPickerDialog

abstract class OnColorPickerListener : ColorPickerDialog.OnColorPickerListener {
    override fun onCancel(dialog: ColorPickerDialog?) {}

    override fun onOk(dialog: ColorPickerDialog?, color: Int) {}
}