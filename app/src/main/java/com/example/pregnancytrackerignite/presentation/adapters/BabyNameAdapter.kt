package com.example.pregnancytrackerignite.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.BabiesNameModel
import com.example.pregnancytrackerignite.databinding.ItemBabyNameBinding

class BabyNameAdapter(
    private val onFavoriteIconClicked: (id: Int, isFavorite: Boolean) -> Unit
) : RecyclerView.Adapter<BabyNameAdapter.BabyNameViewHolder>() {

    val babyNames = ArrayList<BabiesNameModel>()
    inner class BabyNameViewHolder(private val binding: ItemBabyNameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(babyName: BabiesNameModel, position: Int) {
            binding.babyName.text = babyName.name
            binding.favoriteIcon.setImageResource(
                if (babyName.isFavorite) R.drawable.icon_favourite_done else R.drawable.icon_favourite
            )

            binding.favoriteIcon.setOnClickListener {
                val newFavoriteStatus = !babyName.isFavorite
                binding.favoriteIcon.setImageResource(
                    if (newFavoriteStatus) R.drawable.icon_favourite_done else R.drawable.icon_favourite
                )
                onFavoriteIconClicked(babyName.id, newFavoriteStatus)
                babyNames[position] = babyName.copy(isFavorite = newFavoriteStatus)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyNameViewHolder {
        val binding = ItemBabyNameBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BabyNameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BabyNameViewHolder, position: Int) {
        holder.bind(babyNames[position], position)
    }

    override fun getItemCount(): Int = babyNames.size

    fun updateList(newBabyNames: List<BabiesNameModel>) {
        babyNames.clear()
        babyNames.addAll(newBabyNames)
        notifyDataSetChanged()
    }
}