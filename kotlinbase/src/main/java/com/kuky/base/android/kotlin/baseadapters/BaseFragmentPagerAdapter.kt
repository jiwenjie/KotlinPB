package com.kuky.base.android.kotlin.baseadapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * @author kuky.
 * @description
 */
class BaseFragmentPagerAdapter(fm: FragmentManager, fragments: ArrayList<Fragment>, titles: Array<String>)
    : FragmentPagerAdapter(fm) {

    private var mFragments = fragments
    private var mTitles = titles

    override fun getItem(position: Int): Fragment = mFragments[position]

    override fun getCount(): Int = mTitles.size

    override fun getPageTitle(position: Int): CharSequence? =
        if (mTitles.isEmpty()) super.getPageTitle(position) else mTitles[position]
}