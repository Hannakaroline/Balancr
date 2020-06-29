package com.hanna.balancr.ui.pictures

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hanna.balancr.R
import com.hanna.balancr.model.entities.BodyPicture
import java.text.SimpleDateFormat

class BodyPicturesAdapter :
    ListAdapter<BodyPicture, BodyPictureViewHolder>(object : DiffUtil.ItemCallback<BodyPicture>() {
        override fun areItemsTheSame(oldItem: BodyPicture, newItem: BodyPicture): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: BodyPicture, newItem: BodyPicture): Boolean {
            return oldItem.date == newItem.date && oldItem.picturePath == newItem.picturePath
        }

    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BodyPictureViewHolder {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.body_picture_page_item, parent, false)
            .let(::BodyPictureViewHolder)
    }

    override fun onBindViewHolder(holder: BodyPictureViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class BodyPictureViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
    val date = itemView.findViewById<TextView>(R.id.date)
    val imageView = itemView.findViewById<ImageView>(R.id.image)

    fun bind(bodyPicture: BodyPicture) {
        date.text = dateFormatter.format(bodyPicture.date)
        Glide.with(itemView.context)
            .load(bodyPicture.picturePath)
            .into(imageView)
    }
}