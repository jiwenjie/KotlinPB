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
    private var mViewToast: Toast? = null

    @SuppressLint("ShowToast")
    @JvmStatic
    fun showToast(context: Context, msg: String,
                  length: Int = Toast.LENGTH_SHORT) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, length)
        } else {
            mToast!!.setText(msg)
        }

        mToast!!.show()
    }

    @JvmStatic
    fun showCustomViewToast(context: Context, view: View,
                            gravity: Int = Gravity.CENTER,
                            offsetX: Int = 0, offsetY: Int = 0) {
        if (mViewToast == null) {
            mViewToast = Toast(context)
        }
        mViewToast!!.view = view
        mViewToast!!.setGravity(gravity, offsetX, offsetY)
        mViewToast!!.show()
    }

    @JvmStatic
    fun cancelToast() {
        if (mToast != null) {
            mToast!!.cancel()
            mToast = null
        }

        if (mViewToast != null) {
            mViewToast!!.cancel()
            mViewToast = null
        }
    }
}