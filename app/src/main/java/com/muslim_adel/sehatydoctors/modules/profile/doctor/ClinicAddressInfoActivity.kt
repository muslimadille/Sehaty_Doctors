package com.seha_khanah_doctors.modules.profile.doctor

import android.os.BaseBundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.base.BaseActivity
import com.seha_khanah_doctors.remote.apiServices.ApiClient
import com.seha_khanah_doctors.remote.apiServices.SessionManager

class ClinicAddressInfoActivity : BaseActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinic_address_info)
    }
}