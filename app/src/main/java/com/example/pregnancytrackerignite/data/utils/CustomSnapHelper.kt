package com.example.pregnancytrackerignite.data.utils

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

class CustomSnapHelper : LinearSnapHelper() {
    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        val linearLayoutManager = layoutManager as? LinearLayoutManager ?: return null

        val firstVisible = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
        val lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition()
        val itemCount = layoutManager.itemCount

        // If first or last item is fully visible, snap to it
        if (firstVisible == 0) {
            return layoutManager.findViewByPosition(0)
        }
        if (lastVisible == itemCount - 1) {
            return layoutManager.findViewByPosition(itemCount - 1)
        }

        // Default snap behavior
        return super.findSnapView(layoutManager)
    }
}