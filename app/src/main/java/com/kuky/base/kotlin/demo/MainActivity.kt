package com.kuky.base.kotlin.demo

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import com.kuky.base.android.kotlin.baseadapters.BaseListAdapter
import com.kuky.base.android.kotlin.baseviews.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_activity_item.view.*

/* BaseActivity Demo */
class MainActivity : BaseActivity() {

    private val mActivities = arrayListOf("mvp.MvpActivity", "rx.RxBusActivity", "vp.VpActivity")

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initActivity(savedInstanceState: Bundle?) {
        val dataList = ArrayList<String>()
        mActivities.forEach { dataList.add(it.split(".")[1]) }
        demo_activity_list.adapter = ActivityAdapter(this@MainActivity, dataList)
    }

    override fun setListener() {
        demo_activity_list.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    val clazz = Class.forName("$packageName.${mActivities[position]}")
                    startActivity(clazz.newInstance()::class.java)
                }
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
