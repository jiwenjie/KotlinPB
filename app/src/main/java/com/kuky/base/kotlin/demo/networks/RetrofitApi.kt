package com.kuky.base.kotlin.demo.networks

import com.kuky.base.kotlin.demo.entity.NewsEntity
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * @author kuky.
 * @description
 */
interface RetrofitApi {

    /**
     * type
     * key
     */
    @GET("/toutiao/index")
    fun getNews(@Query("type") type: String, @Query("key") key: String): Observable<NewsEntity>
}