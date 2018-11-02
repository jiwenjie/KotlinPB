package com.kuky.base.android.kotlin.baseviews

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author kuky.
 * @description fragment 基类，懒加载 fragment 基类同，初始化时候通过 by lazy 进行初始化即可
 */
abstract class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment(savedInstanceState)
        setListener()
        handleRxBus()
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun initFragment(savedInstanceState: Bundle?)

    protected open fun setListener() {}

    protected open fun handleRxBus() {}
}