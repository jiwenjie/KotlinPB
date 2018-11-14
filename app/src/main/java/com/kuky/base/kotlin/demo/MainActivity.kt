package com.kuky.base.kotlin.demo

import android.content.ActivityNotFoundException
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import com.kuky.base.android.kotlin.RxBus
import com.kuky.base.android.kotlin.baseadapters.BaseListAdapter
import com.kuky.base.android.kotlin.baseutils.LogUtils
import com.kuky.base.android.kotlin.baseutils.ToastUtils
import com.kuky.base.android.kotlin.baseviews.BaseActivity
import com.kuky.base.kotlin.demo.rx.RxBusActivity
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_activity_item.view.*
import kotlin.random.Random

/* BaseActivity Demo */
class MainActivity : BaseActivity() {

    private val mActivities = arrayListOf("mvp.MvpActivity", "rx.RxBusActivity", "vp.VpActivity",
            "empty.NonActivity", "empty.RxResultShowActivity", "empty.JustTestActivity",
            "empty.NotClickActivity", "empty.EmptyActivity")

    private lateinit var mAdapter: ActivityAdapter

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initActivity(savedInstanceState: Bundle?) {
        mAdapter = ActivityAdapter(this@MainActivity, mActivities)
        demo_activity_list.adapter = mAdapter
    }

    override fun setListener() {
        demo_activity_list.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    try {
                        val clazz = Class.forName("$packageName.${mAdapter.getAdapterDataList()[position]}")
                        startActivity(clazz.newInstance()::class.java)
                    } catch (e: ActivityNotFoundException) {
                        ToastUtils.showToast(this@MainActivity, "Activity Not Found! and not click Activities below empty package")
                    }
                }
    }

    override fun handleRxBus() {
        /* register an RxBus instance */
        RxBus.mBus.register(this@MainActivity, RxBusActivity.OrderChangeEvent::class.java,
                Consumer { t ->
                    if (t.changeOrder) {
                        val orderList = ArrayList<Int>()
                        val finalItems = ArrayList<String>()

                        do {
                            val random = Random(System.currentTimeMillis()).nextInt(mActivities.size)
                            orderList.apply {
                                if (!contains(random)) add(random)
                            }
                        } while (orderList.size < mActivities.size)

                        orderList.forEach { finalItems.add(mActivities[it]) }
                        mAdapter.updateDataList(finalItems)
                    }
                }, Consumer { t -> LogUtils.e(t.message) })

        /* or you can register an bus like this if you want switch thread by yourself */
//        RxBus.mBus.register(this@MainActivity,
//                RxBus.mBus
//                        .getObserver(RxBusActivity.OrderChangeEvent::class.java)
//                        .subscribe({ t -> LogUtils.e("${t.changeOrder}") },
//                                { t -> LogUtils.e(t.message!!) }))
    }

    override fun onDestroy() {
        super.onDestroy()
        /* remember to unregister RxBus instance when activity destroyed */
        RxBus.mBus.unregister(this@MainActivity)
    }

    /* ListView Adapter Demo */
    class ActivityAdapter(context: Context, activities: ArrayList<String>? = null) :
            BaseListAdapter<String, ActivityHolder>(context, activities) {

        override fun createViewHolder(itemView: View): ActivityHolder {
            val holder = ActivityHolder()
            holder.activityName = itemView.activity_name
            return holder
        }

        override fun convertItemView(holder: ActivityHolder, data: String) {
            holder.activityName.text = data
        }

        override fun getAdapterLayoutId(): Int = R.layout.list_activity_item
    }

    class ActivityHolder : BaseListAdapter.BaseListHolder() {
        lateinit var activityName: TextView
    }
}
