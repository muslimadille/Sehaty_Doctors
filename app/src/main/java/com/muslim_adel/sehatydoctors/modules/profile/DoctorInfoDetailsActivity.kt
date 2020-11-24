package com.muslim_adel.sehatydoctors.modules.profile

import androidx.appcompat.app. AppCompatActivity
import android.os.Bundle
import android.view.View
import com.muslim_adel.sehatydoctors.R
import com.muslim_adel.sehatydoctors.modules.base.BaseActivity
import com.muslim_adel.sehatydoctors.remote.objects.doctor.SubSpiecialityModel
import kotlinx.android.synthetic.main.activity_doctor_info_details.*

class DoctorInfoDetailsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_info_details)
        getIntents()
    }
    private fun getIntents(){
        when(intent.getIntExtra("key",0)){
            0->{}
            1->{
                about_doc_info_lay.visibility=View.VISIBLE
                about_doc_contacts_lay.visibility=View.GONE
                doc_sp_d_lay.visibility=View.GONE

                if (preferences!!.getString("language","")=="Arabic"){
                    page_title.text=intent.getStringExtra("title")!!
                    info_content.text=intent.getStringExtra("content_ar")!!
                }else{
                    page_title.text=intent.getStringExtra("title")!!
                    info_content.text=intent.getStringExtra("content_en")!!
                }
            }
            2->{
                about_doc_info_lay.visibility=View.GONE
                about_doc_contacts_lay.visibility=View.VISIBLE
                doc_sp_d_lay.visibility=View.GONE

                if (preferences!!.getString("language","")=="Arabic"){
                    page_title.text=intent.getStringExtra("title")!!
                    phone.text=intent.getStringExtra("phone")!!
                    email.text=intent.getStringExtra("email")!!
                    //info_content.text=intent.getStringExtra("lat")!!
                    //info_content.text=intent.getStringExtra("lng")!!
                    address.text=intent.getStringExtra("address_ar")!!

                }else{
                    page_title.text=intent.getStringExtra("title")!!
                    phone.text=intent.getStringExtra("phone")!!
                    email.text=intent.getStringExtra("email")!!
                    //info_content.text=intent.getStringExtra("lat")!!
                    //info_content.text=intent.getStringExtra("lng")!!
                    address.text=intent.getStringExtra("address_en")!!
                }
            }
            3->{
                about_doc_info_lay.visibility=View.GONE
                about_doc_contacts_lay.visibility=View.GONE
                doc_sp_d_lay.visibility=View.VISIBLE
                if (preferences!!.getString("language","")=="Arabic"){
                    sub_spiciality.text= intent.getParcelableArrayListExtra<SubSpiecialityModel>("sub_sp")?.get(0)?.name_ar?:""
                    //phone.text=intent.getStringExtra("phone")!!
                    //email.text=intent.getStringExtra("email")!!
                    //info_content.text=intent.getStringExtra("lat")!!
                    //info_content.text=intent.getStringExtra("lng")!!
                    //address.text=intent.getStringExtra("address_ar")!!

                }else{
                    sub_spiciality.text= intent.getParcelableArrayListExtra<SubSpiecialityModel>("sub_sp")?.get(0)?.name_en?:""
                    // page_title.text=intent.getStringExtra("title")!!
                   // phone.text=intent.getStringExtra("phone")!!
                   // email.text=intent.getStringExtra("email")!!
                    //info_content.text=intent.getStringExtra("lat")!!
                    //info_content.text=intent.getStringExtra("lng")!!
                   // address.text=intent.getStringExtra("address_en")!!
                }


            }
            
        }


    }
}