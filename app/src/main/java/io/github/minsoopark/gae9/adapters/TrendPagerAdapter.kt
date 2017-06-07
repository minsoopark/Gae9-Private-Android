package io.github.minsoopark.gae9.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import io.github.minsoopark.gae9.activities.DetailFragment
import io.github.minsoopark.gae9.network.models.Trend

class TrendPagerAdapter(fm: FragmentManager, private val trends: List<Trend>) : FragmentPagerAdapter(fm) {
    override fun getPageTitle(position: Int): CharSequence {
        val trend = trends[position]
        return trend.title
    }

    override fun getItem(position: Int): Fragment {
        val trend = trends[position]
        return DetailFragment.newInstance(trend)
    }

    override fun getCount() = trends.size

}