package com.muslim_adel.enaya_doctor.modules.map

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.base.BaseActivity
import com.muslim_adel.enaya_doctor.modules.home.fragments.HomeFragment
import com.muslim_adel.enaya_doctor.remote.apiServices.ApiClient
import com.muslim_adel.enaya_doctor.remote.apiServices.SessionManager
import com.muslim_adel.enaya_doctor.remote.objects.*
import com.muslim_adel.enaya_doctor.remote.objects.doctor.DoctorProfileModel
import com.muslim_adel.enaya_doctor.utiles.Q
import com.muslim_adel.enaya_doctor.utiles.SpinnerAdapterCustomFont
import kotlinx.android.synthetic.main.activity_maps.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MapsActivity : BaseActivity(), OnMapReadyCallback {
    var name=""
    var lat="3.000"
    var lng="2.000"

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    var docProfileModel: DoctorProfileModel? =null
    var pharmProfileModel:Pharmacy ? =null
    var labProfileModel: Laboratory? =null
    private lateinit var mMap: GoogleMap
    var fusedLocationProviderClient:FusedLocationProviderClient?=null
    var currentLocation:Location?=null
    var currentMrker:Marker?=null
    var key=0

    private var regionsList = ArrayList<Reagons>()
    private var regionsNameList = ArrayList<String>()

    private lateinit var regionsSpinnerAdapter: SpinnerAdapterCustomFont
    private var selectedRegionId=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        if(preferences!!.getString(Q.USER_TYPE,"")== Q.USER_DOCTOR){
            ObserveDocProfile()
            address_updat_btn.setOnClickListener {
                upDateDoctorProfile()
            }
        }
        else if (preferences!!.getString(Q.USER_TYPE,"")== Q.USER_PHARM){
            ObservePharmProfile()
            address_updat_btn.setOnClickListener {
                upDatepharmProfile()
            }
        }
        else if (preferences!!.getString(Q.USER_TYPE,"")== Q.USER_LAB){
            ObserveLabProfile()
            address_updat_btn.setOnClickListener {
                upDateLabProfile()
            }
              }
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
        implementListeners()
        initSpinners()
        regonObserver()
        onHideMapClicked()
        onSelectLocationClicked()
    }

    private fun fetchLocation(){
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
            &&ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1000)
            return
        }
        val task=fusedLocationProviderClient?.lastLocation
        task?.addOnSuccessListener { location->
            if(location!=null){
                this.currentLocation=location
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            1000->{if(grantResults.size!=0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                fetchLocation()
            }}
        }
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val lat_long=LatLng(currentLocation?.latitude!!,currentLocation?.longitude!!)
        drawMarker(lat_long)
        mMap.setOnMarkerDragListener(object :GoogleMap.OnMarkerDragListener{
            override fun onMarkerDragStart(p0: Marker?) {
            }

            override fun onMarkerDrag(p0: Marker?) {
            }

            override fun onMarkerDragEnd(p0: Marker?) {

                if(currentMrker!=null){
                    currentMrker?.remove()
                    var newLatLng=LatLng(p0?.position!!.latitude,p0.position.longitude)
                    drawMarker(newLatLng)


                }
            }
        })

    }
    private fun drawMarker(lat_long:LatLng){
       val markerOption= MarkerOptions().position(lat_long).snippet(getAddress(lat_long.latitude,lat_long.latitude)).draggable(true)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(lat_long))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat_long,15f))
        currentMrker=mMap.addMarker(markerOption)
        currentMrker?.showInfoWindow()
        edit_location_txt.text=getAddress(lat_long.latitude,lat_long.longitude).toString()
        lat=lat_long.latitude.toString()
        lng=lat_long.longitude.toString()

    }
    private  fun getAddress(lat:Double,lng:Double):String{
       val getCoder= Geocoder(this,Locale.getDefault())
        val address=getCoder.getFromLocation(lat,lng,1)
        return address[0].getAddressLine(0).toString()
    }
    private fun initSpinners(){

        regionsSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, regionsNameList)
        regionsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        regionsSpinnerAdapter.textSize = 12
        regionsSpinnerAdapter.add(getString(R.string.select_reagon))
        regions_spinner.adapter = regionsSpinnerAdapter
        regionsSpinnerAdapter.notifyDataSetChanged()

    }
    private fun implementListeners() {

        regions_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if(position!=0){
                   selectedRegionId=regionsList[position-1].id
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }


    }
    private fun onObserveStart() {
        edite_add_progrss_lay?.visibility = View.VISIBLE
        edit_address_lay?.visibility = View.GONE
    }
    private fun onObserveSuccess() {
        edite_add_progrss_lay?.visibility = View.GONE
        edit_address_lay?.visibility = View.VISIBLE
    }
    private fun onObservefaled() {
        edite_add_progrss_lay?.visibility = View.VISIBLE
        edit_address_lay?.visibility = View.GONE
        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
    }
    private fun regonObserver(){
        onObserveStart()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).fitchReagonsList()
            .enqueue(object : Callback<BaseResponce<List<Reagons>>> {
                override fun onFailure(call: Call<BaseResponce<List<Reagons>>>, t: Throwable) {
                    alertNetwork(true)
                }
                override fun onResponse(call: Call<BaseResponce<List<Reagons>>>, response: Response<BaseResponce<List<Reagons>>>) {
                    if(response!!.isSuccessful){
                        response.body()!!.data.let {
                            regionsList.addAll(it!!)
                            for (i in 0 until it.size ){
                                if (preferences!!.getString("language","")=="Arabic"){
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

    private fun ObserveDocProfile(){
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).fitchDoctorProfile()
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
                                docProfileModel=it
                            }
                        } else {
                        }

                    } else {
                    }

                }


            })
    }
    private fun ObservePharmProfile(){
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).fitchPharmProfile()
            .enqueue(object : Callback<BaseResponce<Pharmacy>> {
                override fun onFailure(call: Call<BaseResponce<Pharmacy>>, t: Throwable) {
                    alertNetwork(false)
                }

                override fun onResponse(
                    call: Call<BaseResponce<Pharmacy>>,
                    response: Response<BaseResponce<Pharmacy>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            response.body()!!.data!!.let {
                                pharmProfileModel=it
                            }
                        } else {
                        }

                    } else {
                    }

                }


            })
    }
    private fun ObserveLabProfile(){
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).fitchLabProfile()
            .enqueue(object : Callback<BaseResponce<Laboratory>> {
                override fun onFailure(call: Call<BaseResponce<Laboratory>>, t: Throwable) {
                    alertNetwork(false)
                }

                override fun onResponse(
                    call: Call<BaseResponce<Laboratory>>,
                    response: Response<BaseResponce<Laboratory>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            response.body()!!.data!!.let {
                                labProfileModel=it
                            }
                        } else {
                        }

                    } else {
                    }

                }


            })
    }
    private fun upDateDoctorProfile(){
        onObserveStart()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).editDocAddress(
            if(edit_sn_en_txt.text.isNotEmpty())edit_sn_en_txt.text.toString() else docProfileModel!!.address_en,
            if(edit_sn_txt.text.isNotEmpty())edit_sn_txt.text.toString() else docProfileModel!!.address_ar,
            if(edit_lm_en_txt.text.isNotEmpty())edit_lm_en_txt.text.toString() else docProfileModel!!.landmark_en,
            if(edit_lm_ar_txt.text.isNotEmpty())edit_lm_ar_txt.text.toString() else docProfileModel!!.landmark_ar,
            if(selectedRegionId!=0)selectedRegionId.toString() else docProfileModel!!.area_id.toString(),
            lng,
            lat
        )
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
                            onObserveSuccess()
                            response.body()!!.data!!.let {
                                docProfileModel=it
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
    private fun upDatepharmProfile(){
        onObserveStart()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).editPharmAddress(
            if(edit_sn_en_txt.text.isNotEmpty())edit_sn_en_txt.text.toString() else pharmProfileModel!!.address_en,
            if(edit_sn_txt.text.isNotEmpty())edit_sn_txt.text.toString() else pharmProfileModel!!.address_ar,
            if(edit_lm_en_txt.text.isNotEmpty())edit_lm_en_txt.text.toString() else pharmProfileModel!!.landmark_en,
            if(edit_lm_ar_txt.text.isNotEmpty())edit_lm_ar_txt.text.toString() else pharmProfileModel!!.landmark_ar,
            if(selectedRegionId!=0)selectedRegionId.toString() else pharmProfileModel!!.area_id.toString(),
            lng,
            lat
        )
            .enqueue(object : Callback<BaseResponce<Pharmacy>> {
                override fun onFailure(call: Call<BaseResponce<Pharmacy>>, t: Throwable) {
                    alertNetwork(false)
                }

                override fun onResponse(
                    call: Call<BaseResponce<Pharmacy>>,
                    response: Response<BaseResponce<Pharmacy>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            onObserveSuccess()
                            response.body()!!.data!!.let {
                                pharmProfileModel=it
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
    private fun upDateLabProfile(){
        onObserveStart()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).editLabAddress(
            if(edit_sn_en_txt.text.isNotEmpty())edit_sn_en_txt.text.toString() else labProfileModel!!.address_en,
            if(edit_sn_txt.text.isNotEmpty())edit_sn_txt.text.toString() else labProfileModel!!.address_ar,
            if(edit_lm_en_txt.text.isNotEmpty())edit_lm_en_txt.text.toString() else labProfileModel!!.landmark_en,
            if(edit_lm_ar_txt.text.isNotEmpty())edit_lm_ar_txt.text.toString() else labProfileModel!!.landmark_ar,
            if(selectedRegionId!=0)selectedRegionId.toString() else labProfileModel!!.area_id.toString(),
            lng,
            lat
        )
            .enqueue(object : Callback<BaseResponce<Laboratory>> {
                override fun onFailure(call: Call<BaseResponce<Laboratory>>, t: Throwable) {
                    alertNetwork(false)
                }

                override fun onResponse(
                    call: Call<BaseResponce<Laboratory>>,
                    response: Response<BaseResponce<Laboratory>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            onObserveSuccess()
                            response.body()!!.data!!.let {
                                labProfileModel=it
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
    private fun onHideMapClicked(){
        hid_map_btn.setOnClickListener {
            map_lay.visibility=View.GONE
        }
    }

    private fun onSelectLocationClicked(){
        select_location_btn.setOnClickListener {
            map_lay.visibility=View.VISIBLE
        }
    }
}