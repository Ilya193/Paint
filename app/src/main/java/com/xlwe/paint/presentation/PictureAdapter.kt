package com.xlwe.paint.presentation

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xlwe.paint.core.DiffUtilCallback
import com.xlwe.paint.core.OnClickListener
import com.xlwe.paint.core.OnLongClickListener
import com.xlwe.paint.databinding.PictureItemBinding
import com.xlwe.paint.model.Picture

class PictureAdapter(
    private val onClickListener: OnClickListener,
    private val onLongClickListener: OnLongClickListener
) : ListAdapter<Picture, PictureAdapter.PictureViewHolder>(DiffUtilCallback()) {

    inner class PictureViewHolder(private val binding: PictureItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(absolutePath: String, name: String) {
            binding.picture.setImageBitmap(BitmapFactory.decodeFile(absolutePath))
            binding.name.text = name

            binding.root.setOnLongClickListener {
                onLongClickListener.onLongClick(absolutePath)
                true
            }

            binding.root.setOnClickListener {
                onClickListener.onClick(absolutePath, name)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        return PictureViewHolder(
            PictureItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val picture = getItem(position)
        holder.bind(picture.absolutePath, picture.name)
    }
}