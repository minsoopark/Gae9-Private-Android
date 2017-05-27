package io.github.minsoopark.gae9.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.minsoopark.gae9.network.models.Trend
import io.github.minsoopark.gae9.views.TrendItemView

class TrendItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    fun setTrend(trend: Trend) {
        val trendItemView = itemView as TrendItemView
        trendItemView.setTrend(trend)
    }
}