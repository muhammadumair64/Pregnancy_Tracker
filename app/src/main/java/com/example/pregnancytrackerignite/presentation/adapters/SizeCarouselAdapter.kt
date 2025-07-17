package com.example.pregnancytrackerignite.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pregnancytrackerignite.databinding.ItemSizeCrouselBinding

class SizeCarouselAdapter(
    private val arrayList: ArrayList<Int>
) :
    RecyclerView.Adapter<SizeCarouselAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: ItemSizeCrouselBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemSizeCrouselBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = arrayList[position]
        val binding = holder.binding
        binding.mainImage.setImageResource(item)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}