package com.kuky.base.kotlin.demo.vp

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kuky.base.android.kotlin.RetrofitManager
import com.kuky.base.android.kotlin.baseadapters.BaseRecyclerAdapter
import com.kuky.base.android.kotlin.baseutils.LogUtils
import com.kuky.base.android.kotlin.baseutils.ToastUtils
import com.kuky.base.android.kotlin.baseviews.BaseFragment
import com.kuky.base.kotlin.demo.Constant
import com.kuky.base.kotlin.demo.R
import com.kuky.base.kotlin.demo.entity.NewsData
import com.kuky.base.kotlin.demo.networks.RetrofitApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.recycler_news_item.view.*

/**
 * @author kuky.
 * @description
 */
/* BaseFragment Demo*/
class NewsFragment : BaseFragment() {
    private var mType: String? = null
    private var mNewsDisposable: Disposable? = null
    private lateinit var mAdapter: BaseRecyclerAdapter<NewsData>

    override fun getLayoutId(): Int = R.layout.fragment_news

    override fun initFragment(savedInstanceState: Bundle?) {
        mType = arguments?.getString(TYPE)

        /* BaseRecyclerAdapter Demo */
        mAdapter = object : BaseRecyclerAdapter<NewsData>(activity!!) {
            override fun getAdapterLayoutId(): Int = R.layout.recycler_news_item

            override fun convertView(itemView: View, t: NewsData, position: Int) {
                itemView.news_title.text = t.title
                itemView.news_public_date.text = t.date
                itemView.news_author_name.text = t.author_name

                if (position == mSelectedPosition) {
                    itemView.news_author_name.setTextColor(Color.RED)
                } else {
                    itemView.news_author_name.setTextColor(Color.GREEN)
                }

                if (!TextUtils.isEmpty(t.thumbnail_pic_s))
                    Glide.with(activity!!)
                        .applyDefaultRequestOptions(RequestOptions().centerCrop().placeholder(R.mipmap.ic_launcher))
                        .load(t.thumbnail_pic_s)
                        .into(itemView.thumbnail_pic_1)

                if (!TextUtils.isEmpty(t.thumbnail_pic_s02))
                    Glide.with(activity!!)
                        .applyDefaultRequestOptions(RequestOptions().centerCrop().placeholder(R.mipmap.ic_launcher))
                        .load(t.thumbnail_pic_s)
                        .into(itemView.thumbnail_pic_2)

                if (!TextUtils.isEmpty(t.thumbnail_pic_s03))
                    Glide.with(activity!!)
                        .applyDefaultRequestOptions(RequestOptions().centerCrop().placeholder(R.mipmap.ic_launcher))
                        .load(t.thumbnail_pic_s)
                        .into(itemView.thumbnail_pic_3)
            }
        }
        mAdapter.updateSelectItem(0)

        news_list.layoutManager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
        news_list.adapter = mAdapter
        news_refresh.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent,
            R.color.colorDracula
        )

        if (!TextUtils.isEmpty(mType)) requestNews(mType!!)
    }

    private fun requestNews(type: String, refreshable: Boolean = false) {
        news_refresh.isRefreshing = true
        mNewsDisposable = RetrofitManager.mRetrofit
            .create(RetrofitApi::class.java)
            .getNews(type, Constant.NEWS_APP_KEY)
            .map { t -> t.result.data }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t ->
                if (!t.isEmpty()) {
                    mAdapter.updateAdapterData(t as ArrayList<NewsData>)
                    if (refreshable) ToastUtils.showToast(activity!!, "更新数据成功")
                    news_refresh.isRefreshing = false
                }
            }, { t -> LogUtils.e(t.message) })
    }

    override fun setListener() {
        news_refresh.setOnRefreshListener { requestNews(mType!!, true) }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mNewsDisposable != null) {
            mNewsDisposable!!.dispose()
            mNewsDisposable = null
        }
    }

    companion object {
        private const val TYPE = "type"

        @JvmStatic
        fun newInstance(type: String): NewsFragment {
            return NewsFragment().apply {
                arguments = Bundle().apply {
                    putString(TYPE, type)
                }
            }
        }
    }
}