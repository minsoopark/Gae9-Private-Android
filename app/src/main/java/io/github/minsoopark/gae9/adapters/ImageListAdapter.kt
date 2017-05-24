package io.github.minsoopark.gae9.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import io.github.minsoopark.gae9.R
import io.github.minsoopark.gae9.adapters.viewholders.ImageItemViewHolder
import java.util.ArrayList

class ImageListAdapter(private val context: Context, private val imageUrls: ArrayList<String>)
    : RecyclerView.Adapter<ImageItemViewHolder>() {

    override fun onBindViewHolder(viewHolder: ImageItemViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        viewHolder.setImageUrl(imageUrl)
    }

    override fun getItemCount() = imageUrls.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItemViewHolder {
        val imageView = ImageView(context)
        val layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val verticalMargin = context.resources.getDimensionPixelSize(R.dimen.activity_vertical_margin)
        layoutParams.topMargin = verticalMargin
        layoutParams.bottomMargin = verticalMargin

        imageView.layoutParams = layoutParams
                return ImageItemViewHolder(
                imageView
        )
    }
}