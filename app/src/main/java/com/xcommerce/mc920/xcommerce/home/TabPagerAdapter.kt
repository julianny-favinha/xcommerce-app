package com.xcommerce.mc920.xcommerce.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.xcommerce.mc920.xcommerce.categories.CategoriesFragment
import com.xcommerce.mc920.xcommerce.home.highlights.HighlightsFragment

class TabPagerAdapter(fm: FragmentManager, private var tabCount: Int) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> HighlightsFragment()
            1 -> CategoriesFragment()
            else -> null
        }
    }

    override fun getCount(): Int {
        return tabCount
    }
}