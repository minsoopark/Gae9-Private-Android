package io.github.minsoopark.gae9.network.services

import io.github.minsoopark.gae9.network.models.BaseResponse
import io.github.minsoopark.gae9.network.models.TrendListData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

interface TrendService {
    /**
     * 인기 트렌드 목록
     */
    @GET("trend")
    fun getTrend(): Observable<BaseResponse<TrendListData>>

    /**
     * 일간 트렌드 목록
     * @param date 조회할 날짜 ex) 2017-05-24
     */
    @GET("trend/{date}")
    fun getDaily(@Path("date") date: String): Observable<BaseResponse<TrendListData>>

    /**
     * 최신 트렌드 목록
     * @param maxId 가져올 커서 id
     */
    @GET("trend/new")
    fun getNew(@Query("max_id") maxId: Int): Observable<BaseResponse<TrendListData>>
}
