package com.example.pregnancytrackerignite.presentation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.databinding.LayoutWeekRvItemBinding
import com.iobits.videocompressor.utils.gone
import com.iobits.videocompressor.utils.visible

class WeekRvAdapter(
    private val context: Context,
    private val list: List<String>?,
    var selected: Int = 0
) : RecyclerView.Adapter<WeekRvAdapter.MenuListViewHolder>() {

    var onSelected: ((Int) -> Unit)? = null
    class MenuListViewHolder(val binding: LayoutWeekRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {
        val binding = LayoutWeekRvItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MenuListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuListViewHolder, position: Int) {
        val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams

        if (position == 0) {
            // Add extra start margin for the first item
            layoutParams.marginStart = context.resources.getDimensionPixelSize(R.dimen.edge_item_margin)
        } else if (position == itemCount - 1) {
            // Add extra end margin for the last item
            layoutParams.marginEnd = context.resources.getDimensionPixelSize(R.dimen.edge_item_margin)
        } else {
            // Reset margins for other items
            layoutParams.marginStart = 0
            layoutParams.marginEnd = 0
        }

        holder.itemView.layoutParams = layoutParams

        // Set text and highlight for the selected item
        holder.binding.apply {
            weekNumber.text = list?.get(position) ?: ""

            if (position == selected) {
                onSelected?.invoke(position)
                weekText.setTextColor(ContextCompat.getColor(context, R.color.primary))
                weekNumber.setTextColor(ContextCompat.getColor(context, R.color.primary))
                selector.setBackgroundResource(R.drawable.item_carousal_bg)
            } else {
                weekText.setTextColor(ContextCompat.getColor(context, R.color.dark_text))
                weekNumber.setTextColor(ContextCompat.getColor(context, R.color.dark_text))
                selector.setBackgroundResource(R.color.tp)
            }
        }
    }


    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    fun updateSelectedPosition(newPosition: Int) {
        if (selected != newPosition) {
            val oldPosition = selected
            selected = newPosition
            notifyItemChanged(oldPosition)
            notifyItemChanged(newPosition)
        }
    }
}
