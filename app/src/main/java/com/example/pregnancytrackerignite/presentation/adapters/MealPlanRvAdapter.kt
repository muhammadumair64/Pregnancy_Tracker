package com.example.pregnancytrackerignite.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.BpModel
import com.example.pregnancytrackerignite.data.models.ReportDataClass
import com.example.pregnancytrackerignite.data.utils.Meal
import com.example.pregnancytrackerignite.databinding.LayoutDietItemBinding


class MealPlanRvAdapter(
    val context: Context,
    private val onClick: (Meal?) -> Unit
) : RecyclerView.Adapter<MealPlanRvAdapter.MenuListViewHolder>() {
    private var mList = ArrayList<Meal>()
    class MenuListViewHolder(val binding: LayoutDietItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {
        val binding =
            LayoutDietItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuListViewHolder, position: Int) {
        val model = mList?.get(position)
        val mealTitle = when (position) {
            0 -> {
                "Breakfast"
            }

            1 -> {
                "Snacks"
            }

            2 -> {
                "Lunch"
            }

            3 -> {
                "Snacks"
            }

            4 -> {
                "Dinner"
            }

            else -> {
                "Breakfast"
            }
        }

        holder.binding.apply {
            prepTime.text = model?.prepTime
            cockTime.text = model?.cookTime
            title.text = mealTitle
            mealName.text = model?.name
            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.diet_placeholder)
                .error(R.drawable.diet_placeholder)
            Glide.with(context).load(model?.imageUrl).apply(options).into(image)

            root.setOnClickListener {
             onClick.invoke(model)
            }
        }
    }


    fun updateList(mlist: ArrayList<Meal>?) {
        mList.clear()
        if (mlist != null) {
            mList.addAll(mlist)
        }
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

}