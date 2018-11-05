package com.kuky.base.kotlin.demo.vp

import android.os.Bundle
import com.kuky.base.android.kotlin.baseadapters.BaseFragmentPagerAdapter
import com.kuky.base.android.kotlin.baseutils.ScreenUtils
import com.kuky.base.android.kotlin.baseviews.BaseActivity
import com.kuky.base.android.kotlin.baseviews.BaseFragment
import com.kuky.base.kotlin.demo.R
import kotlinx.android.synthetic.main.activity_vp.*

class VpActivity : BaseActivity() {
    /* BaseFragmentPagerAdapter Demo */
    private lateinit var mFragmentAdapter: BaseFragmentPagerAdapter
    private val mFragments = ArrayList<BaseFragment>()
    private val mTitles = arrayOf("头条", "社会")
    private val mKeys = arrayOf("top", "shehui")

    override fun getLayoutId(): Int = R.layout.activity_vp

    override fun initActivity(savedInstanceState: Bundle?) {
        mKeys.forEach {
            val instance: NewsFragment by lazy { NewsFragment.newInstance(it) }
            mFragments.add(instance)
        }
        mFragmentAdapter = BaseFragmentPagerAdapter(supportFragmentManager, mFragments, mTitles)
        container_vp.adapter = mFragmentAdapter
        container_tab.setupWithViewPager(container_vp)
        ScreenUtils.tabIndicatorReflex(container_tab, 40f)
    }
}
