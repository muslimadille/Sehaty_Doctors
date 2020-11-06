package com.muslim_adel.sehatydoctors.modules.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


open class BasePagerAddapter (fm: FragmentActivity, val fragment: List<Fragment>): FragmentStateAdapter(fm) {
    override fun getItemCount(): Int  = fragment.size
    override fun createFragment(position: Int): Fragment {
        return fragment[position]
    }
}