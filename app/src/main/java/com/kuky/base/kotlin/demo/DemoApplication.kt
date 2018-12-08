package com.kuky.base.kotlin.demo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.kuky.base.android.kotlin.RetrofitManager

/**
 * @author kuky.
 * @description
 */
class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        contextInstance = this.applicationContext
        /* 初始化 baseUrl*/
        RetrofitManager.setBaseUrl(Constant.BASE_NEWS_URL)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var contextInstance: Context
    }
}