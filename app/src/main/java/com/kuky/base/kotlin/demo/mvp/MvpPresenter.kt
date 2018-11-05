package com.kuky.base.kotlin.demo.mvp

import com.kuky.base.android.kotlin.basemvp.BaseMvpPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @author kuky.
 * @description
 */
class MvpPresenter(mView: IMvpView) : IMvpPresenter, BaseMvpPresenter<IMvpView>(mView) {
    private val mModel = MvpModel()
    private var mCountDisposable: Disposable? = null

    override fun loadResultToUi() {
        mView!!.showLoading()

        mCountDisposable =
                Observable.timer(500, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        mView!!.setResult(mModel.getChangeList())
                        mView!!.dismissLoading()
                    }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mCountDisposable != null) {
            mCountDisposable!!.dispose()
            mCountDisposable = null
        }
    }
}