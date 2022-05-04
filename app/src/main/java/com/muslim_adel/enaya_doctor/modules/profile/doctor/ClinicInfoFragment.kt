package com.muslim_adel.enaya_doctor.modules.profile.doctor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.home.MainActivity
import com.muslim_adel.enaya_doctor.modules.map.EditLocationActivity
import kotlinx.android.synthetic.main.fragment_clinic_info.*
import kotlinx.android.synthetic.main.fragment_doctor_info.*
import kotlinx.android.synthetic.main.fragment_pharmacy_profile.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClinicInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClinicInfoFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clinic_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clinic_price_txt.text=mContext!!.doctorProfileModel!!.price.toString()+" "+mContext!!.getString(R.string.derham)
        if (mContext!!.preferences!!.getString("language","")=="Arabic"){
            clinic_address_txt.text=mContext!!.doctorProfileModel!!.address_ar
        }else{
            clinic_address_txt.text=mContext!!.doctorProfileModel!!.address_en

        }
        onClinicAddressClicked()

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
    private fun onClinicServicesClicked(){
        about_doc_btn.setOnClickListener {
            val intent = Intent(mContext, DoctorInfoDetailsActivity::class.java)
            intent.putExtra("content_ar",mContext!!.doctorProfileModel!!.aboutDoctor_ar)
            intent.putExtra("content_en",mContext!!.doctorProfileModel!!.aboutDoctor_en)
            intent.putExtra("title",mContext!!.getString(R.string.about_doc_title))
            intent.putExtra("key",1)
            mContext!!.startActivity(intent)
        }
    }
    private fun onClinicAddressClicked(){
        clinic_address_btn.setOnClickListener {

            val zoom=40
            var lable=mContext!!.doctorProfileModel!!.aboutDoctor_ar
            mContext!!.intent= Intent(Intent.ACTION_VIEW)
                mContext!!.intent.data= Uri.parse("geo:0,0?z=$zoom&q=${mContext!!.doctorProfileModel!!.lat},${mContext!!.doctorProfileModel!!.lng},$lable")
                if(mContext!!.intent.resolveActivity(mContext!!.packageManager)!=null){
                    mContext!!.startActivity(mContext!!.intent)
                }

        }
    }
}