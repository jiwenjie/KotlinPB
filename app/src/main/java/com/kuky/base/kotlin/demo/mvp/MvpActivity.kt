package com.kuky.base.kotlin.demo.mvp

import android.os.Bundle
import android.view.View
import com.kuky.base.android.kotlin.basemvp.BaseMvpActivity
import com.kuky.base.kotlin.demo.R
import kotlinx.android.synthetic.main.activity_mvp.*
import kotlin.random.Random

/* MvpActivity Demo */
class MvpActivity : BaseMvpActivity<IMvpView, MvpPresenter>(), IMvpView {

    override fun initPresenter(): MvpPresenter = MvpPresenter(this@MvpActivity)

    override fun getLayoutId(): Int = R.layout.activity_mvp

    override fun initActivity(savedInstanceState: Bundle?) {
        mvp_tool_bar.title = this::class.java.simpleName
        setSupportActionBar(mvp_tool_bar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mPresenter.loadResultToUi()
    }

    override fun setListener() {
        mvp_result_text.setOnClickListener { mPresenter.loadResultToUi() }

        mvp_tool_bar.setNavigationOnClickListener { finish() }
    }

    override fun showLoading() {
        mvp_loading_progress.visibility = View.VISIBLE
        mvp_result_text.visibility = View.GONE
    }

    override fun dismissLoading() {
        mvp_loading_progress.visibility = View.GONE
        mvp_result_text.visibility = View.VISIBLE
    }

    override fun setResult(result: Array<String>) {
        val random = Random(System.currentTimeMillis()).nextInt(result.size)
        mvp_result_text.text = result[random]
    }
}
