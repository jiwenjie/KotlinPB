package com.kuky.base.android.kotlin

import android.app.Activity

/**
 * @author kuky.
 * @description
 */
object ActivityStackManager {

    private val activities = ArrayList<Activity>()

    /**
     * onCreate 调用
     */
    @JvmStatic
    fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    /**
     * onDestroy 调用
     */
    @JvmStatic
    fun removeActivity(activity: Activity) {
        if (activities.contains(activity)) {
            activities.remove(activity)
            activity.finish()
        }
    }

    /**
     * 获取栈顶 Activity
     */
    @JvmStatic
    fun getTopActivity(): Activity? = if (activities.isEmpty()) null else activities[activities.size - 1]

    /**
     * 关闭所有 Activity
     */
    @JvmStatic
    fun finishAll() {
        for (a in activities)
            if (!a.isFinishing) a.finish()
    }
}