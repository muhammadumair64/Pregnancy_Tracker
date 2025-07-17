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
import com.example.pregnancytrackerignite.data.models.KickDataClass
import com.example.pregnancytrackerignite.data.models.ReportDataClass
import com.example.pregnancytrackerignite.databinding.ItemRecordsBinding
import com.example.pregnancytrackerignite.databinding.ItemReportsBinding
import com.iobits.videocompressor.utils.toFormattedDate
import com.iobits.videocompressor.utils.visible
import java.io.File

class KickRecordRvAdapter(
    val context: Context
) : RecyclerView.Adapter<KickRecordRvAdapter.MenuListViewHolder>() {

    var onClick: ((position: KickDataClass) -> Unit)? = null
    private var reportList = ArrayList<KickDataClass>()

    class MenuListViewHolder(val binding: ItemRecordsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {
        val binding = ItemRecordsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuListViewHolder, position: Int) {
        val model = reportList[position]
        holder.binding.apply {
         kickDate.text = model.date
            count.text = model.kickCount
        }
        holder.itemView.setOnClickListener {

        }
    }

    fun updateList(mlist: ArrayList<KickDataClass>?) {
        mlist?.reverse()
        reportList.clear()
        if (mlist != null) {
            reportList.addAll(mlist)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return reportList.size ?: 0
    }
}