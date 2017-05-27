package io.github.minsoopark.gae9.views

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.TextView
import io.github.minsoopark.gae9.R
import io.github.minsoopark.gae9.activities.InAppBrowserActivity
import io.github.minsoopark.gae9.adapters.compats.Timeline

class TimelineItemView : RelativeLayout {

    private lateinit var containerCard: CardView
    private lateinit var txtTitle: TextView
    private lateinit var txtMeta: TextView

    constructor(context: Context?) : super(context) { initView() }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { initView() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { initView() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) { initView() }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.layout_timeline_item, this, true)
        containerCard = findViewById(R.id.container_card) as CardView
        txtTitle = findViewById(R.id.txt_title) as TextView
        txtMeta = findViewById(R.id.txt_meta) as TextView
    }

    fun setTimeline(timeline: Timeline) {
        val title = timeline.title
        val meta = timeline.meta
        val url = timeline.url

        txtTitle.text = title
        txtMeta.text = meta

        containerCard.setOnClickListener {
            val intent = Intent(context, InAppBrowserActivity::class.java)
            intent.putExtra("url", url)
            context.startActivity(intent)
        }
    }
}