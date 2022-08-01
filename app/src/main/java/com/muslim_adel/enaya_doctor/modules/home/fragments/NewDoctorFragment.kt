package com.muslim_adel.enaya_doctor.modules.home.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.home.MainActivity
import com.muslim_adel.enaya_doctor.modules.profile.doctor.DoctorEditProfileActivity
import com.muslim_adel.enaya_doctor.remote.apiServices.ApiClient
import com.muslim_adel.enaya_doctor.remote.apiServices.SessionManager
import com.muslim_adel.enaya_doctor.remote.objects.BaseResponce
import com.muslim_adel.enaya_doctor.remote.objects.VisitorsNumber
import com.muslim_adel.enaya_doctor.remote.objects.doctor.DoctorProfileModel
import com.muslim_adel.enaya_doctor.utiles.Q
import kotlinx.android.synthetic.main.fragment_new_doctor.*
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Error

class NewDoctorFragment : Fragment() {
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_doctor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ObserveDoctorProfile()
        onEditProfileClicked()
    }
    var mContext: MainActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }
    private fun visitorsNumObserver(id:Int) {
        if(true){
            var url= Q.DOC_VISITORS_API+"$id"
            apiClient = ApiClient()
            sessionManager = SessionManager(mContext!!)
            apiClient.getApiService(mContext!!).fitchDocVisitor(url)
                .enqueue(object : Callback<VisitorsNumber> {
                    override fun onFailure(
                        call: Call<VisitorsNumber>,
                        t: Throwable
                    ) {
                    }

                    override fun onResponse(
                        call: Call<VisitorsNumber>,
                        response: Response<VisitorsNumber>
                    ) {
                        if (response!!.isSuccessful) {
                            if (response.body()!!.success) {
                                onObserveSuccess()
                                response.body()!!.data.let {
                                    if(it!=null){ visitors_num_txt?.text=it.toString()}
                                }

                            } else {
                                Toast.makeText(
                                    mContext!!,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            Toast.makeText(mContext!!, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }


                })
        }else{
        }

    }
    private fun ObserveDoctorProfile(){
        onObserveStart()
        apiClient = ApiClient()
        sessionManager = SessionManager(mContext!!)
        apiClient.getApiService(mContext!!).fitchDoctorProfile()
            .enqueue(object : Callback<BaseResponce<DoctorProfileModel>> {
                override fun onFailure(call: Call<BaseResponce<DoctorProfileModel>>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<BaseResponce<DoctorProfileModel>>,
                    response: Response<BaseResponce<DoctorProfileModel>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            response.body()!!.data!!.let {
                                mContext!!.doctorProfileModel=it
                                onObserveSuccess()
                                setProfileData(it)
                            }
                        } else {
                            onObservefaled()
                        }

                    } else {
                        onObservefaled()
                    }

                }


            })
    }
    private fun onObserveStart() {
        doc_profile_progrss_lay?.visibility = View.VISIBLE
        doc_profil_lay?.visibility = View.GONE
        doc_profile_no_search_lay?.visibility = View.GONE
    }

    private fun onObserveSuccess() {
        doc_profile_progrss_lay?.visibility = View.GONE
        doc_profile_no_search_lay?.visibility = View.GONE

        doc_profil_lay?.visibility = View.VISIBLE
    }

    private fun onObservefaled() {
        doc_profile_no_search_lay?.visibility = View.VISIBLE
        doc_profile_progrss_lay?.visibility = View.GONE
        doc_profil_lay?.visibility = View.GONE
    }
    private  fun setProfileData(profileModel:DoctorProfileModel){
        try {
            var img= view?.findViewById<ImageView>(R.id.doc_profile_img)

            if (mContext!!.preferences!!.getString("language","")=="Arabic"){
                doc_full_name_txt.text="${mContext!!.getString(R.string.full_name)}:${profileModel.firstName_ar} ${profileModel.lastName_ar}"
                doc_email_txt.text=mContext!!.getString(R.string.e_mail)+":"+profileModel.email
                doc_phone_txt.text=mContext!!.getString(R.string.phone_number)+":"+profileModel.phonenumber.toString()
                doc_address_txt.text="${mContext!!.getString(R.string.address)}${profileModel.address_ar}"
                ref_point_txt.text="${mContext!!.getString(R.string.landmark)}:${profileModel.landmark_ar}"
                doc_sp_txt.text="${mContext!!.getString(R.string.speciality)}:${profileModel.sub_specialties[0].name_ar}"
                doc_prof_details.text="${mContext!!.getString(R.string.professional_title_english)}${profileModel.profissionalTitle_ar}"
                about_doc.text="${mContext!!.getString(R.string.about_doctor)}:${profileModel.aboutDoctor_ar}"

            }else{
                doc_full_name_txt.text="${mContext!!.getString(R.string.full_name)}:${profileModel.firstName_en} ${profileModel.lastName_en}"
                doc_email_txt.text=mContext!!.getString(R.string.e_mail)+":"+profileModel.email
                doc_phone_txt.text=mContext!!.getString(R.string.phone_number)+":"+profileModel.phonenumber.toString()
                doc_address_txt.text="${mContext!!.getString(R.string.address)}${profileModel.address_en}"
                ref_point_txt.text="${mContext!!.getString(R.string.landmark)}:${profileModel.landmark_en}"
                doc_sp_txt.text="${mContext!!.getString(R.string.speciality)}:${profileModel.sub_specialties[0].name_en}"
                doc_prof_details.text="${mContext!!.getString(R.string.position)}${profileModel.profissionalTitle_en}"
                about_doc.text="${mContext!!.getString(R.string.about_doctor)}:${profileModel.aboutDoctor_en}"

            }
            if(profileModel.featured!=""||profileModel.featured!=null){doctor_img?.setPadding(1,1,1,1)}
            img.let {
                if (img != null) {
                    Glide.with(mContext!!).applyDefaultRequestOptions(
                        RequestOptions()
                            .placeholder(R.drawable.ic_camera_24)
                            .error(R.drawable.ic_camera_24))
                        .load(if (profileModel.featured!=null)profileModel.featured else "")
                        .centerCrop()
                        .into(img)
                }
            }

            visitorsNumObserver(profileModel.id.toInt())
        }catch (error: Error){

        }

    }
    private fun onEditProfileClicked(){
        edit_profile_btn.setOnClickListener {
            mContext!!.intent= Intent(mContext, DoctorEditProfileActivity::class.java)
            mContext!!.startActivity(mContext!!.intent)
        }
    }

}