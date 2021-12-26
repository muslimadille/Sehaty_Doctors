package com.muslim_adel.sehatydoctors.modules.newRegistration.doctor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.muslim_adel.sehatydoctors.modules.newRegistration.doctor.fragments.RegistrationDataFragment
import com.muslim_adel.sehatydoctors.remote.objects.doctor.DoctorRigistrationValidator
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.base.BaseActivity
import com.seha_khanah_doctors.modules.introwizerd.adapters.IntroPagerAdapter
import com.seha_khanah_doctors.modules.introwizerd.fragments.Intro1Fragment
import com.seha_khanah_doctors.modules.introwizerd.fragments.Intro2Fragment
import com.seha_khanah_doctors.modules.introwizerd.fragments.Intro3Fragment
import kotlinx.android.synthetic.main.activity_doctor_registration_screen.*
import kotlinx.android.synthetic.main.activity_intro_wizard.*

class DoctorRegistrationScreen : BaseActivity() {
    var doctorRegistrationModel: DoctorRigistrationValidator? = null
    private val fragmentList = ArrayList<Fragment>()
    private val  adapter=IntroPagerAdapter(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_registration_screen)
        initRegistrationModel()
        onNextClicked()
        initPagerAdapter()
    }

    private fun initRegistrationModel() {
        doctorRegistrationModel = DoctorRigistrationValidator(
            "", "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ArrayList<String>(),
            "",
            "",
            "",
            "",
            "",
            "",
            "", "", "", "", "", "", "", "", "",
            "", "", "",
        )
    }
    private fun initPagerAdapter() {
        doctor_registration_pager.adapter = adapter
        fragmentList.addAll(listOf(
            RegistrationDataFragment(), Intro2Fragment(), Intro3Fragment()
        ))
        adapter.setFragmentList(fragmentList)
    }
    private fun onNextClicked(){
        doctor_registration_next_btn.setOnClickListener {
            if (doctor_registration_pager.getCurrentItem() <(fragmentList.size-1)) {
                doctor_registration_pager.currentItem = doctor_registration_pager.currentItem +1
            }  else {
                doctor_registration_next_btn.text="register"
            }
        }
    }

}