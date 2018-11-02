package com.kuky.base.android.kotlin.baseadapters

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author kuky.
 * @description RecyclerView 通用适配器
 *
 * @see BaseRecyclerAdapter.addHeader 支持添加多个头部
 * @see BaseRecyclerAdapter.addFooter 支持添加多个尾部
 * @see BaseRecyclerAdapter.setOnItemClickListener 支持点击事件
 * @see BaseRecyclerAdapter.setOnItemLongClickListener 支持长按事件
 * @see BaseRecyclerAdapter.getConvertType 支持多布局
 */
abstract class BaseRecyclerAdapter<T : Any>(context: Context, dataList: ArrayList<T>? = null) :
    RecyclerView.Adapter<BaseRecyclerAdapter.BaseRecyclerHolder>() {

    private val mHeaderViews: SparseArray<View> = SparseArray()
    private val mFooterViews: SparseArray<View> = SparseArray()
    protected var mContext = context
    protected var mDataList = dataList
    protected var mSelectedPosition = -1
    private val mInflater = LayoutInflater.from(mContext)
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mOnItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        this.mOnItemLongClickListener = listener
    }

    fun updateAdapterData(dataList: ArrayList<T>?) {
        this.mDataList = dataList
        notifyDataSetChanged()
    }

    fun getSelectedPosition(): Int = mSelectedPosition

    fun updateSelectItem(position: Int) {
        this.mSelectedPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerHolder =
        if (haveHeader() && mHeaderViews.get(viewType) != null) BaseRecyclerHolder(mHeaderViews.get(viewType))
        else if (haveFooter() && mFooterViews.get(viewType) != null) BaseRecyclerHolder(mFooterViews.get(viewType))
        else BaseRecyclerHolder(mInflater.inflate(getAdapterLayoutId(), parent, false))

    abstract fun getAdapterLayoutId(): Int

    override fun getItemCount(): Int = getHeaderSize() + getDataSize() + getFooterSize()

    override fun onBindViewHolder(holder: BaseRecyclerHolder, position: Int) {
        if (!isHeader(position) && !isFooter(position)) {
            val pos = position - getHeaderSize()

            convertView(holder.itemView, mDataList!![pos])

            holder.itemView.setOnClickListener { v ->
                mOnItemClickListener?.onItemClick(pos, v)
            }

            holder.itemView.setOnLongClickListener { v ->
                mOnItemLongClickListener?.onItemLongClick(pos, v)
                false
            }
        }
    }

    abstract fun convertView(itemView: View, t: T)

    fun addHeader(header: View) {
        mHeaderViews.put(HEADER + getHeaderSize(), header)
        notifyItemInserted(getHeaderSize())
    }

    fun addFooter(footer: View) {
        mFooterViews.put(FOOTER + getFooterSize(), footer)
        var pos = if (mDataList == null) getFooterSize() else getFooterSize() + getDataSize() - 1
        if (haveHeader()) pos += getHeaderSize()
        notifyItemInserted(pos)
    }

    override fun getItemViewType(position: Int): Int =
        when {
            isHeader(position) -> mHeaderViews.keyAt(position)
            isFooter(position) -> mFooterViews.keyAt(position - getDataSize() - getHeaderSize())
            else -> getConvertType(position)
        }

    protected open fun getConvertType(position: Int): Int = 0

    private fun getHeaderSize(): Int = mHeaderViews.size()

    private fun getDataSize(): Int = mDataList?.size ?: 0

    private fun getFooterSize(): Int = mFooterViews.size()

    private fun haveHeader(): Boolean = mHeaderViews.size() > 0

    private fun haveFooter(): Boolean = mFooterViews.size() > 0

    private fun isHeader(position: Int): Boolean = haveHeader() && position < getHeaderSize()

    private fun isFooter(position: Int): Boolean = haveFooter() && position >= getHeaderSize() + getDataSize()

    private fun getRealPosition(viewHolder: RecyclerView.ViewHolder): Int = viewHolder.layoutPosition

    fun getAdapterData(): ArrayList<T>? = mDataList

    fun addData(data: T) {
        if (mDataList != null) {
            this.mDataList!!.add(data)
            notifyDataSetChanged()
        } else {
            throw IllegalStateException("data list has not been initial")
        }
    }

    fun addDataAtPosition(position: Int, data: T) {
        if (mDataList != null) {
            this.mDataList!!.add(position, data)
            notifyDataSetChanged()
        } else {
            throw IllegalStateException("data list has not been initial")
        }
    }

    fun addDataList(dataList: ArrayList<T>) {
        if (mDataList != null) {
            this.mDataList!!.addAll(dataList)
            notifyDataSetChanged()
        } else {
            throw IllegalStateException("data list has not been initial")
        }
    }

    fun removeDataAtPosition(position: Int) {
        if (mDataList != null) {
            this.mDataList!!.removeAt(position)
            notifyDataSetChanged()
        } else {
            throw IllegalStateException("data list has not been initial")
        }
    }

    fun removeData(data: T) {
        if (mDataList != null && data in mDataList!!) {
            mDataList!!.remove(data)
            notifyDataSetChanged()
        } else {
            throw IllegalStateException("data not in data list and check it before remove")
        }
    }

    fun clearData() {
        if (mDataList != null) {
            mDataList!!.clear()
            notifyDataSetChanged()
        } else {
            throw IllegalStateException("data list has not been initial")
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val lm = recyclerView.layoutManager
        if (lm is GridLayoutManager)
            lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int =
                    if (isHeader(position) || isFooter(position)) lm.spanCount
                    else 1
            }
    }

    override fun onViewAttachedToWindow(holder: BaseRecyclerHolder) {
        super.onViewAttachedToWindow(holder)
        val lp = holder.itemView.layoutParams
        if (lp is StaggeredGridLayoutManager.LayoutParams)
            lp.isFullSpan = isHeader(getRealPosition(holder)) || isFooter(getRealPosition(holder))
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int, view: View)
    }

    class BaseRecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private const val HEADER = 0x00100000
        private const val FOOTER = 0x00200000
    }
}