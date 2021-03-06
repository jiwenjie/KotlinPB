package com.kuky.base.android.kotlin.baseviews

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.kuky.base.android.kotlin.R

/**
 * @author kuky.
 * @description
 */
abstract class BaseDialog(context: Context, themeId: Int = R.style.AlertDialogStyle) :
        Dialog(context, themeId) {

    protected val mContext = context
    private val mInflater = LayoutInflater.from(mContext)
    protected val mDialogView: View

    init {
        mDialogView = mInflater.inflate(this.getLayoutId(), null, false)
        this.initDialogView()
        this.setContentView(mDialogView)
        this.setDialogWidth((mContext.resources.displayMetrics.widthPixels * 0.8).toInt())
        this.setDialogHeight(WindowManager.LayoutParams.WRAP_CONTENT)
    }

    open fun setDialogHeight(height: Int) {
        val lp = window!!.attributes
        lp.height = height
        window!!.attributes = lp
    }

    open fun setDialogWidth(width: Int) {
        val lp = window!!.attributes
        lp.width = width
        window!!.attributes = lp
    }

    abstract fun getLayoutId(): Int

    protected open fun initDialogView() {}
}