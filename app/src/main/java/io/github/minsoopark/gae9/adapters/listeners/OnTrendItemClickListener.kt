package io.github.minsoopark.gae9.adapters.listeners

import io.github.minsoopark.gae9.network.models.Trend

interface OnTrendItemClickListener {
    fun onItemClick(index: Int, trend: Trend)
}