package com.hanna.balancr.ui.pictures

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hanna.balancr.model.entities.BodyPicture


class BodyPicturesAdapter :
    ListAdapter<BodyPicture, BodyPictureViewHolder>(object : DiffUtil.ItemCallback<BodyPicture>() {


        override fun onBindViewHolder(holder: BodyPictureViewHolder, position: Int) {
            TODO("Not yet implemented")
        }
    }

        class BodyPictureViewHolder(view: View) : RecyclerView.ViewHolder(view)