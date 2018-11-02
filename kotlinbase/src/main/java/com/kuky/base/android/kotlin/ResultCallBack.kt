package com.kuky.base.android.kotlin

/**
 * @author kuky.
 * @description 网络数据请求回调
 */
interface OnHttpRequestCallBack<T> {
    fun onSuccess(t: T)

    fun onFail(msg: String)
}

interface onListCallBack<T> {
    fun onSucceed(t: T)

    fun onEmpty()

    fun onFailed()
}

interface OnResultBack<T> {
    fun onResult(t: T)
}