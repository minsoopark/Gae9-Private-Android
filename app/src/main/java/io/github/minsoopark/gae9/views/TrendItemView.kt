package io.github.minsoopark.gae9.views

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import io.github.minsoopark.gae9.R
import io.github.minsoopark.gae9.activities.DetailActivity
import io.github.minsoopark.gae9.network.models.Trend

class TrendItemView : RelativeLayout {

    private lateinit var ivThumb: ImageView
    private lateinit var txtTitle: TextView
    private lateinit var txtSites: TextView

    constructor(context: Context?) : super(context) { initView() }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { initView() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { initView() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) { initView() }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.layout_trend_item, this, true)

        ivThumb = findViewById(R.id.iv_thumb) as ImageView
        txtTitle = findViewById(R.id.txt_title) as TextView
        txtSites = findViewById(R.id.txt_sites) as TextView
    }

    fun setTrend(trend: Trend) {
        Glide.with(context).load(trend.thumbUrl).crossFade().into(ivThumb)

        txtTitle.text = trend.title

        var siteFirst = ""
        var siteSecond = ""

        trend.sites.forEachIndexed { index, site ->
            if (index == 0) siteFirst = site.title
            else if (index == 1) siteSecond = site.title
        }

        txtSites.text = resources.getString(R.string.label_sites, siteFirst, siteSecond, trend.sites.size)

        setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("trend", trend)
            context.startActivity(intent)
        }
    }
}