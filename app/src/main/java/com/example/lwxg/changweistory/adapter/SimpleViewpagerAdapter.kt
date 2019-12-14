package com.example.lwxg.changweistory.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SimpleViewpagerAdapter(fm: FragmentManager?, private val fragments: List<Fragment>) : FragmentPagerAdapter(fm!!) {
    override fun getItem(p0: Int): Fragment {
        return fragments[p0]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    companion object {

        fun instance(fm: FragmentManager?, fragments: List<Fragment>): SimpleViewpagerAdapter {
            return SimpleViewpagerAdapter(fm, fragments)
        }

    }


}