package com.muslim_adel.enaya_doctor.modules.profile.doctor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.home.MainActivity
import com.muslim_adel.enaya_doctor.modules.profile.doctor.DoctorInfoDetailsActivity
import kotlinx.android.synthetic.main.fragment_doctor_info.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DoctorInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DoctorInfoFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onDocInfoClicked()
        onDocContactsClicked()
        onDocProfDetailsClicked()


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
    private fun onDocInfoClicked(){
        about_doc_btn.setOnClickListener {
            val intent = Intent(mContext, DoctorInfoDetailsActivity::class.java)
            intent.putExtra("content_ar",mContext!!.doctorProfileModel!!.aboutDoctor_ar)
            intent.putExtra("content_en",mContext!!.doctorProfileModel!!.aboutDoctor_en)
            intent.putExtra("title",mContext!!.getString(R.string.about_doc_title))
            intent.putExtra("key",1)
            mContext!!.startActivity(intent)
        }
    }
    private fun onDocContactsClicked(){
        contacts_btn.setOnClickListener {
            val intent = Intent(mContext, DoctorInfoDetailsActivity::class.java)
            intent.putExtra("phone",mContext!!.doctorProfileModel!!.phonenumber.toString())
            intent.putExtra("email",mContext!!.doctorProfileModel!!.email)
            intent.putExtra("lat",mContext!!.doctorProfileModel!!.lat.toString())
            intent.putExtra("lng",mContext!!.doctorProfileModel!!.lng.toString())
            intent.putExtra("title",mContext!!.getString(R.string.doc_contacts_title))
            intent.putExtra("address_ar", "${ mContext!!.doctorProfileModel!!.address_ar}")
            intent.putExtra("address_en", "${ mContext!!.doctorProfileModel!!.address_en}")
            intent.putExtra("key",2)
            mContext!!.startActivity(intent)
        }
    }
    private fun onDocProfDetailsClicked(){
        sp_info_btn.setOnClickListener {
            val intent = Intent(mContext, DoctorInfoDetailsActivity::class.java)
            intent.putExtra("profissionalDetails_id",mContext!!.doctorProfileModel!!.profissionalDetails_id)
            intent.putParcelableArrayListExtra("sub_sp",mContext!!.doctorProfileModel!!.sub_specialties)
            intent.putExtra("profissionalTitle_en",mContext!!.doctorProfileModel!!.profissionalTitle_en)
            intent.putExtra("profissionalTitle_ar",mContext!!.doctorProfileModel!!.profissionalTitle_ar)
            intent.putExtra("practiceLicenseID",  mContext!!.doctorProfileModel!!.practiceLicenseID)
            intent.putExtra("title",mContext!!.getString(R.string.doc_prof_details_title))
            intent.putExtra("key",3)
            mContext!!.startActivity(intent)
        }
    }
}