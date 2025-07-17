package com.example.pregnancytrackerignite.data.horizontalCallender

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.DateModel
import com.example.pregnancytrackerignite.data.utils.OnDateSelectListener
import com.example.pregnancytrackerignite.data.horizontalCallender.HorizontalCalendar.LayoutClickListener


class HorizontalCalendarAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    LayoutClickListener {
    private var mNoOfItems = 5
    private var mValues: MutableList<DateModel>? = null
    private var mRowIndex = -1
    private var mHorizontalCalendar: HorizontalCalendar? = null
    private var mOnDateSelectListener: OnDateSelectListener? = null
    private var mWidth = 0

    fun setOnDateSelectListener(onDateSelectListener: OnDateSelectListener?) {
        mOnDateSelectListener = onDateSelectListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_date, parent, false)
        if (mWidth != 0) {
            try {
                view.layoutParams.width = mWidth
                if (mWidth >= 115) {
                    val relativeLayout = view.findViewById<RelativeLayout>(R.id.relativeLayout)
                    val differenceWidth = (mWidth - relativeLayout.layoutParams.width) / mNoOfItems
                    relativeLayout.layoutParams.width += differenceWidth
                    relativeLayout.layoutParams.height += differenceWidth
                    relativeLayout.requestLayout()
                    relativeLayout.invalidate()
                    view.layoutParams.height = relativeLayout.layoutParams.height
                }
                view.requestLayout()
                view.invalidate()
            } catch (ignore: Exception) {
            }
        }
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DateViewHolder) holder.setData(mValues!![position], position)
    }

    override fun getItemCount(): Int {
        return if (mValues != null && !mValues!!.isEmpty()) mValues!!.size else 0
    }

    fun setData(values: MutableList<DateModel>?, horizontalCalendar: HorizontalCalendar?) {
        if (mValues != null) mValues!!.clear()
        mValues = values
        mHorizontalCalendar = horizontalCalendar
//        calculateLayoutParam(mHorizontalCalendar!!.context)
        mHorizontalCalendar!!.setLayoutClickListener(this)
    }

    override fun onLayoutClick(position: Int) {
        notifyItemChanged(position)
    }

    override fun onLayoutClick(position: Int, recyclerView: RecyclerView) {
        var recyclerView: RecyclerView? = recyclerView
        var oldHolder: RecyclerView.ViewHolder? = null
        if (mRowIndex != -1) oldHolder = recyclerView!!.findViewHolderForAdapterPosition(mRowIndex)
        var holder = recyclerView!!.findViewHolderForAdapterPosition(position)
        val old = mRowIndex
        mRowIndex = position
        if (old != -1) {
            if (oldHolder != null) onBindViewHolder(oldHolder, old)
            else if (!recyclerView.isComputingLayout && (recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE)) onLayoutClick(
                old
            )
        }
        if (holder != null) onBindViewHolder(holder, mRowIndex)
        else if (!recyclerView.isComputingLayout && (recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE)) onLayoutClick(
            mRowIndex
        )
        recyclerView = null
        oldHolder = null
        holder = null
    }


    override fun getSelection(): Int {
        return mRowIndex
    }

    private inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val dayOfTheWeekTextView: TextView = itemView.findViewById(R.id.dayOfTheWeekTextView)
        private val relativeLayout: RelativeLayout? = itemView.findViewById(R.id.relativeLayout)
        private val cardView: CardView? = itemView.findViewById(R.id.materialCardView)

        fun setData(values: DateModel, position: Int) {
            dateTextView.text = values.day.toString() + ""
            dayOfTheWeekTextView.text = values.dayOfWeek
            if (mHorizontalCalendar!!.mDateFontStyle != null && !mHorizontalCalendar!!.mDateFontStyle.isEmpty()) {
                dateTextView.typeface = Typeface.createFromAsset(
                    itemView.resources.assets,
                    mHorizontalCalendar!!.mDateFontStyle
                )
                dayOfTheWeekTextView.typeface = Typeface.createFromAsset(
                    itemView.resources.assets,
                    mHorizontalCalendar!!.mDateFontStyle
                )
            }
            if (mHorizontalCalendar!!.mDateTextSize != -1) {
                dateTextView.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    mHorizontalCalendar!!.mDateTextSize.toFloat()
                )
            }
            if (mHorizontalCalendar!!.mWeekTextSize != -1) {
                dayOfTheWeekTextView.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    mHorizontalCalendar!!.mWeekTextSize.toFloat()
                )
            }
            if (mRowIndex == position) {
//                relativeLayout?.setBackgroundResource(mHorizontalCalendar!!.mSelectedBgResourceId)
                cardView?.visibility = View.VISIBLE
                dateTextView.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.theme_text_color
                    )
                )
                dayOfTheWeekTextView.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.theme_text_color
                    )
                )
                if (mOnDateSelectListener != null) mOnDateSelectListener!!.onSelect(values)
            } else {
//              relativeLayout?.setBackgroundResource(mHorizontalCalendar!!.mBgResourceId)
                cardView?.visibility = View.INVISIBLE
                dateTextView.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.dark_gray
                    )
                )
                dayOfTheWeekTextView.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.dark_gray
                    )
                )
            }
            itemView.setOnClickListener {
                val old = mRowIndex
                mRowIndex = absoluteAdapterPosition
                notifyItemChanged(old)
                notifyItemChanged(mRowIndex)
            }
        }
    }

    /**
     * displays 5 or 3 items in the list according to screen size and list size
     *
     * @param context
     */
     fun calculateLayoutParam(context: Context) {
        try {
            mNoOfItems =
                if (mValues != null && !mValues!!.isEmpty()) (if (mValues!!.size > 4) 5 else mValues!!.size) else 0
            val displaymetrics = DisplayMetrics()
            val activity = context as Activity
            activity.windowManager.defaultDisplay.getMetrics(displaymetrics)
            mWidth = displaymetrics.widthPixels
            if (mNoOfItems == 5 && mWidth < 620) {
                mNoOfItems = 3
            }
            if (mNoOfItems != 0) {
                mWidth = ((mWidth - (70 * displaymetrics.density)) / mNoOfItems).toInt()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
