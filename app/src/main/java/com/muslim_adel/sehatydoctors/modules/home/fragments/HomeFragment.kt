package com.muslim_adel.sehatydoctors.modules.home.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.muslim_adel.sehatydoctors.R
import com.muslim_adel.sehatydoctors.modules.base.CustomTabLayout
import com.muslim_adel.sehatydoctors.modules.home.schedual.AppointmentsFragment
import com.muslim_adel.sehatydoctors.modules.home.schedual.AppointmentsManageFragment
import com.muslim_adel.sehatydoctors.modules.home.schedual.TabsAdapter
import com.muslim_adel.sehatydoctors.utiles.ComplexPreferences
import com.muslim_adel.sehatydoctors.utiles.Q
import kotlinx.android.synthetic.main.home_fragment.*


class HomeFragment : Fragment() {
    var preferences: ComplexPreferences? = null
    val listFragments = ArrayList<Fragment>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.home_fragment, container, false)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preferences = ComplexPreferences.getComplexPreferences(context as FragmentActivity, Q.PREF_FILE, Q.MODE_PRIVATE)
        addFragment()
        setupViewPager()

    }
    fun setupViewPager() {
        val adapter = TabsAdapter(context as FragmentActivity, listFragments)
        viewPager.adapter = adapter

        val tabLayout = requireView().findViewById<TabLayout>(R.id.tabLayout)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text =getString(R.string.appointments)

                }
                1 -> {
                    tab.text = getString(R.string.appointments_manage)

                }

            }
            viewPager.setCurrentItem(tab.position, true)
            val customTabLayout = CustomTabLayout(context as FragmentActivity)
            customTabLayout.setTitle(tab.text.toString())
            tab.customView = customTabLayout

        }.attach()

    }

    fun addFragment() {
        listFragments.add(AppointmentsFragment())
        listFragments.add(AppointmentsManageFragment())

    }


}