package com.example.pregnancytrackerignite.presentation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.databinding.LayoutDietDaysRvItemBinding
import com.example.pregnancytrackerignite.databinding.LayoutWeekRvItemBinding
import com.iobits.videocompressor.utils.gone
import com.iobits.videocompressor.utils.visible

class DietDayRvAdapter(
    val context: Context,
    private var list: List<String>?,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<DietDayRvAdapter.MenuListViewHolder>() {
     private var selected = 0

    class MenuListViewHolder(val binding: LayoutDietDaysRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {
        val binding = LayoutDietDaysRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuListViewHolder, @SuppressLint("RecyclerView") position: Int) {
        if(selected == position){
            holder.binding.apply {
                day.setTextColor(ContextCompat.getColor(context, R.color.primary))
                selector.setBackgroundResource(R.drawable.stroke_btn)
            }
        }else{
            holder.binding.apply {
                day.setTextColor(ContextCompat.getColor(context, R.color.dark_text))
                selector.setBackgroundResource(R.drawable.gray_stroke_btn)
            }
        }

        val model = list?.get(position)
        holder.binding.apply {
            day.text = model.toString()
        }
        holder.itemView.setOnClickListener {
            if (model != null) {
                onClick.invoke(position)
                selected = position
                notifyDataSetChanged()
            }
        }

    }


    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    fun updateSelectedItem(){

    }

}