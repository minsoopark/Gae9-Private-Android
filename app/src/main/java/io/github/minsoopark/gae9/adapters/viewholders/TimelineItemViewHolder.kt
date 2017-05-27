package io.github.minsoopark.gae9.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.minsoopark.gae9.adapters.compats.Timeline
import io.github.minsoopark.gae9.views.TimelineItemView

class TimelineItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    fun setTimeline(timeline: Timeline) {
        val timelineItemView = itemView as TimelineItemView
        timelineItemView.setTimeline(timeline)
    }
}
