package com.seha_khanah_doctors.modules.registration

import android.Manifest
import android.content.Intent
import com.seha_khanah_doctors.remote.apiServices.SessionManager
import android.content.pm.PackageManager
import android.icu.text.IDNA
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
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
import com.muslim_adel.sehatydoctors.modules.registration.VerivicationActivity
import com.muslim_adel.sehatydoctors.remote.objects.doctor.DoctorRigistrationValidator
import com.seha_khanah_doctors.remote.apiServices.ApiClient
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.base.BaseActivity
import com.seha_khanah_doctors.modules.base.GlideObject
import com.seha_khanah_doctors.modules.home.MainActivity
import com.seha_khanah_doctors.remote.objects.*
import com.seha_khanah_doctors.remote.objects.doctor.DaysModel
import com.seha_khanah_doctors.remote.objects.doctor.DoctorProfileModel
import com.seha_khanah_doctors.remote.objects.doctor.SubSpiecialityModel
import com.seha_khanah_doctors.utiles.Q
import com.seha_khanah_doctors.utiles.SpinnerAdapterCustomFont
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_doctor_edit_profile.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.new_registration_layout.*
import kotlinx.android.synthetic.main.new_registration_layout.edit_about_doc_ar_txt
import kotlinx.android.synthetic.main.new_registration_layout.edit_about_doc_en_txt
import kotlinx.android.synthetic.main.new_registration_layout.edit_doc_profile_img
import kotlinx.android.synthetic.main.new_registration_layout.edit_fna_txt
import kotlinx.android.synthetic.main.new_registration_layout.edit_fne_txt
import kotlinx.android.synthetic.main.new_registration_layout.edit_lm_ar_txt
import kotlinx.android.synthetic.main.new_registration_layout.edit_lm_en_txt
import kotlinx.android.synthetic.main.new_registration_layout.edit_lna_txt
import kotlinx.android.synthetic.main.new_registration_layout.edit_lne_txt
import kotlinx.android.synthetic.main.new_registration_layout.edit_location_txt
import kotlinx.android.synthetic.main.new_registration_layout.edit_num_of_days_txt
import kotlinx.android.synthetic.main.new_registration_layout.edit_price_txt
import kotlinx.android.synthetic.main.new_registration_layout.edit_sn_en_txt
import kotlinx.android.synthetic.main.new_registration_layout.edit_sn_txt
import kotlinx.android.synthetic.main.new_registration_layout.hid_map_btn
import kotlinx.android.synthetic.main.new_registration_layout.map_lay
import kotlinx.android.synthetic.main.new_registration_layout.progrss_lay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class RegisterationActivity : BaseActivity(), OnMapReadyCallback {
    var name=""
    var lat="3.000"
    var lng="2.000"

    var is_practiceLicenseID_clecked=false
    var is_profissionalTitleID_clecked=false
    var is_profile_img_clecked=false



    private lateinit var mMap: GoogleMap
    var fusedLocationProviderClient: FusedLocationProviderClient?=null
    var currentLocation: Location?=null
    var currentMrker:Marker?=null
    var key=0

    private var selectedImage: File? = null
    private var selectedpracticeLicenseIDImage: File? = null
    private var selectedprofissionalTitleIDImage: File? = null



    private var regionsList = ArrayList<Reagons>()
    private var regionsNameList = ArrayList<String>()
    private lateinit var regionsSpinnerAdapter: SpinnerAdapterCustomFont
    private var selectedRegionId=0

    private lateinit var profDetailsSpinnerAdapter: SpinnerAdapterCustomFont
    private var profDetailsNamesList = ArrayList<String>()
    private var profDetailsList = ArrayList<DaysModel>()

    private lateinit var perfixSpinnerAdapter: SpinnerAdapterCustomFont
    private var perfixNamesList = ArrayList<String>()
    private var perfixList = ArrayList<SubSpiecialityModel>()


    private lateinit var SpicialitySpinnerAdapter: SpinnerAdapterCustomFont
    private var SpicialityNameList = ArrayList<String>()
    private var SpicList = ArrayList<Specialties>()

    private lateinit var subSpicialitySpinnerAdapter: SpinnerAdapterCustomFont
    private var subSpicialityNameList = ArrayList<String>()
    private var subSpicList = ArrayList<SubSpiecialityModel>()

    private lateinit var swatingTimeSpinnerAdapter: SpinnerAdapterCustomFont
    private var waitingTimeList = ArrayList<String>()
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private var gender=-1
    private var selectedSubSpic=ArrayList<String>()


    private var doctorValidator: DoctorRigistrationValidator?=null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_registration_layout)
        doctorValidator=DoctorRigistrationValidator("","","",
        "",
            "",
            "",
            "",
            "",
            "",
            "",
            selectedSubSpic,
            "",
            "",
            "",
            "",
            "",
            "",
            "","","","","","","","","",
            "","","",
        )
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        progrss_lay.setOnClickListener {

        }
        fetchLocation()
        implementListeners()
        initSpinners()
        regonObserver()
        handelRdioStates()
        onSelectIMageClicked()
        onprofissionalTitleIDImageClicked()
        onpracticeLicenseIDImageClicked()
        onSelectLocationClicked()
        profDetailsObserver()
        spiecObserver()
        SubSpiecObserver()
        onRegisterClicked()
        onHideMapClicked()
    }

