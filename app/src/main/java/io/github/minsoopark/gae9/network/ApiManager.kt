package io.github.minsoopark.gae9.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import io.github.minsoopark.gae9.network.services.TrendService
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory

object ApiManager {
    private val baseUrl = "http://api.gae9.com/"
    private val retrofit: Retrofit

    init {
        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .setLenient()
                .create()

        val clientBuilder = OkHttpClient.Builder()
        val client = clientBuilder.build()

        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    fun getTrendService(): TrendService = retrofit.create(TrendService::class.java)
}