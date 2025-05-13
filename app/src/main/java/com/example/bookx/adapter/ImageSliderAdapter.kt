package com.example.bookx

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ImageSliderAdapter(
    private val images: List<SliderItem>,
    private val itemWidth: Int,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<ImageSliderAdapter.SliderViewHolder>() {

    class SliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.slideImageView)
        val nameText: TextView = view.findViewById(R.id.slideNameText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.manual_slider_layout, parent, false)

        // Set the width to show 3 items at once
        view.layoutParams.width = itemWidth

        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val item = images[position]

        // Set image resource
        holder.imageView.setImageResource(item.imageResId)

        // Set name text
        holder.nameText.text = item.name

        // Set click listener
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount() = images.size
}