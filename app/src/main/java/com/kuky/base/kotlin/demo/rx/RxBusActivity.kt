package com.kuky.base.kotlin.demo.rx

import android.os.Bundle
import com.kuky.base.android.kotlin.RxBus
import com.kuky.base.android.kotlin.baseutils.ToastUtils
import com.kuky.base.android.kotlin.baseviews.BaseActivity
import com.kuky.base.kotlin.demo.R
import kotlinx.android.synthetic.main.activity_rx_bus.*

class RxBusActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_rx_bus

    override fun initActivity(savedInstanceState: Bundle?) {

    }

    override fun setListener() {
        send_event.setOnClickListener {
            RxBus.mBus.post(OrderChangeEvent())
            ToastUtils.showToast(this@RxBusActivity, "MainActivity data list order has change")
        }
    }

    class OrderChangeEvent(val changeOrder: Boolean = true)
}
