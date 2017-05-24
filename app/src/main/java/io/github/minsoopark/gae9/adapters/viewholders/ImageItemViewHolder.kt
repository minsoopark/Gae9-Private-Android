package io.github.minsoopark.gae9.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

class ImageItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    fun setImageUrl(imageUrl: String) {
        val ivImage = itemView as ImageView
        Glide.with(itemView.context).load(imageUrl).crossFade().into(ivImage)
    }
}
