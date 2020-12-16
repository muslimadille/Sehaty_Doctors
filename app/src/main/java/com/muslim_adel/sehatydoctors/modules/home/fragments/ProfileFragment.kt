package com.seha_khanah_doctors.modules.home.fragments

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.base.CustomTabLayout
import com.seha_khanah_doctors.modules.home.MainActivity
import com.seha_khanah_doctors.modules.home.schedual.TabsAdapter
import com.seha_khanah_doctors.modules.profile.doctor.ClinicInfoFragment
import com.seha_khanah_doctors.modules.profile.doctor.DoctorEditProfileActivity
import com.seha_khanah_doctors.modules.profile.doctor.DoctorInfoFragment
import com.seha_khanah_doctors.remote.apiServices.ApiClient
import com.seha_khanah_doctors.remote.apiServices.SessionManager
import com.seha_khanah_doctors.remote.objects.BaseResponce
import com.seha_khanah_doctors.remote.objects.doctor.DoctorProfileModel
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Error


class ProfileFragment : Fragment() {
    val listFragments = ArrayList<Fragment>()
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient


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
        ObserveDoctorProfile()
        addFragment()
        setupViewPager()
        onEditProfileClicked()
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
    private  fun setProfileData(profileModel:DoctorProfileModel){
        var img= view?.findViewById<ImageView>(R.id.doctor_img)
        if (mContext!!.preferences!!.getString("language","")=="Arabic"){
            doctor_name_txt?.text= "${ profileModel.firstName_ar } ${profileModel.lastName_ar}"
            doctor_spieciality_txt?.text=profileModel.profissionalTitle_ar
        }else{
            doctor_name_txt.text= "${ profileModel.firstName_en } ${profileModel.lastName_en}"
            doctor_spieciality_txt.text=profileModel.profissionalTitle_en
        }
        if(profileModel.featured!=""||profileModel.featured!=null){doctor_img?.setPadding(1,1,1,1)}
        Glide.with(mContext!!).applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.ic_camera_24)
                .error(R.drawable.ic_camera_24))
            .load(if (profileModel.featured!=null)profileModel.featured else "")
            .centerCrop()
            .into(img!!)


    }
    private fun ObserveDoctorProfile(){
        onObserveStart()
        apiClient = ApiClient()
        sessionManager = SessionManager(mContext!!)
        apiClient.getApiService(mContext!!).fitchDoctorProfile()
            .enqueue(object : Callback<BaseResponce<DoctorProfileModel>> {
                override fun onFailure(call: Call<BaseResponce<DoctorProfileModel>>, t: Throwable) {
                    alertNetwork(false)
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
    open fun alertNetwork(isExit: Boolean = false) {
        val alertBuilder = AlertDialog.Builder(mContext!!)
        //alertBuilder.setTitle(R.string.error)
        alertBuilder.setMessage(R.string.no_internet)
        if (isExit) {
            alertBuilder.setPositiveButton(R.string.exit) { dialog: DialogInterface, _: Int -> mContext!!.finish() }
        } else {
            alertBuilder.setPositiveButton(R.string.dismiss) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        }
        try {
            if(!mContext!!.isFinishing){
                alertBuilder.show()
            }

        }catch (e: Error){}
    }
    private fun onEditProfileClicked(){
        edit_doc_profile_btn.setOnClickListener {
            mContext!!.intent= Intent(mContext,DoctorEditProfileActivity::class.java)
            mContext!!.startActivity(mContext!!.intent)
        }
    }
}