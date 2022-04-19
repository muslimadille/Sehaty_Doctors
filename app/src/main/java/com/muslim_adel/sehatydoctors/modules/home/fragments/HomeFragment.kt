package com.seha_khanah_doctors.modules.home.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.base.CustomTabLayout
import com.seha_khanah_doctors.modules.home.MainActivity
import com.seha_khanah_doctors.modules.home.schedual.AllDaysFragment
import com.seha_khanah_doctors.modules.home.schedual.AppointmentsFragment
import com.seha_khanah_doctors.modules.home.schedual.AppointmentsManageFragment
import com.seha_khanah_doctors.modules.home.schedual.TabsAdapter
import com.seha_khanah_doctors.utiles.ComplexPreferences
import com.seha_khanah_doctors.utiles.Q
import kotlinx.android.synthetic.main.home_fragment.*


class HomeFragment : Fragment() {
    var preferences: ComplexPreferences? = null
    val listFragments = ArrayList<Fragment>()
    val appointmentsManageFragment:AppointmentsManageFragment=AppointmentsManageFragment()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.home_fragment, container, false)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preferences = ComplexPreferences.getComplexPreferences(mContext, Q.PREF_FILE, Q.MODE_PRIVATE)
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
        listFragments.add(AllDaysFragment())
        listFragments.add(AppointmentsManageFragment())

    }
    var mContext: MainActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }
    override fun onDetach() {
        super.onDetach()
        mContext = context as MainActivity
    }


}