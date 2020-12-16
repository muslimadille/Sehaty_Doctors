package com.seha_khanah_doctors.modules.introwizerd.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class IntroPagerAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {

    private val fragmentList = ArrayList<Fragment>()
    override fun getItemCount(): Int {
        return fragmentList.size
    }
    override fun createFragment(position: Int): Fragment {
        return fragmentList.get(position)
    }
    fun setFragmentList(list: List<Fragment>) {
        fragmentList.clear()
        fragmentList.addAll(list)
        notifyDataSetChanged()
    }
}