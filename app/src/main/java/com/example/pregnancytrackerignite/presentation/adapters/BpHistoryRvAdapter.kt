package com.example.pregnancytrackerignite.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pregnancytrackerignite.data.models.BpModel
import com.example.pregnancytrackerignite.data.models.ExerciseType
import com.example.pregnancytrackerignite.databinding.ItemBpHistoryBinding
import com.example.pregnancytrackerignite.databinding.LayoutExerciseItemBinding
import com.iobits.videocompressor.utils.formatToCustomString

class BpHistoryRvAdapter(
    val context: Context
) : RecyclerView.Adapter<BpHistoryRvAdapter.MenuListViewHolder>() {

    var onClick: ((position: BpModel) -> Unit)? = null
    private var historyList = ArrayList<BpModel>()

    class MenuListViewHolder(val binding: ItemBpHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {
        val binding =
            ItemBpHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuListViewHolder, position: Int) {
        val model = historyList[position]
        holder.binding.apply {
           systolicValue.text = model.systolic.toString() + "mmhg"
           diastolicValue.text = model.diastolic.toString() + "mmhg"
           pulseValue.text = model.pulse.toString() + "bpm"
           bloodPressureDate.text = model.date.formatToCustomString()
        }
    }

    fun updateList(mlist: ArrayList<BpModel>?) {
        mlist?.reverse()
        historyList.clear()
        if (mlist != null) {
            historyList.addAll(mlist)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return historyList?.size ?: 0
    }

}