package com.muslim_adel.enaya_doctor.modules.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
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
import java.util.function.Consumer

class MapsActivity : BaseActivity(), OnMapReadyCallback, LocationListener {
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
    private lateinit var locationCallback2: LocationCallback
    lateinit  var locationManager: LocationManager

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
        locationCallback2 = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                if(p0.lastLocation!=null&&currentLocation==null){
                    currentLocation=p0.lastLocation
                    val mapFragment = supportFragmentManager
                        .findFragmentById(R.id.map) as SupportMapFragment
                    mapFragment.getMapAsync(this@MapsActivity)
                    onObserveSuccess()
                }
            }
        }
        //fetchLocation()
        implementListeners()
        initSpinners()
        regonObserver()
        onHideMapClicked()
        onSelectLocationClicked()
    }
    fun setAdrressData(){

        if(docProfileModel!=null){
            edit_sn_en_txt.setText(docProfileModel!!.address_en)
            edit_sn_txt.setText(docProfileModel!!.address_ar)
            edit_lm_en_txt.setText(docProfileModel!!.landmark_en)
            edit_lm_ar_txt.setText(docProfileModel!!.landmark_ar)
            lat= docProfileModel!!.lat.toString()
            lng=docProfileModel!!.lng.toString()
            if(lat.isNotEmpty()&&lng.isNotEmpty()){
                this.currentLocation= Location("")
                this.currentLocation!!.latitude=lat.toDouble()
                this.currentLocation!!.longitude=lng.toDouble()
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
                onObserveSuccess()
            }else{
                fetchLocation()
            }
        }
        if(labProfileModel!=null){
            edit_sn_en_txt.setText(labProfileModel!!.address_en)
            edit_sn_txt.setText(labProfileModel!!.address_ar)
            edit_lm_en_txt.setText(labProfileModel!!.landmark_en)
            edit_lm_ar_txt.setText(labProfileModel!!.landmark_ar)
            lat= labProfileModel!!.lat.toString()
            lng=labProfileModel!!.lng.toString()
            if(lat.isNotEmpty()&&lng.isNotEmpty()){
                this.currentLocation= Location("")
                this.currentLocation!!.latitude=lat.toDouble()
                this.currentLocation!!.longitude=lng.toDouble()
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
                onObserveSuccess()
            }else{
                fetchLocation()
            }
            onObserveSuccess()
        }
        if(pharmProfileModel!=null){
            edit_sn_en_txt.setText(pharmProfileModel!!.address_en)
            edit_sn_txt.setText(pharmProfileModel!!.address_ar)
            edit_lm_en_txt.setText(pharmProfileModel!!.landmark_en)
            edit_lm_ar_txt.setText(pharmProfileModel!!.landmark_ar)
            lat= pharmProfileModel!!.lat.toString()
            lng=pharmProfileModel!!.lng.toString()
            if(lat.isNotEmpty()&&lng.isNotEmpty()){
                this.currentLocation= Location("")
                this.currentLocation!!.latitude=lat.toDouble()
                this.currentLocation!!.longitude=lng.toDouble()
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
                onObserveSuccess()
            }else{
                fetchLocation()
            }

        }
    }

    var locationRequest: LocationRequest = LocationRequest()
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(locationRequest,
            locationCallback2,
            Looper.getMainLooper())
    }
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient!!.removeLocationUpdates(locationCallback2)
    }

    fun fetchLocation(){
        onObserveStart()
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1000)
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),1000)

            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100L,10f,this@MapsActivity)
        } else {

            startLocationUpdates()

        }

    }





    override fun onMapReady(googleMap: GoogleMap) {


        mMap = googleMap

        val lat_long=LatLng(currentLocation?.latitude!!,currentLocation?.longitude!!)
            drawMarker(lat_long)

        mMap.setOnMarkerDragListener(object :GoogleMap.OnMarkerDragListener{
            override fun onMarkerDragStart(p0: Marker) {
            }

            override fun onMarkerDrag(p0: Marker) {
            }

            override fun onMarkerDragEnd(p0: Marker) {

                if(currentMrker!=null){
                    currentMrker?.remove()
                    var newLatLng=LatLng(p0?.position!!.latitude,p0.position.longitude)
                    drawMarker(newLatLng)

                }
            }
        })

    }
    private fun drawMarker(lat_long:LatLng){
       val markerOption= MarkerOptions().position(lat_long).draggable(true)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(lat_long))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat_long,15f))
        currentMrker=mMap.addMarker(markerOption)
        currentMrker?.showInfoWindow()
        lat=lat_long.latitude.toString()
        lng=lat_long.longitude.toString()

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
    }
    private fun onObserveSuccess() {
        edite_add_progrss_lay?.visibility = View.GONE
    }
    private fun onObservefaled() {
        edite_add_progrss_lay?.visibility = View.GONE
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
                                setAdrressData()
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
                                setAdrressData()
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
                                setAdrressData()

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
                            finish()

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
                            finish()
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
                            finish()
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

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onLocationChanged(p0: Location) {
        if (null != p0&&currentLocation==null) {
            this.currentLocation=p0
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
            onObserveSuccess()
        }
    }
}