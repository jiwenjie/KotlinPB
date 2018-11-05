package com.kuky.base.kotlin.demo.mvp

import com.kuky.base.android.kotlin.basemvp.BaseMvpViewImpl

/**
 * @author kuky.
 * @description
 */

interface IMvpModel {
    fun getChangeList(): Array<String>
}

interface IMvpView : BaseMvpViewImpl {
    fun showLoading()
    fun dismissLoading()
    fun setResult(result: Array<String>)
}

interface IMvpPresenter {
    fun loadResultToUi()
}

