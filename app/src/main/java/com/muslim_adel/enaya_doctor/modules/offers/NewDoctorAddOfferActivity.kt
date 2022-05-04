package com.muslim_adel.enaya_doctor.modules.offers

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.base.BaseActivity
import com.muslim_adel.enaya_doctor.modules.base.GlideObject
import com.muslim_adel.enaya_doctor.modules.home.MainActivity
import com.muslim_adel.enaya_doctor.remote.apiServices.ApiClient
import com.muslim_adel.enaya_doctor.remote.apiServices.SessionManager
import com.muslim_adel.enaya_doctor.remote.objects.*
import com.muslim_adel.enaya_doctor.utiles.IOUtile
import com.muslim_adel.enaya_doctor.utiles.Q
import com.muslim_adel.enaya_doctor.utiles.SpinnerAdapterCustomFont
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_new_doctor_add_offer.*
import kotlinx.android.synthetic.main.activity_new_doctor_add_offer.offer_lay
import kotlinx.android.synthetic.main.activity_new_doctor_add_offer.offer_price_txt
import kotlinx.android.synthetic.main.activity_new_doctor_add_offer.offer_title_en_txt
import kotlinx.android.synthetic.main.activity_new_doctor_add_offer.progrss_lay
import kotlinx.android.synthetic.main.activity_new_doctor_add_offer.services_spinner
import kotlinx.android.synthetic.main.activity_new_doctor_add_offer.sub_survices_spinner
import kotlinx.android.synthetic.main.activity_new_doctor_add_offer.title_ar_txt
import kotlinx.android.synthetic.main.activity_new_doctor_add_offer.unit_spinner
import kotlinx.android.synthetic.main.fragment_lab_profile_edit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewDoctorAddOfferActivity : BaseActivity() {
    private var selectedImage: File? = null
    private var validator=validator("","","","","","","","","","","","","","","","","","","","")

    val calendar= Calendar.getInstance()
    val yearformat = SimpleDateFormat("yyyy", Locale.ENGLISH)
    val monthformat = SimpleDateFormat("MM", Locale.ENGLISH)
    val dayformat = SimpleDateFormat("dd", Locale.ENGLISH)
    val year=yearformat.format(calendar.get(Calendar.YEAR)).toInt()
    val month=monthformat.format(calendar.get(Calendar.MONTH)).toInt()
    val day=dayformat.format(calendar.get(Calendar.DAY_OF_MONTH)).toInt()

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private var categoriesList: MutableList<OffersCategory> = ArrayList()
    private var categoriesListAddapter: CatigoriesAdapter? = null
    private var subCategoriesListAddapter: SubCategoriesAdapter? = null
    private var offerSubcategoryList = ArrayList<OffersSubGategory>()

    private var selectedCategoryIndex=0
    private var selectedSubCategoryIndex=0
    private var selectedServiceIndex=0
    private var selectedSubServiceIndex=0
    private var selectedUnitIndex=0


    private lateinit var categoriesSpinnerAdapter: SpinnerAdapterCustomFont
    private lateinit var subcategoriesSpinnerAdapter: SpinnerAdapterCustomFont
    private lateinit var servicesSpinnerAdapter: SpinnerAdapterCustomFont
    private lateinit var subServicesSpinnerAdapter: SpinnerAdapterCustomFont
    private lateinit var unitsSpinnerAdapter: SpinnerAdapterCustomFont

    private var offerServicesList = ArrayList<OfferServicesModel>()
    private var offerSubServicesList = ArrayList<OfferServicesModel>()
    private var offerUnitsList = ArrayList<OfferUnitsModel>()

    private var offerCatigorisNameList = ArrayList<String>()
    private var offerSubcategoryNameList = ArrayList<String>()
    private var offerServicesNameList = ArrayList<String>()
    private var offerSubServicesNameList = ArrayList<String>()
    private var offerUnitsNameList = ArrayList<String>()
    lateinit var dpd: DatePickerDialog






    var selectedCtegory=-1
    var selectedSubCtegory=-1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Launches photo picker in single-select mode.
        // This means that the user can select one photo or video.
        val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intentGallery.resolveActivity(this.packageManager) != null) {
            // Launch the intent
        }
        setContentView(R.layout.activity_new_doctor_add_offer)
        initRVAdapter()
        categoriesObserver()

        initSpinners()
        implementListeners()
        onAddOfferClicked()
        onSelectIMageClicked()
        setStartDate()
        setEndDate()

    }
    private fun initRVAdapter() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        categories_rv.layoutManager = layoutManager
        categoriesListAddapter= CatigoriesAdapter(this, categoriesList)
        categories_rv.adapter = categoriesListAddapter

        val layoutManager2 = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        subcatigory_rv.layoutManager = layoutManager2
        subCategoriesListAddapter= SubCategoriesAdapter(this, offerSubcategoryList)
        subcatigory_rv.adapter = subCategoriesListAddapter
    }
    private fun categoriesObserver() {
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).doctorOffersCategories()
            .enqueue(object : Callback<BaseResponce<List<OffersCategory>>> {
                override fun onFailure(
                    call: Call<BaseResponce<List<OffersCategory>>>,
                    t: Throwable
                ) {
                    alertNetwork(true)
                }

                override fun onResponse(
                    call: Call<BaseResponce<List<OffersCategory>>>,
                    response: Response<BaseResponce<List<OffersCategory>>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            response.body()!!.data!!.let {
                                if (it.isNotEmpty()) {
                                    categoriesList.addAll(it)
                                    categoriesListAddapter!!.notifyDataSetChanged()
                                } else {
                                    Toast.makeText(this@NewDoctorAddOfferActivity, "empty", Toast.LENGTH_SHORT).show()
                                }

                            }
                        } else {
                            Toast.makeText(this@NewDoctorAddOfferActivity, "faid", Toast.LENGTH_SHORT).show()

                        }

                    } else {
                        Toast.makeText(this@NewDoctorAddOfferActivity, "connect faid", Toast.LENGTH_SHORT).show()

                    }

                }


            })
    }
    private fun OfferSubCategoriesObserver(id:Int) {
        offerSubcategoryList.clear()
        if(true){
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            var url= Q.DOC_OFFER_SUB_CATEGORIES_API+"$id"
            apiClient.getApiService(this).doctorOffersSubCategories(url)
                .enqueue(object : Callback<BaseResponce<List<OffersSubGategory>>> {
                    override fun onFailure(
                        call: Call<BaseResponce<List<OffersSubGategory>>>,
                        t: Throwable
                    ) {
                        alertNetwork(true)
                    }

                    override fun onResponse(
                        call: Call<BaseResponce<List<OffersSubGategory>>>,
                        response: Response<BaseResponce<List<OffersSubGategory>>>
                    ) {
                        if (response!!.isSuccessful) {
                            if (response.body()!!.success) {
                                response.body()!!.data.let {
                                    offerSubcategoryList.addAll(it!!)
                                    subCategoriesListAddapter!!.notifyDataSetChanged()
                                }

                            } else {
                                Toast.makeText(
                                    this@NewDoctorAddOfferActivity,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        } else {
                            Toast.makeText(this@NewDoctorAddOfferActivity, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }


                })
        }else{
            Toast.makeText(this, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }

    }
    fun refreshRecycler(){
        val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        categories_rv.layoutManager = layoutManager
        categoriesListAddapter= CatigoriesAdapter(this, categoriesList)
        categories_rv.adapter = categoriesListAddapter
        selectedSubCtegory=-1
        OfferSubCategoriesObserver(categoriesList[selectedCtegory].id.toInt())
    }
    fun refreshRecycler2(){
        val layoutManager2 = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        subcatigory_rv.layoutManager = layoutManager2
        subCategoriesListAddapter= SubCategoriesAdapter(this, offerSubcategoryList)
        subcatigory_rv.adapter = subCategoriesListAddapter
        OfferServicesObserver(offerSubcategoryList[selectedSubCtegory].id.toInt())

    }

    private fun initSpinners(){

        servicesSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, offerServicesNameList)
        servicesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        servicesSpinnerAdapter.textSize = 12
        servicesSpinnerAdapter.add(getString(R.string.select_service))
        services_spinner.adapter = servicesSpinnerAdapter
        servicesSpinnerAdapter.notifyDataSetChanged()

        subServicesSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, offerSubServicesNameList)
        subServicesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        subServicesSpinnerAdapter.add(getString(R.string.select_sub_service))
        subServicesSpinnerAdapter.textSize = 12
        sub_survices_spinner.adapter = subServicesSpinnerAdapter
        subServicesSpinnerAdapter.notifyDataSetChanged()

        unitsSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, offerUnitsNameList)
        unitsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        unitsSpinnerAdapter.textSize = 12
        unitsSpinnerAdapter.add(getString(R.string.select_unit))
        unit_spinner.adapter = unitsSpinnerAdapter
        unitsSpinnerAdapter.notifyDataSetChanged()
    }
    private fun implementListeners() {
        
        services_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedServiceIndex=position-1
                if(offerServicesList.isNotEmpty()&&position!=0){
                    validator.service_id=offerServicesList[selectedCategoryIndex].id.toString()
                    OfferUnitsObserver(offerServicesList[selectedCategoryIndex].id.toInt())
                    OfferSubServicesObserver(offerServicesList[selectedCategoryIndex].id.toInt())
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        /********************************************************************************/
        unit_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position>0){
                    validator.unit_id=offerUnitsList[position-1].id.toString()

                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        /********************************************************************************/
        sub_survices_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if(position>0){
                    selectedSubServiceIndex=position-1
                    validator.sub_service_id=offerSubServicesList[position-1].id.toString()
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

    }
    private fun onObserveStart() {
        progrss_lay?.visibility = View.VISIBLE
        offer_lay?.visibility = View.GONE
    }

    private fun onObserveSuccess() {
        progrss_lay?.visibility = View.GONE
        offer_lay?.visibility = View.VISIBLE
    }
    private fun onObservefaled() {
        progrss_lay?.visibility = View.VISIBLE
        offer_lay?.visibility = View.GONE
        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun onAddOfferClicked(){
        doc_add_new_offer_btn.setOnClickListener {

            when(preferences!!.getString(Q.USER_TYPE, "")){
                Q.USER_DOCTOR -> {
                    validator.category_id=categoriesList[selectedCtegory].id.toString()
                    validator.sub_category_id=offerSubcategoryList[selectedSubCtegory].id.toString()
                    validator.unit_number=offer_unit_num_txt.text.toString()
                    validator.device_name_ar=devicename_ar_txt.text.toString()
                    validator.device_name_en=devicename_title_en_txt.text.toString()
                    validator.title_ar=title_ar_txt.text.toString()
                    validator.title_en=offer_title_en_txt.text.toString()
                    validator.description_ar=description_ar_txt.text.toString()
                    validator.description_en=description_en_txt.text.toString()
                    validator.price=offer_price_txt.text.toString()
                    validator.discount=offer_discount_txt.text.toString()
                    if (selectedImage != null) {
                        val fileInBytes = IOUtile.readFileAsByteArray(selectedImage!!)
                        validator.featured1 = "data:image/${selectedImage!!.extension};base64,"+toBase64(selectedImage.toString())
                    }
                    addDocOfferObserver()

                }
                Q.USER_LAB -> {
                }
                Q.USER_PHARM -> {

                }

            }



        }
    }
    private fun addDocOfferObserver() {
        var title_ar=title_ar_txt.text.toString()
        var title_en=offer_title_en_txt.text.toString()
        var price=offer_price_txt.text.toString()
        var img=""

        if(title_ar.isNotEmpty()&&title_en.isNotEmpty()&&price.isNotEmpty()){
            apiClient = ApiClient()
            onObserveStart()
            sessionManager = SessionManager(this)
            apiClient.getApiService(this).addDocOffer(validator.featured1,validator.category_id,validator.sub_category_id,
            validator.service_id,validator.sub_service_id,validator.device_name_en,validator.unit_id,validator.unit_number,validator.title_en,
            validator.title_ar,validator.description_en,validator.description_ar,validator.price,validator.discount,validator.date_from,validator.date_to,
            validator.device_name_ar,validator.featured2,validator.featured3,validator.featured4)
                .enqueue(object : Callback<BaseResponce<DocOffer>> {
                    override fun onFailure(
                        call: Call<BaseResponce<DocOffer>>,
                        t: Throwable
                    ) {
                        alertNetwork(true)
                    }

                    override fun onResponse(
                        call: Call<BaseResponce<DocOffer>>,
                        response: Response<BaseResponce<DocOffer>>
                    ) {
                        if (response!!.isSuccessful) {
                            if (response.body()!!.success) {
                                onObserveSuccess()
                                Toast.makeText(
                                    this@NewDoctorAddOfferActivity,
                                    "تم إضافة العرض بنجاح",
                                    Toast.LENGTH_SHORT
                                ).show()
                                intent = Intent(this@NewDoctorAddOfferActivity, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                onObservefaled()

                                Toast.makeText(
                                    this@NewDoctorAddOfferActivity,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        } else {
                            onObservefaled()
                            Toast.makeText(this@NewDoctorAddOfferActivity, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }

                })
        }else{
            Toast.makeText(this, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onSelectIMageClicked(){
        doc_offer_img.setOnClickListener {
            selectImage()
        }
    }
    fun selectImage() {
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
                        .start(this@NewDoctorAddOfferActivity)
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(this@NewDoctorAddOfferActivity,
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
                    selectedImage = File(result!!.uri!!.path!!)

                    GlideObject.GlideProfilePic(this, selectedImage!!.path, doc_offer_img)
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



    private fun OfferServicesObserver(id:Int) {

        if(true){
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            var url=Q.DOC_OFFER_SERVICE_API+"$id"
            apiClient.getApiService(this).doctorOffersServices(url)
                .enqueue(object : Callback<BaseResponce<List<OfferServicesModel>>> {
                    override fun onFailure(
                        call: Call<BaseResponce<List<OfferServicesModel>>>,
                        t: Throwable
                    ) {
                        alertNetwork(true)
                    }

                    override fun onResponse(
                        call: Call<BaseResponce<List<OfferServicesModel>>>,
                        response: Response<BaseResponce<List<OfferServicesModel>>>
                    ) {
                        if (response!!.isSuccessful) {
                            if (response.body()!!.success) {
                                response.body()!!.data.let {
                                    offerServicesList.addAll(it!!)
                                }
                                offerServicesList.forEach { serv:OfferServicesModel->
                                    if (preferences!!.getString("language","")=="Arabic"){
                                        offerServicesNameList.add(serv.name_ar)
                                    }else{
                                        offerServicesNameList.add(serv.name_en)
                                    }
                                }
                                servicesSpinnerAdapter.notifyDataSetChanged()


                            } else {
                                Toast.makeText(
                                    this@NewDoctorAddOfferActivity,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        } else {
                            Toast.makeText(this@NewDoctorAddOfferActivity, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }


                })
        }else{
            Toast.makeText(this, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }

    }
    private fun OfferSubServicesObserver(id:Int) {

        if(true){
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            var url=Q.DOC_OFFER_SUB_SERVICE_API+"$id"
            apiClient.getApiService(this).doctorOffersSubServices(url)
                .enqueue(object : Callback<BaseResponce<List<OfferServicesModel>>> {
                    override fun onFailure(
                        call: Call<BaseResponce<List<OfferServicesModel>>>,
                        t: Throwable
                    ) {
                        alertNetwork(true)
                    }

                    override fun onResponse(
                        call: Call<BaseResponce<List<OfferServicesModel>>>,
                        response: Response<BaseResponce<List<OfferServicesModel>>>
                    ) {
                        if (response!!.isSuccessful) {
                            if (response.body()!!.success) {
                                response.body()!!.data.let {
                                    offerSubServicesList.addAll(it!!)
                                }
                                offerSubServicesList.forEach { serv:OfferServicesModel->
                                    if (preferences!!.getString("language","")=="Arabic"){
                                        offerSubServicesNameList.add(serv.name_ar)
                                    }else{
                                        offerSubServicesNameList.add(serv.name_en)
                                    }
                                }
                                subServicesSpinnerAdapter.notifyDataSetChanged()


                            } else {
                                Toast.makeText(
                                    this@NewDoctorAddOfferActivity,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        } else {
                            Toast.makeText(this@NewDoctorAddOfferActivity, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }


                })
        }else{
            Toast.makeText(this, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }

    }
    private fun OfferUnitsObserver(id:Int) {

        if(true){
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            var url=Q.DOC_OFFER_UNITS_API+"$id"
            apiClient.getApiService(this).doctorOfferUnits(url)
                .enqueue(object : Callback<BaseResponce<List<OfferUnitsModel>>> {
                    override fun onFailure(
                        call: Call<BaseResponce<List<OfferUnitsModel>>>,
                        t: Throwable
                    ) {
                        alertNetwork(true)
                    }

                    override fun onResponse(
                        call: Call<BaseResponce<List<OfferUnitsModel>>>,
                        response: Response<BaseResponce<List<OfferUnitsModel>>>
                    ) {
                        if (response!!.isSuccessful) {
                            if (response.body()!!.success) {
                                response.body()!!.data.let {
                                    offerUnitsList.addAll(it!!)
                                }
                                offerUnitsList.forEach { unit:OfferUnitsModel->
                                    if (preferences!!.getString("language","")=="Arabic"){
                                        offerUnitsNameList.add(unit.name_ar)
                                    }else{
                                        offerUnitsNameList.add(unit.name_en)
                                    }
                                }
                                unitsSpinnerAdapter.notifyDataSetChanged()


                            } else {
                                Toast.makeText(
                                    this@NewDoctorAddOfferActivity,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        } else {
                            Toast.makeText(this@NewDoctorAddOfferActivity, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }


                })
        }else{
            Toast.makeText(this, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setStartDate(){
        var calendar= Calendar.getInstance()

        var year=calendar.get(Calendar.YEAR)
        var month=calendar.get(Calendar.MONTH)
        var day=calendar.get(Calendar.DAY_OF_MONTH)
        offer_date_from_txt.setOnClickListener {
             dpd= DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, myear, mMonth, mdayOfMonth ->

                    var month = ""
                    var day = ""
                    if (mMonth < 10) {
                        month = "0${mMonth+1}"
                    } else {
                        month = "${mMonth+1}"
                    }
                    if (mdayOfMonth < 10) {
                        day = "0$mdayOfMonth"
                    } else {
                        day = "$mdayOfMonth"
                    }
                    var selectedDate = "$myear-$month-$day"
                    val selectedate = SimpleDateFormat("yyyy-MM-dd").parse(selectedDate)
                    var dayname=SimpleDateFormat("EEEE").format(selectedate)
                    validator!!.date_from=selectedDate
                    offer_date_from_txt.text="$dayname ${selectedDate}"

                }, year, month, day
            )
            dpd.show()

        }
    }
    private fun setEndDate(){
        var calendar= Calendar.getInstance()
        var year=calendar.get(Calendar.YEAR)
        var month=calendar.get(Calendar.MONTH)
        var day=calendar.get(Calendar.DAY_OF_MONTH)
        offer_date_to_txt.setOnClickListener {
            val dpd= DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, myear, mMonth, mdayOfMonth ->

                    var month = ""
                    var day = ""
                    if (mMonth < 10) {
                        month = "0${mMonth+1}"
                    } else {
                        month = "${mMonth+1}"
                    }
                    if (mdayOfMonth < 10) {
                        day = "0$mdayOfMonth"
                    } else {
                        day = "$mdayOfMonth"
                    }
                    var selectedDate = "$myear-$month-$day"
                    val selectedate = SimpleDateFormat("yyyy-MM-dd").parse(selectedDate)
                    var dayname=SimpleDateFormat("EEEE").format(selectedate)
                    validator!!.date_to=selectedDate
                    offer_date_to_txt.text="$dayname ${selectedDate}"

                }, year, month, day
            )
            dpd.show()

        }
    }
}
data class validator(
      var category_id:String,
     var sub_category_id:String,
             var service_id:String,
             var sub_service_id:String,
             var device_name_en:String,
             var device_name_ar:String,
             var unit_id:String,
             var unit_number:String,
             var title_en:String,
             var title_ar:String,
             var description_en:String,
             var description_ar:String,
             var price:String,
             var discount:String,
             var date_from:String,
             var date_to:String,
             var featured1:String,
      var featured2:String,
      var featured3:String,
      var featured4:String,


      )