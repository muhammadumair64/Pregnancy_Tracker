package com.example.pregnancytrackerignite.presentation.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.pregnancytrackerignite.data.models.MedicineReminderDataClass
import com.example.pregnancytrackerignite.data.models.ReportDataClass
import com.example.pregnancytrackerignite.databinding.ItemReportsBinding
import com.example.pregnancytrackerignite.databinding.LayoutMedicineReminderItemBinding
import com.iobits.videocompressor.utils.toFormattedDate
import com.iobits.videocompressor.utils.visible
import java.io.File

class MedicineItemRvAdapter(
    val context: Context
) : RecyclerView.Adapter<MedicineItemRvAdapter.MenuListViewHolder>() {

    var onClick: ((position: MedicineReminderDataClass) -> Unit)? = null
    private var reminderList = ArrayList<MedicineReminderDataClass>()

    class MenuListViewHolder(val binding: LayoutMedicineReminderItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {
        val binding = LayoutMedicineReminderItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MenuListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuListViewHolder, position: Int) {
        val model = reminderList[position]
        holder.binding.apply {
            name.text = model.name
            timeSlot.text = when (model.medicineTimes.size) {
                1 -> "Once a day"
                2 -> "Twice a day"
                3 -> "Three times a day"
                else -> {
                    "Once a day"
                }
            }
        }
        holder.itemView.setOnClickListener {
            onClick?.invoke(model)
        }
    }

    fun updateList(mlist: ArrayList<MedicineReminderDataClass>?) {
        mlist?.reverse()
        reminderList.clear()
        if (mlist != null) {
            reminderList.addAll(mlist)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return reminderList.size ?: 0
    }
}