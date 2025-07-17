package com.example.pregnancytrackerignite.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pregnancytrackerignite.data.models.CountryModel
import com.example.pregnancytrackerignite.databinding.ItemCountryBinding

class CountryAdapter(
    private val countryList: List<CountryModel>,
    private val onItemClicked: (CountryModel) -> Unit
) :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    inner class CountryViewHolder(private val binding: ItemCountryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(country: CountryModel) {
            binding.countryImage.setImageResource(country.imageResId)
            binding.countryName.text = country.name
            binding.root.setOnClickListener {
                onItemClicked(country)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countryList[position])
    }

    override fun getItemCount(): Int = countryList.size
}