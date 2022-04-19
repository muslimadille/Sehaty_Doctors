package com.seha_khanah_doctors.modules.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.get
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.base.BaseActivity
import com.seha_khanah_doctors.modules.home.fragments.ExstarsFragment
import com.seha_khanah_doctors.modules.home.fragments.HomeFragment
import com.seha_khanah_doctors.modules.home.fragments.OffersFragment
import com.seha_khanah_doctors.modules.home.fragments.ProfileFragment
import com.seha_khanah_doctors.modules.profile.labs.LabProfileFragment
import com.seha_khanah_doctors.modules.profile.pharmacy.PharmacyProfileFragment
import com.seha_khanah_doctors.remote.objects.doctor.DoctorProfileModel
import com.seha_khanah_doctors.utiles.Q
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    var doctorProfileModel: DoctorProfileModel? =null
    var navK=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navK=intent.let { it.getIntExtra("key",0) }
        if(preferences!!.getString(Q.USER_TYPE,"")==Q.USER_DOCTOR){
            setContentView(R.layout.activity_main)
            if (savedInstanceState == null) {
                val fragment = HomeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                    .commit()
            }
            initBottomNavigation()
        }
        else if (preferences!!.getString(Q.USER_TYPE,"")==Q.USER_PHARM){
            setContentView(R.layout.activity_main2)
            if (savedInstanceState == null) {
                val fragment = HomeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                    .commit()
            }
            initPharmacyBottomNavigation()
        }
        else if (preferences!!.getString(Q.USER_TYPE,"")==Q.USER_LAB){
            setContentView(R.layout.activity_main3)
            if (savedInstanceState == null) {
                val fragment = HomeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                    .commit()
            }
            initLabBottomNavigation()        }


    }
    private fun initBottomNavigation(){

        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {

                    val fragment = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_offers -> {
                    val fragment = ProfileFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_appointment -> {

                    val fragment =
                        OffersFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_extras->{
                    val fragment = ExstarsFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
        bottomNavigationView.labelVisibilityMode= LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        when (navK) {
            0 -> {
                bottomNavigationView.selectedItemId = R.id.navigation_offers
            }
            1 -> {
                bottomNavigationView.selectedItemId = R.id.navigation_home
            }
            2 -> {
                bottomNavigationView.selectedItemId = R.id.navigation_appointment
            }
            3 -> {
                bottomNavigationView.selectedItemId = R.id.navigation_extras
            }
        }
    }
    private fun initLabBottomNavigation(){

        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {

                    val fragment = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_offers -> {
                    val fragment = LabProfileFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_extras->{
                    val fragment = ExstarsFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        when (navK) {
            0 -> {
                bottomNavigationView.selectedItemId = R.id.navigation_home
            }
            1 -> {
                bottomNavigationView.selectedItemId = R.id.navigation_offers
            }

            2 -> {
                bottomNavigationView.selectedItemId = R.id.navigation_extras
            }
        }
    }
    private fun initPharmacyBottomNavigation(){

        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_offers -> {
                    val fragment = PharmacyProfileFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_appointment -> {
                    val fragment =
                        OffersFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_extras->{
                    val fragment = ExstarsFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        when (navK) {

            0 -> {
                bottomNavigationView.selectedItemId = R.id.navigation_offers
            }
            1 -> {
                bottomNavigationView.selectedItemId = R.id.navigation_appointment
            }
            2 -> {
                bottomNavigationView.selectedItemId = R.id.navigation_extras
            }
        }
    }

}