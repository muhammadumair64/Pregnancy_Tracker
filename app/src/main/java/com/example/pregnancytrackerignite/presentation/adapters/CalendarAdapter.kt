package com.example.pregnancytrackerignite.presentation.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.pregnancytrackerignite.R
import com.iobits.videocompressor.utils.gone
import com.iobits.videocompressor.utils.visible

class CalendarAdapter(
    val context: Context,
    private val days: List<String>,
    private val ovulationDay: Int,
    private val fertileDays: List<Int>
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    inner class CalendarViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        val dayView: TextView = itemView.findViewById(R.id.dayText)
        val heartAnim: LottieAnimationView = itemView.findViewById(R.id.heart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day, parent, false) as ViewGroup
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val day = days[position]
        holder.dayView.text = day

        if (day.isNotEmpty()) {
            when (day.toIntOrNull()) {
                ovulationDay -> {
                    holder.dayView.setBackgroundResource(R.drawable.ovulation_day_background)
                    holder.dayView.setTextColor(ContextCompat.getColor(context,R. color. primary))
                    holder.heartAnim.visible()
                }
                in fertileDays -> {
                    holder.dayView.setBackgroundResource(R.drawable.fertile_day_background)
                    holder.dayView.setTextColor(Color.BLACK)
                    holder.heartAnim.gone()
                }
                else -> {
                    holder.dayView.setBackgroundColor(Color.TRANSPARENT)
                    holder.heartAnim.gone()
                }
            }
        } else {
            holder.dayView.setBackgroundColor(Color.TRANSPARENT)
            holder.dayView.text = ""
        }
    }

    override fun getItemCount(): Int = days.size
}
