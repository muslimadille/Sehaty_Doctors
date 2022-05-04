package com.muslim_adel.enaya_doctor.modules.registration

import android.content.Intent
import android.os.Bundle
import com.muslim_adel.enaya_doctor.modules.selectCuntry.SelectCountryActivity
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.base.BaseActivity
import kotlinx.android.synthetic.main.activity_select_user.*

class SelectUserActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_user)
        onDoctorClicked()
        onLabClicked()
        onPharmacyClicked()
    }
    fun onDoctorClicked(){
        doctor_btn.setOnClickListener {

            val intent = Intent(this, SelectCountryActivity::class.java)
            intent.putExtra("key",1)
            startActivity(intent)
        }
    }
    fun onLabClicked(){
        lab_btn.setOnClickListener {
            val intent = Intent(this, SelectCountryActivity::class.java)
            intent.putExtra("key",2)
            startActivity(intent)
        }
    }
    fun onPharmacyClicked(){
        pharmacy_btn.setOnClickListener {
            val intent = Intent(this, SelectCountryActivity::class.java)
            intent.putExtra("key",3)
            startActivity(intent)
        }
    }
}