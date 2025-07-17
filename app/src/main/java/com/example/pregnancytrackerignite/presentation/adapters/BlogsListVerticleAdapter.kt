package com.example.pregnancytrackerignite.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pregnancytrackerignite.data.models.MainBlogModel
import com.example.pregnancytrackerignite.databinding.ItemBlogsVerticleRvBinding

class BlogsListVerticleAdapter(
    private val context: Context,
    private val item: ArrayList<MainBlogModel>,
    private val onItemClicked: (MainBlogModel) -> Unit
) : RecyclerView.Adapter<BlogsListVerticleAdapter.BlogsViewHolder>() {
    private var selectedPosition = -1

    class BlogsViewHolder(val binding: ItemBlogsVerticleRvBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogsViewHolder {
        val binding = ItemBlogsVerticleRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BlogsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BlogsViewHolder, position: Int) {
        val model = item[position]
        holder.binding.apply {

            titleTxt.text = model.Title
            descriptionTxt.text = model.para
            Glide.with(context).load(model.image).into(mainImg)
            root.setOnClickListener {
                model?.let { blogs ->
                    onItemClicked(blogs)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

}