package com.kuky.base.android.kotlin.baseutils

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Toast

/**
 * @author kuky.
 * @description
 */
object ToastUtils {
    private var mToast: Toast? = null

    @SuppressLint("ShowToast")
    @JvmStatic
    fun showToast(context: Context, msg: String, length: Int = Toast.LENGTH_SHORT,
                  toastView: View? = null, gravity: Int = Gravity.BOTTOM,
                  xOffset: Int = 0, yOffset: Int = 0) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, length)
        } else {
            mToast!!.setText(msg)
        }

        if (toastView != null)
            mToast!!.view = toastView

        mToast!!.setGravity(gravity, xOffset, yOffset)
        mToast!!.show()
    }

    @JvmStatic
    fun cancelToast() {
        if (mToast != null) {
            mToast!!.cancel()
            mToast = null
        }
    }
}