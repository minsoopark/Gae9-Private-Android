package io.github.minsoopark.gae9.network.models

data class BaseResponse<T>(var meta: Map<String, Any>, var response: T)