package com.muslim_adel.enaya_doctor.modules.registration

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.base.BaseActivity
import com.muslim_adel.enaya_doctor.modules.base.GlideObject
import com.muslim_adel.enaya_doctor.remote.apiServices.ApiClient
import com.muslim_adel.enaya_doctor.remote.apiServices.SessionManager
import com.muslim_adel.enaya_doctor.remote.objects.BaseResponce
import com.muslim_adel.enaya_doctor.remote.objects.LaboratoryLoginResponce
import com.muslim_adel.enaya_doctor.remote.objects.Reagons
import com.muslim_adel.enaya_doctor.utiles.Q
import com.muslim_adel.enaya_doctor.utiles.SpinnerAdapterCustomFont
import kotlinx.android.synthetic.main.activity_lab_registration.*
import kotlinx.android.synthetic.main.activity_lab_registration.edit_doc_profile_img
import kotlinx.android.synthetic.main.activity_lab_registration.edit_lna_txt
import kotlinx.android.synthetic.main.activity_lab_registration.edit_lne_txt
import kotlinx.android.synthetic.main.activity_lab_registration.edit_practiceLicenseIDImage_img
import kotlinx.android.synthetic.main.activity_lab_registration.edit_profissionalTitleID_img
import kotlinx.android.synthetic.main.activity_lab_registration.gender_female
import kotlinx.android.synthetic.main.activity_lab_registration.gender_male
import kotlinx.android.synthetic.main.fragment_registration2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import java.util.function.Consumer
import kotlin.collections.ArrayList

class LabRegistrationActivity : BaseActivity() , OnMapReadyCallback {
    var name=""
    var lat="3.000"
    var lng="2.000"

    var is_practiceLicenseID_clecked=false
    var is_profissionalTitleID_clecked=false
    var is_profile_img_clecked=false


    private lateinit var mMap: GoogleMap
    var fusedLocationProviderClient: FusedLocationProviderClient?=null
    var currentLocation: Location?=null
    var currentMrker: Marker?=null
    var key=0
    private var selectedImage: File? = null
    private var selectedpracticeLicenseIDImage: File? = null
    private var selectedprofissionalTitleIDImage: File? = null

