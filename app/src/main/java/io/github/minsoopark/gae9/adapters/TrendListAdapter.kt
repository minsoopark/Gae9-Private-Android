package io.github.minsoopark.gae9.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.github.minsoopark.gae9.adapters.listeners.OnTrendItemClickListener
import io.github.minsoopark.gae9.adapters.viewholders.TrendItemViewHolder
import io.github.minsoopark.gae9.network.models.Trend
import io.github.minsoopark.gae9.views.TrendItemView

class TrendListAdapter(private val context: Context, private val trends: List<Trend>)
    : RecyclerView.Adapter<TrendItemViewHolder>() {

    private var onTrendItemClickListener: OnTrendItemClickListener? = null

    fun setOnTrendItemClickListener(onTrendItemClickListener: OnTrendItemClickListener) {
        this.onTrendItemClickListener = onTrendItemClickListener
    }

    override fun onBindViewHolder(viewHolder: TrendItemViewHolder, position: Int) {
        val trend = trends[position]
        viewHolder.setTrend(trend)
        viewHolder.itemView.setOnClickListener {
            onTrendItemClickListener?.onItemClick(position, trend)
        }
    }

    override fun getItemCount() = trends.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendItemViewHolder {
        return TrendItemViewHolder(
                TrendItemView(context)
        )
    }
}