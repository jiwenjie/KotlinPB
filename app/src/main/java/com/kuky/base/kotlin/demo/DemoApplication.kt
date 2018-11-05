package com.kuky.base.kotlin.demo

import android.app.Application
import com.kuky.base.android.kotlin.RetrofitManager

/**
 * @author kuky.
 * @description
 */
class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        /* 初始化 baseUrl*/
        RetrofitManager.setBaseUrl(Constant.BASE_NEWS_URL)
    }
}