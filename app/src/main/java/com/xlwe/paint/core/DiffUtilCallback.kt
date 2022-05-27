package com.xlwe.paint.core

import androidx.recyclerview.widget.DiffUtil
import com.xlwe.paint.model.Picture

class DiffUtilCallback : DiffUtil.ItemCallback<Picture>() {
    override fun areItemsTheSame(oldItem: Picture, newItem: Picture): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Picture, newItem: Picture): Boolean {
        return oldItem == newItem
    }
}