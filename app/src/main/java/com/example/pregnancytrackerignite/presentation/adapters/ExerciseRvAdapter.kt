package com.example.pregnancytrackerignite.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pregnancytrackerignite.data.models.ExerciseType
import com.example.pregnancytrackerignite.databinding.LayoutExerciseItemBinding

class ExerciseRvAdapter(
    val context: Context,
    private var kegelList: List<ExerciseType>?,
    private val onClick: (ExerciseType) -> Unit
) : RecyclerView.Adapter<ExerciseRvAdapter.MenuListViewHolder>() {


    class MenuListViewHolder(val binding: LayoutExerciseItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {
        val binding = LayoutExerciseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuListViewHolder, position: Int) {
        val model = kegelList?.get(position)

        holder.binding.apply {
            exerciseName.text = model?.name
            Glide.with(context).load(model?.image).into(exerciseImage)
            root.setOnClickListener {
                model?.let { exercise ->
                    onClick(exercise)
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return kegelList?.size ?: 0
    }

}