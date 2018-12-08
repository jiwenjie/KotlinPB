package com.kuky.base.android.kotlin.baseadapters

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

/**
 * @author kuky.
 * @description an LayoutManager for RecyclerView, when you need smoothScroll to an item,
 * and this item need to be the firstItemVisible, what you need do is put the position of item into
 * @see RecyclerView.smoothScrollToPosition
 * only for Vertical LinearLayoutManager
 */
class SmoothScrollerLinearLayouManager : LinearLayoutManager {

    constructor(context: Context) : this(context, LinearLayoutManager.VERTICAL)

    constructor(context: Context, @RecyclerView.Orientation orientation: Int, reverseLayout: Boolean = false) :
            super(context, orientation, reverseLayout)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes)


    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
        val linearSmoothScroller = CustomLinearScroller(recyclerView!!.context)
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }

}

class CustomLinearScroller(context: Context) : LinearSmoothScroller(context) {

    override fun calculateDtToFit(viewStart: Int, viewEnd: Int,
                                  boxStart: Int, boxEnd: Int, snapPreference: Int): Int = boxStart - viewStart
}