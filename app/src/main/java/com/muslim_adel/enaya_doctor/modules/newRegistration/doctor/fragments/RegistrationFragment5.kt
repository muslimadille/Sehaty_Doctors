package com.muslim_adel.enaya_doctor.modules.newRegistration.doctor.fragments

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
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.muslim_adel.enaya_doctor.modules.newRegistration.doctor.DoctorRegistrationScreen
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.remote.apiServices.ApiClient
import com.muslim_adel.enaya_doctor.remote.apiServices.SessionManager
import com.muslim_adel.enaya_doctor.remote.objects.BaseResponce
import com.muslim_adel.enaya_doctor.remote.objects.Reagons
import com.muslim_adel.enaya_doctor.utiles.SpinnerAdapterCustomFont
import kotlinx.android.synthetic.main.fragment_registration5.*
import kotlinx.android.synthetic.main.fragment_registration5.edit_lm_ar_txt
import kotlinx.android.synthetic.main.fragment_registration5.edit_lm_en_txt
import kotlinx.android.synthetic.main.fragment_registration5.edit_sn_en_txt
import kotlinx.android.synthetic.main.fragment_registration5.edit_sn_txt
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

    //////////////map
    var lat="3.000"
    var lng="2.000"
    private lateinit var mMap: GoogleMap
    var currentLocation: Location?=null
    var currentMrker: Marker?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registration5, container, false)



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
        regonObserver()
        onSelectLocationClicked()
        onHideMapClicked()


    }
    fun setLocationText(text:String){
        edit_location_txt?.let { it.text=text }
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
        if(this.edit_sn_en_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل العنوان (english)"+"\n"

        }
        if(this.edit_lm_ar_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل نقطة دالة(عربي)"+"\n"

        }
        if(this.edit_lm_en_txt.text.toString().isEmpty()){
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
            mContext!!.showMap()
        }
    }
    private fun onHideMapClicked(){
       /* hid_map_btn.setOnClickListener {
            mContext!!.hideMap()
        }*/
    }




}