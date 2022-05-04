package com.muslim_adel.enaya_doctor.modules.newRegistration.doctor

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.muslim_adel.enaya_doctor.modules.newRegistration.doctor.fragments.*
import com.muslim_adel.enaya_doctor.modules.registration.VerivicationActivity
import com.muslim_adel.enaya_doctor.remote.objects.doctor.DoctorRigistrationValidator
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.base.BaseActivity
import com.muslim_adel.enaya_doctor.modules.introwizerd.adapters.IntroPagerAdapter
import com.muslim_adel.enaya_doctor.remote.apiServices.ApiClient
import com.muslim_adel.enaya_doctor.remote.apiServices.SessionManager
import com.muslim_adel.enaya_doctor.remote.objects.BaseResponce
import com.muslim_adel.enaya_doctor.remote.objects.LoginResponce
import com.muslim_adel.enaya_doctor.utiles.Q
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_doctor_registration_screen.*
import kotlinx.android.synthetic.main.activity_intro_wizard.*
import kotlinx.android.synthetic.main.fragment_registration2.*
import kotlinx.android.synthetic.main.fragment_registration2.edit_doc_profile_img
import kotlinx.android.synthetic.main.fragment_registration2.edit_practiceLicenseIDImage_img
import kotlinx.android.synthetic.main.fragment_registration2.edit_profissionalTitleID_img
import kotlinx.android.synthetic.main.fragment_registration5.*
import kotlinx.android.synthetic.main.new_registration_layout.*
import kotlinx.android.synthetic.main.new_registration_layout.edit_location_txt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class DoctorRegistrationScreen : BaseActivity(),OnMapReadyCallback {
    var doctorRegistrationModel: DoctorRigistrationValidator? = null
    private val fragmentList = ArrayList<Fragment>()
    private val  adapter=IntroPagerAdapter(this)
    private val fragment1=RegistrationFragment1()
    private val fragment2= RegistrationFragment2()
    private val fragment3= RegistrationFragment3()
    private val fragment4= RegistrationFragment4()
    private val fragment5= RegistrationFragment5()
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var mMap: GoogleMap
    var fusedLocationProviderClient: FusedLocationProviderClient?=null
    var currentLocation: Location?=null
    var currentMrker:Marker?=null
    var key=0
    var lat="3.000"
    var lng="2.000"

    var isMobileVerified:Boolean=false
    var isEmailVerified:Boolean=false


    var countryId:Int=1;



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_registration_screen)
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        countryId=Q.selectedCountry.id!!
        initRegistrationModel()
        onNextClicked()
        initPagerAdapter()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var result: CropImage.ActivityResult? = null
            data?.let { result = CropImage.getActivityResult(data) }
            if (resultCode == AppCompatActivity.RESULT_OK) {
                result?.let {
                    if(fragment2.is_profile_img_clecked){
                        fragment2.selectedImage = File(result!!.uri!!.path!!)
                        edit_doc_profile_img.setImageURI(fragment2.selectedImage!!.toUri())
                        fragment2.is_profile_img_clecked=false

                    }
                    if(fragment2.is_practiceLicenseID_clecked){
                        fragment2. selectedpracticeLicenseIDImage = File(result!!.uri!!.path!!)
                        edit_practiceLicenseIDImage_img.setImageURI(fragment2.selectedpracticeLicenseIDImage!!.toUri())
                        fragment2.is_practiceLicenseID_clecked=false
                    }
                    if(fragment2.is_profissionalTitleID_clecked){
                        fragment2.selectedprofissionalTitleIDImage = File(result!!.uri!!.path!!)
                        edit_profissionalTitleID_img.setImageURI(fragment2.selectedprofissionalTitleIDImage!!.toUri())
                        fragment2.is_profissionalTitleID_clecked=false
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                result!!.error!!.printStackTrace()
            }
        }
    }


    private fun initRegistrationModel() {
        doctorRegistrationModel = DoctorRigistrationValidator(
            "", "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ArrayList<String>(),
            "",
            "",
            "",
            "",
            "",
            "",
            "", "", "", "", "", "", "", "", "",
            "", "", "",
            countryId
        )
    }
    private fun initPagerAdapter() {
        doctor_registration_pager.adapter = adapter
        fragmentList.addAll(listOf(
            fragment1, fragment2, fragment3,fragment4,fragment5
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
                        //fragment4.validateInputData()
                        if (doctor_registration_pager.currentItem <(fragmentList.size-1)) {
                            doctor_registration_pager.currentItem = doctor_registration_pager.currentItem +1

                        }
                    }
                }
                4->{
                    if(fragment5.checkValidation()){
                        fragment5.validateInputData()
                        register()

                    }
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
        apiClient.getApiService(this).DocRegistration(
            Q.selectedCountry.id.toString(),
            doctorRegistrationModel!!.password,
            doctorRegistrationModel!!.phonenumber,
            doctorRegistrationModel!!.email,
            doctorRegistrationModel!!.gender_id,
            doctorRegistrationModel!!.featured,
            doctorRegistrationModel!!.firstName_en,
            doctorRegistrationModel!!.firstName_ar,
            doctorRegistrationModel!!.lastName_en,
            doctorRegistrationModel!!.lastName_ar,
            doctorRegistrationModel!!.specialty_id,
            doctorRegistrationModel!!.subSpecialties_id,
            doctorRegistrationModel!!.prefix_title_id,
            doctorRegistrationModel!!.profissionalDetails_id,
            doctorRegistrationModel!!.profissionalTitle_en,
            doctorRegistrationModel!!.profissionalTitle_ar,
            doctorRegistrationModel!!.aboutDoctor_ar,
            doctorRegistrationModel!!.aboutDoctor_en,
            doctorRegistrationModel!!.practiceLicenseID,
            doctorRegistrationModel!!.profissionalTitleID,
            doctorRegistrationModel!!.area_id,
            doctorRegistrationModel!!.price,
            doctorRegistrationModel!!.waiting_time,
            doctorRegistrationModel!!.num_of_day,
            doctorRegistrationModel!!.address_en,
            doctorRegistrationModel!!.address_ar,
            doctorRegistrationModel!!.landmark_en,
            doctorRegistrationModel!!.landmark_ar,
            doctorRegistrationModel!!.lng,
            doctorRegistrationModel!!.lat,
        )
            .enqueue(object : Callback<LoginResponce> {
                override fun onFailure(call: Call<LoginResponce>, t: Throwable) {
                    alertNetwork(false)
                }

                override fun onResponse(
                    call: Call<LoginResponce>,
                    response: Response<LoginResponce>
                ) {
                    val loginResponse = response.body()
                    if (response!!.isSuccessful) {
                        if (loginResponse!!.success) {
                            onObserveSuccess()
                            if (loginResponse?.data!!.status == 200 && loginResponse!!.data!!.user != null) {
                                sessionManager.saveAuthToken(loginResponse!!.data!!.token,loginResponse!!.data!!.user!!.country_id!!)
                                preferences!!.putString("tok",loginResponse!!.data!!.token.toString())
                                preferences!!.putInteger("COUNTRY_ID",loginResponse!!.data!!.user!!.country_id!!)
                                preferences!!.putBoolean(Q.IS_FIRST_TIME, false)
                                preferences!!.putBoolean(Q.IS_LOGIN, true)
                                preferences!!.putString(Q.USER_TYPE,Q.USER_DOCTOR)
                                preferences!!.putInteger(
                                    Q.USER_ID,
                                    loginResponse!!.data!!.user.id.toInt()
                                )

                                preferences!!.commit()
                                onObserveSuccess()
                                val intent =
                                    Intent(this@DoctorRegistrationScreen, VerivicationActivity::class.java)
                                intent.putExtra("phone",loginResponse!!.data!!.user.phonenumber.toString())
                                intent.putExtra("type","doctor")
                                startActivity(intent)
                                finish()
                            }
                        } else {

                            onObservefaled()
                            Toast.makeText(this@DoctorRegistrationScreen, "تأكد من ادخال جميع البيانات بشكل صحيح", Toast.LENGTH_SHORT).show()

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
        apiClient.getApiService(this).doctorPhoneValidator(
            Q.selectedCountry.phoneCode+doctorRegistrationModel!!.phonenumber)

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
                            Toast.makeText(this@DoctorRegistrationScreen, "رقم الموبيل مستخدم من قبل", Toast.LENGTH_SHORT).show()

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
        apiClient.getApiService(this).doctorPhoneValidator(
            Q.selectedCountry.phoneCode+doctorRegistrationModel!!.phonenumber)

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
                            Toast.makeText(this@DoctorRegistrationScreen, "البريد الإلكتروني  مستخدم من قبل", Toast.LENGTH_SHORT).show()

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
        when(requestCode){
            1000->{if(grantResults.size!=0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
               // fragment5.fetchLocation()
            }}
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val lat_long= LatLng(currentLocation?.latitude!!,currentLocation?.longitude!!)
        drawMarker(lat_long)
        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{
            override fun onMarkerDragStart(p0: Marker?) {
            }

            override fun onMarkerDrag(p0: Marker?) {
            }

            override fun onMarkerDragEnd(p0: Marker?) {

                if(currentMrker!=null){
                    currentMrker?.remove()
                    var newLatLng= LatLng(p0?.position!!.latitude,p0.position.longitude)
                    drawMarker(newLatLng)

                }
            }
        })

    }
    private fun drawMarker(lat_long: LatLng){
        val markerOption= MarkerOptions().position(lat_long).snippet(getAddress(lat_long.latitude,lat_long.latitude)).draggable(true)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(lat_long))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat_long,15f))
        currentMrker=mMap.addMarker(markerOption)
        currentMrker?.showInfoWindow()
        fragment5.edit_location_txt.text=getAddress(lat_long.latitude,lat_long.longitude).toString()
        lat=lat_long.latitude.toString()
        lng=lat_long.longitude.toString()

    }
    private  fun getAddress(lat:Double,lng:Double):String{
        return try {
            val getCoder= Geocoder(this, Locale.getDefault())
            val address=getCoder.let { it.getFromLocation(lat,lng,1) }
            address[0].getAddressLine(0).toString()
        }catch (e:Error){
            ""
        }

    }




}