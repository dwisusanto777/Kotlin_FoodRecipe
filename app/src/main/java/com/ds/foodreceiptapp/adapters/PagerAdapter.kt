package com.ds.foodreceiptapp.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(
    private val resultBundle: Bundle,
    private val fragments: ArrayList<Fragment>,
    fragmentActivity: FragmentActivity
): FragmentStateAdapter(fragmentActivity){
//    override fun getCount(): Int {
//        return fragments.size
//    }
//
//    override fun getItem(position: Int): Fragment {
//        fragments[position].arguments = resultBundle
//        return fragments[position]
//    }
//
//    override fun getPageTitle(position: Int): CharSequence? {
//        return title[position]
//    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        fragments[position].arguments = resultBundle
        return fragments[position]
    }
}