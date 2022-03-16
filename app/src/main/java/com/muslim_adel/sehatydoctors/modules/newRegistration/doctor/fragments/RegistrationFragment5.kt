package com.muslim_adel.sehatydoctors.modules.newRegistration.doctor.fragments

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.muslim_adel.sehatydoctors.modules.newRegistration.doctor.DoctorRegistrationScreen
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.remote.apiServices.ApiClient
import com.seha_khanah_doctors.remote.apiServices.SessionManager
import com.seha_khanah_doctors.remote.objects.BaseResponce
import com.seha_khanah_doctors.remote.objects.Reagons
import com.seha_khanah_doctors.utiles.SpinnerAdapterCustomFont
import kotlinx.android.synthetic.main.fragment_registration3.*
import kotlinx.android.synthetic.main.fragment_registration3.edit_about_doc_ar_txt
import kotlinx.android.synthetic.main.fragment_registration3.edit_about_doc_en_txt
import kotlinx.android.synthetic.main.fragment_registration5.*
import kotlinx.android.synthetic.main.fragment_registration5.edit_lm_ar_txt
import kotlinx.android.synthetic.main.fragment_registration5.edit_lm_en_txt
import kotlinx.android.synthetic.main.fragment_registration5.edit_sn_en_txt
import kotlinx.android.synthetic.main.fragment_registration5.edit_sn_txt
import kotlinx.android.synthetic.main.fragment_registration5.hid_map_btn
import kotlinx.android.synthetic.main.fragment_registration5.map_lay
import kotlinx.android.synthetic.main.fragment_registration5.re_regions_spinner
import kotlinx.android.synthetic.main.fragment_registration5.select_location
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class RegistrationFragment5 : Fragment() {

    var mContext: DoctorRegistrationScreen?=null
    var vlaidationText=""
    private var regionsList = ArrayList<Reagons>()
    private var regionsNameList = ArrayList<String>()
    private lateinit var regionsSpinnerAdapter: SpinnerAdapterCustomFont
    private var selectedRegionId=0
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
        return inflater.inflate(R.layout.fragment_registration5, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //fetchLocation()
        initSpinner()
        regonObserver()
        onSelectLocationClicked()
        onHideMapClicked()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as DoctorRegistrationScreen
    }
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity as DoctorRegistrationScreen
    }
    fun validateInputData(){
        mContext!!.doctorRegistrationModel!!.address_ar=edit_sn_txt.text.toString()
        mContext!!.doctorRegistrationModel!!.address_en=edit_sn_en_txt.text.toString()
        mContext!!.doctorRegistrationModel!!.landmark_ar=edit_lm_ar_txt.text.toString()
        mContext!!.doctorRegistrationModel!!.landmark_en=edit_lm_en_txt.text.toString()
        mContext!!.doctorRegistrationModel!!.lat=mContext!!.lat
        mContext!!.doctorRegistrationModel!!.lng=mContext!!.lng
        mContext!!.register()

    }
    fun checkValidation():Boolean{
        vlaidationText=""
        var value=true
        if(this.edit_sn_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل العنوان(عربي)"+"\n"

        }
        if(this.edit_about_doc_en_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل العنوان (english)"+"\n"

        }
        if(this.edit_lm_ar_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل نقطة دالة(عربي)"+"\n"

        }
        if(this.edit_about_doc_en_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل نقطة دالة(english)"+"\n"

        }


        if(!value){
            Toast.makeText(
                mContext,
                vlaidationText,
                Toast.LENGTH_SHORT
            ).show()
        }
        return value
    }
    private fun onObserveStart() {
        frag5_progrss_lay?.visibility = View.VISIBLE
    }
    private fun onObserveSuccess() {
        frag5_progrss_lay?.visibility = View.GONE
    }
    private fun onObservefaled() {
        frag5_progrss_lay?.visibility = View.GONE
        Toast.makeText(mContext!!, "failed", Toast.LENGTH_SHORT).show()
    }

    private fun regonObserver(){
        onObserveStart()
        apiClient = ApiClient()
        sessionManager = SessionManager(mContext!!)
        apiClient.getApiService(mContext!!).fitchReagonsList()
            .enqueue(object : Callback<BaseResponce<List<Reagons>>> {
                override fun onFailure(call: Call<BaseResponce<List<Reagons>>>, t: Throwable) {
                    mContext!!.alertNetwork(true)
                }
                override fun onResponse(call: Call<BaseResponce<List<Reagons>>>, response: Response<BaseResponce<List<Reagons>>>) {
                    if(response!!.isSuccessful){
                        response.body()!!.data.let {
                            regionsList.addAll(it!!)
                            for (i in 0 until it.size ){
                                if (mContext!!.preferences!!.getString("language","")=="Arabic"){
                                    regionsNameList.add(it[i].area_ar)
                                }else{
                                    regionsNameList.add(it[i].area_en)
                                }
                            }

                            regionsSpinnerAdapter!!.notifyDataSetChanged()
                            onObserveSuccess()
                        }
                    }else{
                        onObservefaled()
                    }

                }

            })
    }

    fun initSpinner(){
        regionsSpinnerAdapter = SpinnerAdapterCustomFont(mContext!!, android.R.layout.simple_spinner_item, regionsNameList)
        regionsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        regionsSpinnerAdapter.textSize = 12
        re_regions_spinner.adapter = regionsSpinnerAdapter
        regionsSpinnerAdapter.notifyDataSetChanged()
        re_regions_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if(regionsList.isNotEmpty()){
                    selectedRegionId=regionsList[position].id
                    mContext!!.doctorRegistrationModel!!.area_id=regionsList[position].id.toString()
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }
    private fun onSelectLocationClicked(){
        select_location.setOnClickListener {
            map_lay.visibility=View.VISIBLE
        }
    }
    private fun onHideMapClicked(){
        hid_map_btn.setOnClickListener {
            map_lay.visibility=View.GONE
        }
    }
    fun fetchLocation(){
        if(ActivityCompat.checkSelfPermission(mContext!!,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(mContext!!,android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(mContext!!, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1000)
            return
        }
        val task=mContext!!.fusedLocationProviderClient?.lastLocation
        task?.addOnSuccessListener { location->
            if(location!=null){
                mContext!!.currentLocation=location
                val mapFragment = mContext!!.supportFragmentManager
                    .findFragmentById(R.id.doc_reg_map) as SupportMapFragment
                mapFragment.getMapAsync(mContext!!)
            }
        }
    }


}