package com.muslim_adel.sehatydoctors.modules.newRegistration.labs

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
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.muslim_adel.sehatydoctors.modules.newRegistration.doctor.fragments.*
import com.muslim_adel.sehatydoctors.modules.newRegistration.labs.fragment.*
import com.muslim_adel.sehatydoctors.modules.registration.LabRegistrationValidator
import com.muslim_adel.sehatydoctors.modules.registration.VerivicationActivity
import com.muslim_adel.sehatydoctors.remote.objects.doctor.DoctorRigistrationValidator
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.base.BaseActivity
import com.seha_khanah_doctors.modules.introwizerd.adapters.IntroPagerAdapter
import com.seha_khanah_doctors.remote.apiServices.ApiClient
import com.seha_khanah_doctors.remote.apiServices.SessionManager
import com.seha_khanah_doctors.remote.objects.BaseResponce
import com.seha_khanah_doctors.remote.objects.LaboratoryLoginResponce
import com.seha_khanah_doctors.remote.objects.LoginResponce
import com.seha_khanah_doctors.utiles.Q
import kotlinx.android.synthetic.main.activity_doctor_registration_screen.*
import kotlinx.android.synthetic.main.activity_lab_registeration.*
import kotlinx.android.synthetic.main.activity_lab_registeration.doctor_registration_next_btn
import kotlinx.android.synthetic.main.activity_lab_registeration.doctor_registration_pager
import kotlinx.android.synthetic.main.activity_lab_registeration.new_reg_progrss_lay
import kotlinx.android.synthetic.main.activity_lab_registeration.register_text_btn
import kotlinx.android.synthetic.main.new_registration_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class LabRegisterationActivity : BaseActivity(), OnMapReadyCallback {
     var labRegisterationModel: LabRegistrationValidator?=null
    private val fragmentList = ArrayList<Fragment>()
    private val  adapter= IntroPagerAdapter(this)
    private val fragment1= LabRegisterationFragment1()
    private val fragment2= LabRegisterationFragment2()
    private val fragment3= LabRegisterationFragment3()
    private val fragment4= LabRegisterationFragment4()
    private val fragment5= LabRegisterationFragment5()

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


    var countryId:Int=1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab_registeration)
        initRegistrationModel()
        initPagerAdapter()
    }
    private fun initRegistrationModel() {
        labRegisterationModel=LabRegistrationValidator(
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
            fragment1, fragment2, fragment3,fragment4,fragment5
        ))
        adapter.setFragmentList(fragmentList)
        doctor_registration_pager.setUserInputEnabled(false)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun onNextClicked(){
        doctor_registration_next_btn.setOnClickListener {

           /* when(doctor_registration_pager.currentItem){
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
            }*/

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
        apiClient.getApiService(this).labRegistration(
            Q.selectedCountry.id.toString(),
            labRegisterationModel!!.password,
            labRegisterationModel!!.phonenumber,
            labRegisterationModel!!.email,
            labRegisterationModel!!.gender_id,
            labRegisterationModel!!.featured,
            labRegisterationModel!!.firstName_en,
            labRegisterationModel!!.firstName_ar,
            labRegisterationModel!!.lastName_en,
            labRegisterationModel!!.lastName_ar,
            labRegisterationModel!!.laboratory_name_ar,
            labRegisterationModel!!.laboratory_name_en,
            labRegisterationModel!!.about_ar,
            labRegisterationModel!!.about_en,
            labRegisterationModel!!.practiceLicenseID,
            labRegisterationModel!!.profissionalTitleID,
            labRegisterationModel!!.area_id,
            labRegisterationModel!!.num_of_day,
            labRegisterationModel!!.address_en,
            labRegisterationModel!!.address_ar,
            labRegisterationModel!!.landmark_en,
            labRegisterationModel!!.landmark_ar,
            labRegisterationModel!!.lng,
            labRegisterationModel!!.lat,
        )
            .enqueue(object : Callback<LaboratoryLoginResponce> {
            override fun onFailure(call: Call<LaboratoryLoginResponce>, t: Throwable) {
                alertNetwork(false)
            }

            override fun onResponse(
                call: Call<LaboratoryLoginResponce>,
                response: Response<LaboratoryLoginResponce>
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
                            preferences!!.putString(Q.USER_TYPE, Q.USER_LAB)
                            preferences!!.putInteger(
                                Q.USER_ID,
                                loginResponse!!.data!!.user.id.toInt()
                            )

                            preferences!!.commit()
                            onObserveSuccess()
                            val intent =
                                Intent(this@LabRegisterationActivity, VerivicationActivity::class.java)
                            intent.putExtra("phone",loginResponse!!.data!!.user.phonenumber.toString())
                            intent.putExtra("type","laboratory")
                            startActivity(intent)
                            finish()
                        }
                    } else {

                        onObservefaled()
                        Toast.makeText(this@LabRegisterationActivity, "تأكد من ادخال جميع البيانات بشكل صحيح", Toast.LENGTH_SHORT).show()

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
            Q.selectedCountry.phoneCode+labRegisterationModel!!.phonenumber)

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
                            Toast.makeText(this@LabRegisterationActivity, "رقم الموبيل مستخدم من قبل", Toast.LENGTH_SHORT).show()

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
            Q.selectedCountry.phoneCode+labRegisterationModel!!.phonenumber)

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
                            Toast.makeText(this@LabRegisterationActivity, "البريد الإلكتروني  مستخدم من قبل", Toast.LENGTH_SHORT).show()

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