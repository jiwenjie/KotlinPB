package com.kuky.base.android.kotlin.baseviews

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.kuky.base.android.kotlin.R

/**
 * @author kuky.
 * @description popupWindow 基类，推荐使用
 * @see BaseDialogFragment 作为 popupWindow 基类
 */
abstract class BasePopupWindow(
    context: Context, animStyle: Int = R.style.AnimBottomIn,
    width: Int = WindowManager.LayoutParams.MATCH_PARENT,
    height: Int = WindowManager.LayoutParams.WRAP_CONTENT
) : PopupWindow(context) {

    protected val mContext = context
    protected val mPopupView: View = LayoutInflater.from(mContext).inflate(this.getLayoutId(), null, false)
    private val mAnimStyle = animStyle
    private val mWidth = width
    private val mHeight = height

    init {
        initPopupWindow()
    }

    private fun initPopupWindow() {
        contentView = mPopupView
        setBackgroundDrawable(ColorDrawable(0))
        initPopupView()
        animationStyle = mAnimStyle
        width = mWidth
        height = mHeight
        isFocusable = true
        setOnDismissListener { setBackgroundAlpha(1.0f) }
    }

    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int, gravity: Int) {
        setBackgroundAlpha(0.6f)
        super.showAsDropDown(anchor, xoff, yoff, gravity)
    }

    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int) {
        this.showAsDropDown(anchor, xoff, yoff, Gravity.TOP or Gravity.START)
    }

    override fun showAsDropDown(anchor: View?) {
        this.showAsDropDown(anchor, 0, 0)
    }

    override fun showAtLocation(parent: View?, gravity: Int, x: Int, y: Int) {
        setBackgroundAlpha(0.6f)
        super.showAtLocation(parent, gravity, x, y)
    }

    fun showAtLocation(parent: View?) =
        this.showAtLocation(parent, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)

    private fun setBackgroundAlpha(alpha: Float) {
        val lp = (mContext as Activity).window.attributes
        lp.alpha = alpha
        mContext.window.attributes = lp
    }

    protected abstract fun getLayoutId(): Int

    protected open fun initPopupView() {}
}