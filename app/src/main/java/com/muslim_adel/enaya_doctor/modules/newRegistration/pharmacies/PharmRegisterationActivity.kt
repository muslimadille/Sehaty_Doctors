package com.muslim_adel.enaya_doctor.modules.newRegistration.pharmacies

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.base.BaseActivity
import com.muslim_adel.enaya_doctor.modules.base.GlideObject
import com.muslim_adel.enaya_doctor.modules.introwizerd.adapters.IntroPagerAdapter
import com.muslim_adel.enaya_doctor.modules.newRegistration.labs.fragment.LabRegisterationFragment1
import com.muslim_adel.enaya_doctor.modules.newRegistration.labs.fragment.LabRegisterationFragment2
import com.muslim_adel.enaya_doctor.modules.newRegistration.labs.fragment.LabRegisterationFragment3
import com.muslim_adel.enaya_doctor.modules.newRegistration.labs.fragment.LabRegisterationFragment5
import com.muslim_adel.enaya_doctor.modules.newRegistration.pharmacies.fragment.PharmRegisterationFragment1
import com.muslim_adel.enaya_doctor.modules.newRegistration.pharmacies.fragment.PharmRegisterationFragment2
import com.muslim_adel.enaya_doctor.modules.newRegistration.pharmacies.fragment.PharmRegisterationFragment3
import com.muslim_adel.enaya_doctor.modules.newRegistration.pharmacies.fragment.PharmRegisterationFragment4
import com.muslim_adel.enaya_doctor.modules.registration.PharmRegisterationValidator
import com.muslim_adel.enaya_doctor.modules.registration.VerivicationActivity
import com.muslim_adel.enaya_doctor.remote.apiServices.ApiClient
import com.muslim_adel.enaya_doctor.remote.apiServices.SessionManager
import com.muslim_adel.enaya_doctor.remote.objects.BaseResponce
import com.muslim_adel.enaya_doctor.remote.objects.PharmacyLoginResponce
import com.muslim_adel.enaya_doctor.utiles.Q
import kotlinx.android.synthetic.main.activity_lab_registeration.*
import kotlinx.android.synthetic.main.activity_lab_registeration.doctor_registration_next_btn
import kotlinx.android.synthetic.main.activity_lab_registeration.doctor_registration_pager
import kotlinx.android.synthetic.main.activity_lab_registeration.new_reg_progrss_lay
import kotlinx.android.synthetic.main.activity_lab_registeration.register_text_btn
import kotlinx.android.synthetic.main.activity_lab_registration.*
import kotlinx.android.synthetic.main.activity_lab_registration.edit_about_doc_ar_txt
import kotlinx.android.synthetic.main.activity_lab_registration.edit_about_doc_en_txt
import kotlinx.android.synthetic.main.activity_lab_registration.edit_doc_profile_img
import kotlinx.android.synthetic.main.activity_lab_registration.edit_fna_txt
import kotlinx.android.synthetic.main.activity_lab_registration.edit_fne_txt
import kotlinx.android.synthetic.main.activity_lab_registration.edit_lm_ar_txt
import kotlinx.android.synthetic.main.activity_lab_registration.edit_lm_en_txt
import kotlinx.android.synthetic.main.activity_lab_registration.edit_lna_txt
import kotlinx.android.synthetic.main.activity_lab_registration.edit_lne_txt
import kotlinx.android.synthetic.main.activity_lab_registration.edit_location_txt
import kotlinx.android.synthetic.main.activity_lab_registration.edit_num_of_days_txt
import kotlinx.android.synthetic.main.activity_lab_registration.edit_practiceLicenseIDImage_img
import kotlinx.android.synthetic.main.activity_lab_registration.edit_profissionalTitleID_img
import kotlinx.android.synthetic.main.activity_lab_registration.edit_sn_en_txt
import kotlinx.android.synthetic.main.activity_lab_registration.edit_sn_txt
import kotlinx.android.synthetic.main.activity_lab_registration.email
import kotlinx.android.synthetic.main.activity_lab_registration.gender_female
import kotlinx.android.synthetic.main.activity_lab_registration.gender_male
import kotlinx.android.synthetic.main.activity_lab_registration.hid_map_btn
import kotlinx.android.synthetic.main.activity_lab_registration.map_lay
import kotlinx.android.synthetic.main.activity_lab_registration.password
import kotlinx.android.synthetic.main.activity_lab_registration.phar_name_ar_txt
import kotlinx.android.synthetic.main.activity_lab_registration.phar_name_en_txt
import kotlinx.android.synthetic.main.activity_lab_registration.phon_num
import kotlinx.android.synthetic.main.activity_lab_registration.progrss_lay
import kotlinx.android.synthetic.main.activity_lab_registration.re_regions_spinner
import kotlinx.android.synthetic.main.activity_lab_registration.register_btn_btn
import kotlinx.android.synthetic.main.activity_lab_registration.select_location
import kotlinx.android.synthetic.main.activity_pharm_registeration.*
import kotlinx.android.synthetic.main.activity_pharm_registration.*
import kotlinx.android.synthetic.main.fragment_registration2.*
import kotlinx.android.synthetic.main.new_registration_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import java.util.function.Consumer
import kotlin.collections.ArrayList

