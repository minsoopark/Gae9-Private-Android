package io.github.minsoopark.gae9.network.models

import java.io.Serializable

data class Trend(var id: String, var title: String, var thumbUrl: String,
                 var commentsCnt: Int, var sites: List<Site>, var new: Boolean?) : Serializable
