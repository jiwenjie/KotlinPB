package com.kuky.base.android.kotlin

import android.text.TextUtils
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalStateException
import java.util.concurrent.TimeUnit

/**
 * @author kuky.
 * @description retrofit 单例生成管理类
 */
object RetrofitManager {
    private const val TAG = "RetrofitManager"
    private const val CONN_TIME = 5
    private const val READ_TIME = 10
    private const val WRITE_TIME = 30
    private var BASE_URL: String = ""

    /**
     * @see RetrofitManager.setBaseUrl 调用 mRetrofit 之前必须优先调用，来设置服务器地址
     */
    val mRetrofit: Retrofit by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {

        if (TextUtils.isEmpty(BASE_URL))
            throw IllegalStateException("Empty url and call setBaseUrl first before get an instance of retrofit")

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(genericOkClient())
            .build()
    }

    fun setBaseUrl(url: String) {
        BASE_URL = url
    }

    private fun genericOkClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor {
            HttpLoggingInterceptor.Logger { message -> Log.i(TAG, message) }
        }

        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .connectTimeout(CONN_TIME.toLong(), TimeUnit.SECONDS)
            .readTimeout(READ_TIME.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME.toLong(), TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }
}