    private var regionsList = ArrayList<Reagons>()
    private var regionsNameList = ArrayList<String>()
    private lateinit var regionsSpinnerAdapter: SpinnerAdapterCustomFont
    private var selectedRegionId=0

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private var gender=-1
    private var doctorValidator: LabRegistrationValidator?=null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab_registration)
        doctorValidator=LabRegistrationValidator("","","","","","","",
            "","","","","","","","","",
            "","","","","","","")
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        progrss_lay.setOnClickListener {

        }
        implementListeners()
        initSpinners()
        regonObserver()
        handelRdioStates()
        onSelectIMageClicked()
        onprofissionalTitleIDImageClicked()
        onpracticeLicenseIDImageClicked()
        onSelectLocationClicked()
        onRegisterClicked()
        onHideMapClicked()
    }
    fun fetchLocation(){
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1000)

            return
        }

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.getCurrentLocation(LocationManager.NETWORK_PROVIDER, null, this.mainExecutor, locationCallback)

        } else {

            val task=fusedLocationProviderClient?.lastLocation
            task?.addOnSuccessListener { location->
                if(location!=null){
                    this.currentLocation=location
                    val mapFragment = supportFragmentManager
                        .findFragmentById(R.id.lab_reg_map_frag) as SupportMapFragment
                    mapFragment.getMapAsync(this)
                }
            }
        }

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
        return ""/*try {
            val getCoder= Geocoder(this, Locale.getDefault())
            val address=getCoder.let { it.getFromLocation(lat,lng,1) }
            address[0].getAddressLine(0).toString()
        }catch (e:Error){
            ""
        }*/

    }

    //SPINNERS
    private fun initSpinners(){

        regionsSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, regionsNameList)
        regionsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        regionsSpinnerAdapter.textSize = 12
        re_regions_spinner.adapter = regionsSpinnerAdapter
        regionsSpinnerAdapter.notifyDataSetChanged()


    }
    private fun implementListeners() {

        re_regions_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if(regionsList.isNotEmpty()){
                    selectedRegionId=regionsList[position].id
                    doctorValidator!!.area_id=regionsList[position].id.toString()
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }


    }

    //OTHER FUNCTIONS
    private fun handelRdioStates(){
        gender_male.isChecked=true
        gender=1
        gender_male.setOnClickListener {
            gender_male.isChecked=true
            gender_female.isChecked=false
            gender=1
            if(gender_male.isChecked){
                gender=1
                gender_female.isChecked=false
            }else if(gender_female.isChecked){
                gender=2
                gender_male.isChecked=false
            }
        }
        gender_female.setOnClickListener {
            gender_female.isChecked=true
            gender_male.isChecked=false
            gender=2
            if(gender_male.isChecked){
                gender=1
                gender_female.isChecked=false
            }else if(gender_female.isChecked){
                gender=2
                gender_male.isChecked=false
            }
        }

    }
    private fun onSelectIMageClicked(){
        edit_doc_profile_img.setOnClickListener {
            is_profile_img_clecked=true
            is_practiceLicenseID_clecked=false
            is_profissionalTitleID_clecked=false
            selectProfileImage()

        }
    }
    private fun onpracticeLicenseIDImageClicked(){
        edit_practiceLicenseIDImage_img.setOnClickListener {
            is_profile_img_clecked=false
            is_practiceLicenseID_clecked=true
            is_profissionalTitleID_clecked=false
            selectProfileImage()

        }
    }
    private fun onprofissionalTitleIDImageClicked(){
        edit_profissionalTitleID_img.setOnClickListener {
            is_profile_img_clecked=false
            is_practiceLicenseID_clecked=false
            is_profissionalTitleID_clecked=true
            selectProfileImage()

        }
    }
    fun selectProfileImage() {
        TedPermission.with(this)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    ImagePicker.with(this@LabRegistrationActivity)
                        .compress(1024)         //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                        .createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(this@LabRegistrationActivity,
                        "getString(R.string.permissionDenied)", Toast.LENGTH_SHORT)
                        .show()
                }
            })
            .setPermissions(Manifest.permission.CAMERA)
            .check()
    }
    val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    result?.let {
                        if(is_profile_img_clecked){
                            selectedImage = File(result!!.data!!.data!!.path!!)
                            GlideObject.GlideProfilePic(this, selectedImage!!.path, edit_doc_profile_img)
                            is_profile_img_clecked=false

                        }
                        if(is_practiceLicenseID_clecked){
                            selectedpracticeLicenseIDImage = File(result!!.data!!.data!!.path!!)
                            GlideObject.GlideProfilePic(this, selectedpracticeLicenseIDImage!!.path, edit_practiceLicenseIDImage_img)
                            is_practiceLicenseID_clecked=false
                        }
                        if(is_profissionalTitleID_clecked){
                            selectedprofissionalTitleIDImage = File(result!!.data!!.data!!.path!!)
                            GlideObject.GlideProfilePic(this, selectedprofissionalTitleIDImage!!.path, edit_profissionalTitleID_img)
                            is_profissionalTitleID_clecked=false
                        }
                    }
                }


            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toBase64(filePath: String): String{
        val bytes = File(filePath).readBytes()
        val base64 = Base64.getEncoder().encodeToString(bytes)
        return base64
    }
    private fun onSelectLocationClicked(){
        select_location.setOnClickListener {
            map_lay.visibility= View.VISIBLE
        }
    }

    //OBSERVE STATES
    private fun onObserveStart() {
        progrss_lay?.visibility = View.VISIBLE
    }
    private fun onObserveSuccess() {
        progrss_lay?.visibility = View.GONE
    }
    private fun onObservefaled() {
        progrss_lay?.visibility = View.GONE
        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
    }

    //OBSERVERS

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

    private fun upDateDoctorProfile(){
        onObserveStart()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).labRegistration(
            Q.selectedCountry.id.toString(),
            doctorValidator!!.password,
            doctorValidator!!.phonenumber,
            doctorValidator!!.email,
            doctorValidator!!.gender_id,
            doctorValidator!!.featured,
            doctorValidator!!.firstName_en,
            doctorValidator!!.firstName_ar,
            doctorValidator!!.lastName_en,
            doctorValidator!!.lastName_ar,
            doctorValidator!!.laboratory_name_ar,
            doctorValidator!!.laboratory_name_en,
            doctorValidator!!.about_ar,
            doctorValidator!!.about_en,
            doctorValidator!!.practiceLicenseID,
            doctorValidator!!.profissionalTitleID,
            doctorValidator!!.area_id,
            doctorValidator!!.num_of_day,
            doctorValidator!!.address_en,
            doctorValidator!!.address_ar,
            doctorValidator!!.landmark_en,
            doctorValidator!!.landmark_ar,
            doctorValidator!!.lng,
            doctorValidator!!.lat,
            "0",
            "0",
            "0",
            "0",
            "0"
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
                                    Intent(this@LabRegistrationActivity, VerivicationActivity::class.java)
                                intent.putExtra("phone",loginResponse!!.data!!.user.phonenumber.toString())
                                intent.putExtra("type","laboratory")
                                startActivity(intent)
                                finish()
                            }
                        } else {

                            onObservefaled()
                            Toast.makeText(this@LabRegistrationActivity, "تأكد من ادخال جميع البيانات بشكل صحيح", Toast.LENGTH_SHORT).show()

                        }

                    } else {
                        onObservefaled()
                    }

                }


            })
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun fillData(){
        doctorValidator!!.gender_id=gender.toString()
        doctorValidator!!.firstName_ar=edit_fna_txt.text.toString()
        doctorValidator!!.firstName_en=edit_fne_txt.text.toString()
        doctorValidator!!.lastName_ar=edit_lna_txt.text.toString()
        doctorValidator!!.lastName_en=edit_lne_txt.text.toString()
        doctorValidator!!.lastName_en=edit_lne_txt.text.toString()
        doctorValidator!!.laboratory_name_ar=phar_name_ar_txt.text.toString()
        doctorValidator!!.laboratory_name_en=phar_name_en_txt.text.toString()
        doctorValidator!!.email=email.text.toString()
        doctorValidator!!.password=password.text.toString()
        doctorValidator!!.phonenumber=Q.PHONE_KEY+phon_num.text.toString()
        doctorValidator!!.num_of_day=edit_num_of_days_txt.text.toString()
        doctorValidator!!.about_ar=edit_about_doc_ar_txt.text.toString()
        doctorValidator!!.about_en=edit_about_doc_en_txt.text.toString()
        doctorValidator!!.address_ar=edit_sn_txt.text.toString()
        doctorValidator!!.address_en=edit_sn_en_txt.text.toString()
        doctorValidator!!.landmark_ar=edit_lm_ar_txt.text.toString()
        doctorValidator!!.landmark_en=edit_lm_en_txt.text.toString()
        doctorValidator!!.lat=lat
        doctorValidator!!.lng=lng
        var img=""
        if (selectedImage != null) {
            img = "data:image/${selectedImage!!.extension};base64,"+toBase64(selectedImage.toString())
        }
        doctorValidator!!.featured=img
        var img2=""
        if (selectedpracticeLicenseIDImage != null) {
            img2 = "data:image/${selectedpracticeLicenseIDImage!!.extension};base64,"+toBase64(selectedpracticeLicenseIDImage.toString())
        }
        doctorValidator!!.practiceLicenseID=img2
        var img3=""
        if (selectedprofissionalTitleIDImage != null) {
            img3 = "data:image/${selectedprofissionalTitleIDImage!!.extension};base64,"+toBase64(selectedprofissionalTitleIDImage.toString())
        }
        doctorValidator!!.profissionalTitleID=img3
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun onRegisterClicked(){
        register_btn_btn.setOnClickListener {
            fillData()
            upDateDoctorProfile()
        }
    }
    private fun onHideMapClicked(){
        hid_map_btn.setOnClickListener {
            map_lay.visibility= View.GONE
        }
    }
    private fun watingTimeList(){
        var l=ArrayList<String>()
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")
        l.add("00:05:00")


    }



    private val locationCallback = Consumer<Location> { location ->
        if (null != location) {
            this.currentLocation=location
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.lab_reg_map_frag) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }

}