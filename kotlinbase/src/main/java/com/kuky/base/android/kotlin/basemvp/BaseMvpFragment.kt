package com.kuky.base.android.kotlin.basemvp

import android.os.Bundle
import android.view.View
import com.kuky.base.android.kotlin.baseviews.BaseFragment

/**
 * @author kuky.
 * @description
 */
abstract class BaseMvpFragment<V : BaseMvpViewImpl, P : BaseMvpPresenter<V>> : BaseFragment() {

    protected lateinit var mPresenter: P

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = initPresenter()
        lifecycle.addObserver(mPresenter)
    }

    abstract fun initPresenter(): P
}