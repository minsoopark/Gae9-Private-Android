package io.github.minsoopark.gae9.activities

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import io.github.minsoopark.gae9.R
import io.github.minsoopark.gae9.adapters.TrendPagerAdapter
import io.github.minsoopark.gae9.network.models.Trend

class DetailActivity : AppCompatActivity() {

    private lateinit var pagerTrends: ViewPager
    private var adapter: TrendPagerAdapter? = null

    private var trends: List<Trend>? = null
    private var index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        pagerTrends = findViewById(R.id.pager_trends) as ViewPager

        trends = intent.getSerializableExtra("trends") as List<Trend>?
        index = intent.getIntExtra("index", 0)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initView()
        initEvent()
        
        pagerTrends.currentItem = index
    }

    private fun initView() {
        trends?.let {
            adapter = TrendPagerAdapter(supportFragmentManager, it)
            pagerTrends.adapter = adapter
        }
    }

    private fun initEvent() {
        pagerTrends.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(scrollState: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                supportActionBar?.title = adapter?.getPageTitle(position)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}