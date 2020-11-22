package com.muslim_adel.sehatydoctors.modules.home.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.muslim_adel.sehatydoctors.R
import com.muslim_adel.sehatydoctors.modules.base.CustomTabLayout
import com.muslim_adel.sehatydoctors.modules.home.MainActivity
import com.muslim_adel.sehatydoctors.modules.home.schedual.AppointmentsFragment
import com.muslim_adel.sehatydoctors.modules.home.schedual.AppointmentsManageFragment
import com.muslim_adel.sehatydoctors.modules.home.schedual.TabsAdapter
import com.muslim_adel.sehatydoctors.modules.profile.ClinicInfoFragment
import com.muslim_adel.sehatydoctors.modules.profile.DoctorInfoFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.home_fragment.viewPager


class ProfileFragment : Fragment() {
    val listFragments = ArrayList<Fragment>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addFragment()
        setupViewPager()
    }
    
    var mContext: MainActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity as MainActivity
    }
    
    fun setupViewPager() {
        val adapter = TabsAdapter(context as FragmentActivity, listFragments)
        doc_profile_viewPager.adapter = adapter

        val tabLayout = requireView().findViewById<TabLayout>(R.id.doc_profile_tabLayout)

        TabLayoutMediator(doc_profile_tabLayout, doc_profile_viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text =getString(R.string.doc_info)

                }
                1 -> {
                    tab.text = getString(R.string.clinic_info)

                }

            }
            doc_profile_viewPager.setCurrentItem(tab.position, true)
            val customTabLayout = CustomTabLayout(context as FragmentActivity)
            customTabLayout.setTitle(tab.text.toString())
            tab.customView = customTabLayout

        }.attach()

    }

    fun addFragment() {
        listFragments.add(DoctorInfoFragment())
        listFragments.add(ClinicInfoFragment())

    }
}