class PharmRegisterationActivity : BaseActivity(), OnMapReadyCallback, LocationListener {
    var pharmRegisterationValidator: PharmRegisterationValidator?=null
    private val fragmentList = ArrayList<Fragment>()
    private val  adapter= IntroPagerAdapter(this)
    private val fragment1= PharmRegisterationFragment1()
    private val fragment2= PharmRegisterationFragment2()
    private val fragment3= PharmRegisterationFragment3()
    private val fragment4= PharmRegisterationFragment4()

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var mMap: GoogleMap
    var fusedLocationProviderClient: FusedLocationProviderClient?=null
    var currentLocation: Location?=null
    var currentMrker: Marker?=null
    var key=0
    var lat="3.000"
    var lng="2.000"

    var isMobileVerified:Boolean=false
    var isEmailVerified:Boolean=false
    private lateinit var locationCallback2: LocationCallback
    lateinit  var locationManager: LocationManager


    var countryId:Int=1;
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharm_registeration)
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        locationCallback2 = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                if(p0.lastLocation!=null&&currentLocation==null){
                    currentLocation=p0.lastLocation
                    val mapFragment = supportFragmentManager
                        .findFragmentById(R.id.lab_reg_map_frag) as SupportMapFragment
                    mapFragment.getMapAsync(this@PharmRegisterationActivity)
                }
            }
        }
        onNextClicked()
        initRegistrationModel()
        initPagerAdapter()
        fetchLocation()

    }
    val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    result?.let {
                        if(fragment2.is_profile_img_clecked){
                            fragment2.selectedImage = File(result!!.data!!.data!!.path!!)
                            edit_doc_profile_img.setImageURI(fragment2.selectedImage!!.toUri())
                            fragment2.is_profile_img_clecked=false

                        }
                        if(fragment2.is_practiceLicenseID_clecked){
                            fragment2. selectedpracticeLicenseIDImage = File(result!!.data!!.data!!.path!!)
                            edit_practiceLicenseIDImage_img.setImageURI(fragment2.selectedpracticeLicenseIDImage!!.toUri())
                            fragment2.is_practiceLicenseID_clecked=false
                        }
                        if(fragment2.is_profissionalTitleID_clecked){
                            fragment2.selectedprofissionalTitleIDImage = File(result!!.data!!.data!!.path!!)
                            edit_profissionalTitleID_img.setImageURI(fragment2.selectedprofissionalTitleIDImage!!.toUri())
                            fragment2.is_profissionalTitleID_clecked=false
                        }
                    }
                }


            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }


    private fun initRegistrationModel() {
        pharmRegisterationValidator= PharmRegisterationValidator(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
        "")
    }
    private fun initPagerAdapter() {
        doctor_registration_pager.adapter = adapter
        fragmentList.addAll(listOf(
            fragment1, fragment2, fragment3,fragment4
        ))
        adapter.setFragmentList(fragmentList)
        doctor_registration_pager.setUserInputEnabled(false)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun onNextClicked(){
        doctor_registration_next_btn.setOnClickListener {

            when(doctor_registration_pager.currentItem){
                0->{
                    if(fragment1.checkValidation()){
                        fragment1.validateInputData()
                        checkMobile()

                    }
                }
                1->{
                    if(fragment2.checkValidation()){
                        fragment2.validateInputData()
                        if (doctor_registration_pager.currentItem <(fragmentList.size-1)) {
                            doctor_registration_pager.currentItem = doctor_registration_pager.currentItem +1

                        }
                    }
                }
                2->{
                    if(fragment3.checkValidation()){
                        fragment3.validateInputData()
                        if (doctor_registration_pager.currentItem <(fragmentList.size-1)) {
                            doctor_registration_pager.currentItem = doctor_registration_pager.currentItem +1

                        }
                    }
                }
                3->{
                    if(fragment4.checkValidation()){
                        fragment4.validateInputData()
                        register()

                    }
                }
                4->{

                }
            }

        }
    }
    private fun onObserveStart() {
        new_reg_progrss_lay?.visibility = View.VISIBLE
    }
    private fun onObserveSuccess() {
        isMobileVerified=true;
        new_reg_progrss_lay?.visibility = View.GONE
    }
    private fun onObservefaled() {
        new_reg_progrss_lay?.visibility = View.GONE
        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
    }

    fun register(){
        onObserveStart()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).pharmRegistration(
            Q.selectedCountry.id.toString(),
            pharmRegisterationValidator!!.password,
            pharmRegisterationValidator!!.phonenumber,
            pharmRegisterationValidator!!.email,
            pharmRegisterationValidator!!.gender_id,
            pharmRegisterationValidator!!.featured,
            pharmRegisterationValidator!!.firstName_en,
            pharmRegisterationValidator!!.firstName_ar,
            pharmRegisterationValidator!!.lastName_en,
            pharmRegisterationValidator!!.lastName_ar,
            pharmRegisterationValidator!!.pharmName_ar,
            pharmRegisterationValidator!!.pharmName_en,
            pharmRegisterationValidator!!.about_ar,
            pharmRegisterationValidator!!.about_en,
            pharmRegisterationValidator!!.practiceLicenseID,
            pharmRegisterationValidator!!.profissionalTitleID,
            pharmRegisterationValidator!!.area_id,
            pharmRegisterationValidator!!.num_of_day,
            pharmRegisterationValidator!!.address_en,
            pharmRegisterationValidator!!.address_ar,
            pharmRegisterationValidator!!.landmark_en,
            pharmRegisterationValidator!!.landmark_ar,
            pharmRegisterationValidator!!.lng,
            pharmRegisterationValidator!!.lat,
            "0",
            
        )
            .enqueue(object : Callback<PharmacyLoginResponce> {
                override fun onFailure(call: Call<PharmacyLoginResponce>, t: Throwable) {
                    alertNetwork(false)
                }

                override fun onResponse(
                    call: Call<PharmacyLoginResponce>,
                    response: Response<PharmacyLoginResponce>
                ) {
                    val loginResponse = response.body()
                    if (response!!.isSuccessful) {
                        if (loginResponse!!.success) {
                            onObserveSuccess()
                            if (loginResponse?.data!!.status == 200 && loginResponse!!.data!!.user != null) {
                               /* sessionManager.saveAuthToken(loginResponse!!.data!!.token,loginResponse!!.data!!.user!!.country_id!!)
                                preferences!!.putString("tok",loginResponse!!.data!!.token.toString())
                                preferences!!.putInteger("COUNTRY_ID",loginResponse!!.data!!.user!!.country_id!!)
                                preferences!!.putBoolean(Q.IS_FIRST_TIME, false)
                                preferences!!.putBoolean(Q.IS_LOGIN, true)
                                preferences!!.putString(Q.USER_TYPE, Q.USER_LAB)
                                preferences!!.putInteger(
                                    Q.USER_ID,
                                    loginResponse!!.data!!.user.id.toInt()
                                )
                                preferences!!.commit()
                               */ onObserveSuccess()
                                val intent =
                                    Intent(this@PharmRegisterationActivity, VerivicationActivity::class.java)
                                intent.putExtra("phone",loginResponse!!.data!!.user.phonenumber.toString())
                                intent.putExtra("type","laboratory")
                                startActivity(intent)
                                finish()
                            }
                        } else {

                            onObservefaled()
                            Toast.makeText(this@PharmRegisterationActivity, "تأكد من ادخال جميع البيانات بشكل صحيح", Toast.LENGTH_SHORT).show()

                        }

                    } else {
                        onObservefaled()
                    }

                }


            })
    }
    private fun checkMobile(){
        onObserveStart()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).pharmPhoneValidator(
            Q.selectedCountry.phoneCode+pharmRegisterationValidator!!.phonenumber)

            .enqueue(object : Callback<BaseResponce<Any>> {
                override fun onFailure(call: Call<BaseResponce<Any>>, t: Throwable) {
                    alertNetwork(false)
                }

                override fun onResponse(
                    call: Call<BaseResponce<Any>>,
                    response: Response<BaseResponce<Any>>
                ) {
                    val myResponse = response.body()
                    if (response!!.isSuccessful) {
                        if (myResponse!!.success) {
                            onObserveSuccess()
                            isMobileVerified=true
                            checkEmail()

                        } else {

                            onObservefaled()
                            Toast.makeText(this@PharmRegisterationActivity, "رقم الموبيل مستخدم من قبل", Toast.LENGTH_SHORT).show()

                        }

                    } else {
                        onObservefaled()
                    }

                }


            })
    }
    private fun checkEmail(){
        onObserveStart()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).pharmEmailValidator(pharmRegisterationValidator!!.email)

            .enqueue(object : Callback<BaseResponce<Any>> {
                override fun onFailure(call: Call<BaseResponce<Any>>, t: Throwable) {
                    alertNetwork(false)
                }

                override fun onResponse(
                    call: Call<BaseResponce<Any>>,
                    response: Response<BaseResponce<Any>>
                ) {
                    val myResponse = response.body()
                    if (response!!.isSuccessful) {
                        if (myResponse!!.success) {
                            onObserveSuccess()
                            isEmailVerified=true
                            if (doctor_registration_pager.currentItem <(fragmentList.size-1)) {
                                doctor_registration_pager.currentItem = doctor_registration_pager.currentItem +1

                            }  else {
                                register_text_btn.text="register"
                            }

                        } else {

                            onObservefaled()
                            Toast.makeText(this@PharmRegisterationActivity, "البريد الإلكتروني  مستخدم من قبل", Toast.LENGTH_SHORT).show()

                        }

                    } else {
                        onObservefaled()
                    }

                }


            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1000->{if(grantResults.size!=0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                fetchLocation()
            }}
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        MapsInitializer.initialize(this)
        mMap = googleMap
        val lat_long= LatLng(currentLocation?.latitude!!,currentLocation?.longitude!!)
        drawMarker(lat_long)
        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{
            override fun onMarkerDragStart(p0: Marker) {
            }

            override fun onMarkerDrag(p0: Marker) {
            }

            override fun onMarkerDragEnd(p0: Marker) {

                if(currentMrker!=null){
                    currentMrker?.remove()
                    var newLatLng= LatLng(p0?.position!!.latitude,p0.position.longitude)
                    drawMarker(newLatLng)

                }
            }
        })

    }
    private fun drawMarker(lat_long: LatLng){
        val markerOption= MarkerOptions().position(lat_long).draggable(true)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(lat_long))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat_long,15f))
        currentMrker=mMap.addMarker(markerOption)
        currentMrker?.showInfoWindow()
        lat=lat_long.latitude.toString()
        lng=lat_long.longitude.toString()

    }
    fun fetchLocation(){
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1000)
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),1000)

            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100L,10f,this@PharmRegisterationActivity)
        } else {

            startLocationUpdates()

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
    fun showMap(){
        map_lay_pharm_reg.visibility=View.VISIBLE
    }
    fun hideMap(){
        map_lay_pharm_reg.visibility=View.GONE
    }
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onLocationChanged(p0: Location) {
        if (null != p0&&currentLocation==null) {
            this.currentLocation=p0
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.pharm_reg_map_frag) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }
}