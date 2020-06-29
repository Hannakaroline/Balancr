package com.hanna.balancr.ui.weight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hanna.balancr.R
import com.hanna.balancr.model.entities.Weighting
import java.text.SimpleDateFormat

class WeightingsAdapter :
    ListAdapter<Weighting, WeightingViewHolder>(object : DiffUtil.ItemCallback<Weighting>() {
        override fun areItemsTheSame(oldItem: Weighting, newItem: Weighting): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Weighting, newItem: Weighting): Boolean {
            return oldItem.weight == newItem.weight && oldItem.date == newItem.date
        }

    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightingViewHolder {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.weighting_list_item, parent, false)
            .let(::WeightingViewHolder)
    }

    override fun onBindViewHolder(holder: WeightingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class WeightingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy")

    val weightText = itemView.findViewById<TextView>(R.id.weight_value)
    val dateText = itemView.findViewById<TextView>(R.id.weight_date)

    fun bind(weighting: Weighting) {
        weightText.text = String.format("%.2f Kg", weighting.weight)
        dateText.text = dateFormatter.format(weighting.date)
    }
}