package com.kuky.base.android.kotlin.basemvp

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.util.Log


/**
 * @author kuky.
 * @description mvp presenter 基类，实现 lifecycle 用于绑定 Activity 和 Fragment 生命周期
 */
abstract class BaseMvpPresenter<V : BaseMvpViewImpl>(view: V) : LifecycleObserver {

    private val tag = BaseMvpPresenter::class.java.simpleName
    protected var mView: V? = view

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected open fun onCreate() {
        Log.d(tag, "onCreate")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected fun onStart() {
        Log.d(tag, "onStart")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected fun onResume() {
        Log.d(tag, "onResume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected fun onPause() {
        Log.d(tag, "onPause")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected fun onStop() {
        Log.d(tag, "onStop")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected fun onDestroy() {
        this.mView = null
        Log.d(tag, "onDestroy")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    protected fun onLifeChange(owner: LifecycleOwner, event: Lifecycle.Event) {
        Log.d(tag, "onLifeChange: ($owner, $event)")
    }
}