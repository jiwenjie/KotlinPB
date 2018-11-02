package com.kuky.base.android.kotlin.basemvp

import android.os.Bundle
import com.kuky.base.android.kotlin.baseviews.BaseActivity

/**
 * @author kuky.
 * @description
 */
abstract class BaseMvpActivity<V : BaseMvpViewImpl, P : BaseMvpPresenter<V>> : BaseActivity() {

    protected lateinit var mPresenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        mPresenter = initPresenter()
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(mPresenter)
    }

    abstract fun initPresenter(): P
}