//LOCATION
    private fun fetchLocation(){
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

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
        edit_location_txt.text=getAddress(lat_long.latitude,lat_long.longitude).toString()
        lat=lat_long.latitude.toString()
        lng=lat_long.longitude.toString()

    }
    private  fun getAddress(lat:Double,lng:Double):String{
        return try {
            val getCoder= Geocoder(this,Locale.getDefault())
            val address=getCoder.let { it.getFromLocation(lat,lng,1) }
            address[0].getAddressLine(0).toString()
        }catch (e:Error){
            ""
        }

    }

    //SPINNERS
    private fun initSpinners(){
        waitingTimeList.add(getString(R.string.waiting_time))
        waitingTimeList.add("5"+getString(R.string.min))
        waitingTimeList.add("10"+getString(R.string.min))
        waitingTimeList.add("15"+getString(R.string.min))
        waitingTimeList.add("20"+getString(R.string.min))
        waitingTimeList.add("25"+getString(R.string.min))
        waitingTimeList.add("30"+getString(R.string.min))
        waitingTimeList.add("35"+getString(R.string.min))
        waitingTimeList.add("40"+getString(R.string.min))
        waitingTimeList.add("45"+getString(R.string.min))
        waitingTimeList.add("50"+getString(R.string.min))
        waitingTimeList.add("55"+getString(R.string.min))
        waitingTimeList.add("60"+getString(R.string.min))
        swatingTimeSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, waitingTimeList)
        swatingTimeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        swatingTimeSpinnerAdapter.textSize = 12
        re_waiting_time_spinner.adapter = swatingTimeSpinnerAdapter
        swatingTimeSpinnerAdapter.notifyDataSetChanged()

        regionsSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, regionsNameList)
        regionsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        regionsSpinnerAdapter.textSize = 12
        re_regions_spinner.adapter = regionsSpinnerAdapter
        regionsSpinnerAdapter.notifyDataSetChanged()

        profDetailsSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, profDetailsNamesList)
        profDetailsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        profDetailsSpinnerAdapter.textSize = 12
        re_prof_details__spinner.adapter = profDetailsSpinnerAdapter

        perfixSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, perfixNamesList)
        perfixSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        perfixSpinnerAdapter.textSize = 12
        perfix_title_spinner.adapter = perfixSpinnerAdapter


        SpicialitySpinnerAdapter= SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, SpicialityNameList)
        SpicialitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        SpicialitySpinnerAdapter.textSize = 12
        re_speciality__spinner.adapter = SpicialitySpinnerAdapter

        subSpicialitySpinnerAdapter= SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, subSpicialityNameList)
        subSpicialitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        subSpicialitySpinnerAdapter.textSize = 12
        re_sub_speciality__spinner.adapter = subSpicialitySpinnerAdapter

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
/**-------------------------------------------------------------------------------------------------*/
        re_speciality__spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if(SpicList.isNotEmpty()){
                    doctorValidator!!.specialty_id=SpicList[position].id.toString()
                    perfixList.clear()
                    perfixNamesList.clear()
                    perfixObserver(SpicList[position].id)

                }

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        /**-------------------------------------------------------------------------------------------------*/
        re_sub_speciality__spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if(subSpicList.isNotEmpty()){
                    selectedSubSpic.clear()
                    selectedSubSpic.add(subSpicList[position].id.toString())
                    doctorValidator!!.subSpecialties_id=selectedSubSpic
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        /**-------------------------------------------------------------------------------------------------*/
        perfix_title_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if(perfixList.isNotEmpty()){
                    doctorValidator!!.prefix_title_id=perfixList[position].id.toString()
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        /**-------------------------------------------------------------------------------------------------*/
        re_prof_details__spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if(profDetailsList.isNotEmpty()){
                    doctorValidator!!.profissionalDetails_id=profDetailsList[position].id.toString()
                    doctorValidator!!.profissionalTitle_ar=profDetailsList[position].name_ar
                    doctorValidator!!.profissionalTitle_en=profDetailsList[position].name_en
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
                    CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) CropImageView.CropShape.RECTANGLE else CropImageView.CropShape.OVAL)
                        .setAllowFlipping(false)
                        .setAllowRotation(false)
                        .setCropMenuCropButtonIcon(R.drawable.ic_add)
                        .setAspectRatio(1, 1)
                        .start(this@RegisterationActivity)
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(this@RegisterationActivity,
                        "getString(R.string.permissionDenied)", Toast.LENGTH_SHORT)
                        .show()
                }
            })
            .setPermissions(Manifest.permission.CAMERA)
            .check()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var result: CropImage.ActivityResult? = null
            data?.let { result = CropImage.getActivityResult(data) }
            if (resultCode == RESULT_OK) {
                result?.let {
                    if(is_profile_img_clecked){
                        selectedImage = File(result!!.uri!!.path!!)
                        GlideObject.GlideProfilePic(this, selectedImage!!.path, edit_doc_profile_img)
                        is_profile_img_clecked=false

                    }
                    if(is_practiceLicenseID_clecked){
                        selectedpracticeLicenseIDImage = File(result!!.uri!!.path!!)
                        GlideObject.GlideProfilePic(this, selectedpracticeLicenseIDImage!!.path, edit_practiceLicenseIDImage_img)
                        is_practiceLicenseID_clecked=false
                    }
                    if(is_profissionalTitleID_clecked){
                        selectedprofissionalTitleIDImage = File(result!!.uri!!.path!!)
                        GlideObject.GlideProfilePic(this, selectedprofissionalTitleIDImage!!.path, edit_profissionalTitleID_img)
                        is_profissionalTitleID_clecked=false
                    }





//                    Picasso.get().load(selectedImage!!).fit().centerCrop().into(ivUserImage )
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                result!!.error!!.printStackTrace()
            }
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
            map_lay.visibility=View.VISIBLE
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
    private fun profDetailsObserver() {
        if(true){
            onObserveStart()
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            apiClient.getApiService(this).doctorProfissionalDetails()
                .enqueue(object : Callback<BaseResponce<List<DaysModel>>> {
                    override fun onFailure(
                        call: Call<BaseResponce<List<DaysModel>>>,
                        t: Throwable
                    ) {
                        alertNetwork(true)
                    }

                    override fun onResponse(
                        call: Call<BaseResponce<List<DaysModel>>>,
                        response: Response<BaseResponce<List<DaysModel>>>
                    ) {
                        if (response!!.isSuccessful) {
                            if (response.body()!!.success) {
                                onObserveSuccess()
                                response.body()!!.data.let {
                                    profDetailsList.addAll(it!!)
                                    it!!.forEach { prof: DaysModel ->
                                        if (preferences!!.getString("language","")=="Arabic"){
                                            profDetailsNamesList.add(prof.name_ar)

                                        }else{
                                            profDetailsNamesList.add(prof.name_en)

                                        }
                                    }
                                    profDetailsSpinnerAdapter.notifyDataSetChanged()

                                }

                            } else {
                                onObservefaled()
                                Toast.makeText(
                                    this@RegisterationActivity,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            onObservefaled()
                            Toast.makeText(this@RegisterationActivity, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }


                })
        }else{
            Toast.makeText(this, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }

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
    private fun SubSpiecObserver() {
        if(true){
            onObserveStart()
            var url= Q.DOC_SUB_SPIC_API
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            apiClient.getApiService(this).fitchSubSpecialitiesList(url)
                .enqueue(object : Callback<BaseResponce<List<SubSpiecialityModel>>> {
                    override fun onFailure(
                        call: Call<BaseResponce<List<SubSpiecialityModel>>>,
                        t: Throwable
                    ) {
                        alertNetwork(true)
                    }

                    override fun onResponse(
                        call: Call<BaseResponce<List<SubSpiecialityModel>>>,
                        response: Response<BaseResponce<List<SubSpiecialityModel>>>
                    ) {
                        if (response!!.isSuccessful) {
                            if (response.body()!!.success) {
                                onObserveSuccess()
                                response.body()!!.data.let {
                                    subSpicList.addAll(it!!)
                                    it!!.forEach { prof: SubSpiecialityModel ->
                                        if (preferences!!.getString("language","")=="Arabic"){
                                            subSpicialityNameList.add(prof.name_ar)
                                        }else{
                                            subSpicialityNameList.add(prof.name_en)

                                        }
                                    }
                                    subSpicialitySpinnerAdapter.notifyDataSetChanged()

                                }

                            } else {
                                onObservefaled()
                                Toast.makeText(
                                    this@RegisterationActivity,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            onObservefaled()
                            Toast.makeText(this@RegisterationActivity, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }


                })
        }else{
            Toast.makeText(this, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }

    }
    private fun spiecObserver() {
        if(true){
            onObserveStart()
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            apiClient.getApiService(this).fitchSpecialitiesList()
                .enqueue(object : Callback<BaseResponce<List<Specialties>>> {
                    override fun onFailure(
                        call: Call<BaseResponce<List<Specialties>>>,
                        t: Throwable
                    ) {
                        alertNetwork(true)
                    }

                    override fun onResponse(
                        call: Call<BaseResponce<List<Specialties>>>,
                        response: Response<BaseResponce<List<Specialties>>>
                    ) {
                        if (response!!.isSuccessful) {
                            if (response.body()!!.success) {
                                onObserveSuccess()
                                response.body()!!.data.let {
                                    SpicList.addAll(it!!)
                                    it!!.forEach { spic: Specialties ->
                                        if (preferences!!.getString("language","")=="Arabic"){
                                            SpicialityNameList.add(spic.name_ar)
                                        }else{
                                            SpicialityNameList.add(spic.name_en)

                                        }
                                    }
                                    SpicialitySpinnerAdapter.notifyDataSetChanged()
                                }

                            } else {
                                onObservefaled()
                                Toast.makeText(
                                    this@RegisterationActivity,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            onObservefaled()
                            Toast.makeText(this@RegisterationActivity, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }


                })
        }else{
            Toast.makeText(this, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }

    }
    private fun perfixObserver(id:Int) {
        if(true){
            onObserveStart()
            var url= Q.DOC_PERFIX_TITLE_BY_SPIC_ID_API+"$id"
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            apiClient.getApiService(this).fitchPerfixList(url)
                .enqueue(object : Callback<BaseResponce<List<SubSpiecialityModel>>> {
                    override fun onFailure(
                        call: Call<BaseResponce<List<SubSpiecialityModel>>>,
                        t: Throwable
                    ) {
                        alertNetwork(true)
                    }

                    override fun onResponse(
                        call: Call<BaseResponce<List<SubSpiecialityModel>>>,
                        response: Response<BaseResponce<List<SubSpiecialityModel>>>
                    ) {
                        if (response!!.isSuccessful) {
                            if (response.body()!!.success) {
                                onObserveSuccess()
                                response.body()!!.data.let {
                                    perfixList.addAll(it!!)
                                    it!!.forEach { prof: SubSpiecialityModel ->
                                        if (preferences!!.getString("language","")=="Arabic"){
                                            perfixNamesList.add(prof.name_ar)
                                        }else{
                                            perfixNamesList.add(prof.name_en)

                                        }
                                    }
                                    perfixSpinnerAdapter.notifyDataSetChanged()

                                }

                            } else {
                                onObservefaled()
                                Toast.makeText(
                                    this@RegisterationActivity,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            onObservefaled()
                            Toast.makeText(this@RegisterationActivity, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }


                })
        }else{
            Toast.makeText(this, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }

    }
    private fun upDateDoctorProfile(){
        onObserveStart()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).DocRegistration(
            doctorValidator!!.password,
            doctorValidator!!.phonenumber,
            doctorValidator!!.email,
            doctorValidator!!.gender_id,
            doctorValidator!!.featured,
            doctorValidator!!.firstName_en,
            doctorValidator!!.firstName_ar,
            doctorValidator!!.lastName_en,
            doctorValidator!!.lastName_ar,
            doctorValidator!!.specialty_id,
            doctorValidator!!.subSpecialties_id,
            doctorValidator!!.prefix_title_id,
            doctorValidator!!.profissionalDetails_id,
            doctorValidator!!.profissionalTitle_en,
            doctorValidator!!.profissionalTitle_ar,
            doctorValidator!!.aboutDoctor_ar,
            doctorValidator!!.aboutDoctor_en,
            doctorValidator!!.practiceLicenseID,
            doctorValidator!!.profissionalTitleID,
            doctorValidator!!.area_id,
            doctorValidator!!.price,
            doctorValidator!!.waiting_time,
            doctorValidator!!.num_of_day,
            doctorValidator!!.address_en,
            doctorValidator!!.address_ar,
            doctorValidator!!.landmark_en,
            doctorValidator!!.landmark_ar,
            doctorValidator!!.lng,
            doctorValidator!!.lat,
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
                                    Intent(this@RegisterationActivity, VerivicationActivity::class.java)
                                intent.putExtra("phone",loginResponse!!.data!!.user.phonenumber.toString())
                                intent.putExtra("type","doctor")
                                startActivity(intent)
                                finish()
                            }
                        } else {

                            onObservefaled()
                            Toast.makeText(this@RegisterationActivity, "تأكد من ادخال جميع البيانات بشكل صحيح", Toast.LENGTH_SHORT).show()

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
        doctorValidator!!.email=email.text.toString()
        doctorValidator!!.password=password.text.toString()
        doctorValidator!!.phonenumber=Q.PHONE_KEY+phon_num.text.toString()
        doctorValidator!!.price=edit_price_txt.text.toString()
        doctorValidator!!.num_of_day=edit_num_of_days_txt.text.toString()
        doctorValidator!!.waiting_time="00:30:00"
        doctorValidator!!.aboutDoctor_ar=edit_about_doc_ar_txt.text.toString()
        doctorValidator!!.aboutDoctor_en=edit_about_doc_en_txt.text.toString()
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
            map_lay.visibility=View.GONE
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






}
