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
import com.example.pregnancytrackerignite.data.models.ReportDataClass
import com.example.pregnancytrackerignite.databinding.ItemReportsBinding
import com.iobits.videocompressor.utils.toFormattedDate
import com.iobits.videocompressor.utils.visible
import java.io.File

class ReportsRvAdapter(
    val context: Context
) : RecyclerView.Adapter<ReportsRvAdapter.MenuListViewHolder>() {

    var onClick: ((position: ReportDataClass) -> Unit)? = null
    var onClickMenu: ((report: ReportDataClass , view:View) -> Unit)? = null
    private var reportList = ArrayList<ReportDataClass>()

    class MenuListViewHolder(val binding: ItemReportsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {
        val binding = ItemReportsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuListViewHolder, position: Int) {
        val model = reportList[position]
        holder.binding.apply {
            descriptionTxt.text = model.notes
            timeSlot.text = model.date.toFormattedDate()
            reportTitle.text = model.title
            weekText.text = model.week
            renderPdfPageToImageView(File(model.filePath),0,reportImage)
            menu.setOnClickListener {
                onClickMenu?.invoke(model,it)
            }
        }
        holder.itemView.setOnClickListener {
            onClick?.invoke(model)
        }
    }

    fun updateList(mlist: ArrayList<ReportDataClass>?) {
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
    private fun renderPdfPageToImageView(pdfFile: File, pageIndex: Int, imageView: ImageView) {
        // Open the file descriptor
        val fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)

        // Create a PdfRenderer instance
        val pdfRenderer = PdfRenderer(fileDescriptor)

        // Check if the page index is within bounds
        if (pageIndex < 0 || pageIndex >= pdfRenderer.pageCount) {
            pdfRenderer.close()
            fileDescriptor.close()
            return
        }

        // Open the specified page
        val page = pdfRenderer.openPage(pageIndex)

        // Create a bitmap to hold the page image
        val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)

        // Render the page to the bitmap
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

        // Set the bitmap to the ImageView
        imageView.setImageBitmap(bitmap)
        imageView.visible()

        // Close the page and renderer
        page.close()
        pdfRenderer.close()
        fileDescriptor.close()
    }
}