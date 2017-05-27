package io.github.minsoopark.gae9.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import io.github.minsoopark.gae9.R
import io.github.minsoopark.gae9.adapters.compats.ImageListCompatItem
import io.github.minsoopark.gae9.adapters.compats.Timeline
import io.github.minsoopark.gae9.adapters.viewholders.ImageItemViewHolder
import io.github.minsoopark.gae9.adapters.viewholders.TimelineItemViewHolder
import io.github.minsoopark.gae9.views.TimelineItemView
import kotlin.collections.ArrayList

class ImageListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private val VIEW_TYPE_IMAGE = 0
    private val VIEW_TYPE_TIMELINE = 1

    private val context: Context
    private val imageUrls: ArrayList<String>
    private val timelines: ArrayList<Timeline>
    private val compatItems: ArrayList<ImageListCompatItem>

    constructor(context: Context, imageUrls: ArrayList<String>, timelines: ArrayList<Timeline>) : super() {
        this.context = context
        this.imageUrls = imageUrls
        this.timelines = timelines
        this.compatItems = buildCompatItems()
    }


    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is ImageItemViewHolder -> {
                val imageUrl = compatItems[position].imageUrl ?: return
                viewHolder.setImageUrl(imageUrl)
            }
            is TimelineItemViewHolder -> {
                val timeline = compatItems[position].timeline ?: return
                viewHolder.setTimeline(timeline)
            }
        }
    }

    override fun getItemCount() = compatItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        return when (viewType) {
            VIEW_TYPE_IMAGE -> {
                val imageView = ImageView(context)
                val layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val verticalMargin = context.resources.getDimensionPixelSize(R.dimen.activity_vertical_margin)
                layoutParams.topMargin = verticalMargin
                layoutParams.bottomMargin = verticalMargin

                imageView.layoutParams = layoutParams
                ImageItemViewHolder(imageView)
            }
            VIEW_TYPE_TIMELINE -> {
                val itemView = TimelineItemView(context)
                TimelineItemViewHolder(itemView)
            }
            else -> null
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (compatItems[position].type) {
            ImageListCompatItem.Type.IMAGE -> VIEW_TYPE_IMAGE
            ImageListCompatItem.Type.TIMELINE -> VIEW_TYPE_TIMELINE
            else -> -1
        }
    }

    private fun buildCompatItems(): ArrayList<ImageListCompatItem> {
        val items = ArrayList<ImageListCompatItem>()
        imageUrls.forEach {
            val item = ImageListCompatItem(it)
            items.add(item)
        }
        timelines.forEach {
            val item = ImageListCompatItem(it)
            items.add(item)
        }
        return items
    }
}