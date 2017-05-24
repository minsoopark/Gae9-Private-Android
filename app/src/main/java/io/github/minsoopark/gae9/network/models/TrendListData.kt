package io.github.minsoopark.gae9.network.models

import java.util.*

data class TrendListData(var trends: List<Trend>, var nextMaxId: Int?, var lastUpdateAt: Date?)
