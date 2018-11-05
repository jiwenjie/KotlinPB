package com.kuky.base.kotlin.demo

import com.kuky.base.kotlin.demo.entity.NewsEntity
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * @author kuky.
 * @description
 */
interface RetrofitApi {

    /**
     * key
     */
    @GET("/toutiao/index")
    fun getNews(@QueryMap param: HashMap<String, Any>): Observable<NewsEntity>
}