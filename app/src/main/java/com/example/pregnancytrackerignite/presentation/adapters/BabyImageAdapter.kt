package com.example.pregnancytrackerignite.presentation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pregnancytrackerignite.data.models.BabyDataClass
import com.example.pregnancytrackerignite.databinding.ItemBabyBinding


class BabyImageAdapter(
    private val context: Context
) : RecyclerView.Adapter<BabyImageAdapter.MyHolder>() {

   var onClick: ((Int) -> Unit)? = null
    class MyHolder(val binding: ItemBabyBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(ItemBabyBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.cubeTv.text = differ.currentList[position].name

        Glide.with(context).load(differ.currentList[position].img).into(holder.binding.babyImage)

        holder.binding.root.setOnClickListener {
          onClick?.invoke(position)
        }
    }

    private val differCallback = object: DiffUtil.ItemCallback<BabyDataClass>(){
        override fun areItemsTheSame(oldItem: BabyDataClass, newItem: BabyDataClass): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: BabyDataClass, newItem: BabyDataClass): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    fun moveItem(fromPosition: Int, toPosition: Int) {
        val list = differ.currentList.toMutableList()
        val fromItem = list[fromPosition]
        list.removeAt(fromPosition)
        if (toPosition < fromPosition) {
            list.add(toPosition + 1 , fromItem)
        } else {
            list.add(toPosition - 1, fromItem)
        }
        differ.submitList(list)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
