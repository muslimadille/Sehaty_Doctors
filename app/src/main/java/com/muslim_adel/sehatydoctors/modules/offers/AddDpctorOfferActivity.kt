package com.muslim_adel.sehatydoctors.modules.offers

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.SpinnerAdapter
import android.widget.Toast
import com.muslim_adel.sehatydoctors.R
import com.muslim_adel.sehatydoctors.modules.base.BaseActivity
import com.muslim_adel.sehatydoctors.modules.home.MainActivity
import com.muslim_adel.sehatydoctors.remote.apiServices.ApiClient
import com.muslim_adel.sehatydoctors.remote.apiServices.SessionManager
import com.muslim_adel.sehatydoctors.remote.objects.*
import com.muslim_adel.sehatydoctors.utiles.Q
import com.muslim_adel.sehatydoctors.utiles.SpinnerAdapterCustomFont
import kotlinx.android.synthetic.main.activity_add_dpctor_offer.*
import kotlinx.android.synthetic.main.activity_add_dpctor_offer.add_offer_btn
import kotlinx.android.synthetic.main.activity_add_dpctor_offer.offer_img
import kotlinx.android.synthetic.main.activity_add_dpctor_offer.offer_price_txt
import kotlinx.android.synthetic.main.activity_add_dpctor_offer.offer_title_en_txt
import kotlinx.android.synthetic.main.activity_add_dpctor_offer.title_ar_txt
import kotlinx.android.synthetic.main.activity_add_new_offer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddDpctorOfferActivity : BaseActivity() {
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

    private var offerCatigorisList = ArrayList<OffersCategory>()
    private var offerSubcategoryList = ArrayList<OffersSubGategory>()
    private var offerServicesList = ArrayList<OfferServicesModel>()
    private var offerSubServicesList = ArrayList<OfferServicesModel>()
    private var offerUnitsList = ArrayList<OfferUnitsModel>()

    private var offerCatigorisNameList = ArrayList<String>()
    private var offerSubcategoryNameList = ArrayList<String>()
    private var offerServicesNameList = ArrayList<String>()
    private var offerSubServicesNameList = ArrayList<String>()
    private var offerUnitsNameList = ArrayList<String>()



    private  val REQUEST_CODE = 13

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dpctor_offer)
        initSpinners()
        OfferCategoriesObserver()
        implementListeners()
        onAddOfferClicked()
        onSelectIMageClicked()
    }
    private fun initSpinners(){
        categoriesSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, offerCatigorisNameList)
        categoriesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoriesSpinnerAdapter.textSize = 12
        categoriesSpinnerAdapter.add(getString(R.string.select_category))
        category_spinner.adapter = categoriesSpinnerAdapter
        categoriesSpinnerAdapter.notifyDataSetChanged()


        subcategoriesSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, offerSubcategoryNameList)
        subcategoriesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        subcategoriesSpinnerAdapter.textSize = 12
        subcategoriesSpinnerAdapter.add(getString(R.string.select_sub_category))
        sub_category_spinner.adapter = subcategoriesSpinnerAdapter
        subcategoriesSpinnerAdapter.notifyDataSetChanged()

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

        category_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedCategoryIndex=position-1
                if(offerCatigorisList.isNotEmpty()&&position!=0){
                    OfferSubCategoriesObserver(offerCatigorisList[selectedCategoryIndex].id.toInt())
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        /********************************************************************************/
        sub_category_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedSubCategoryIndex=position-1
                if(offerSubcategoryList.isNotEmpty()&&position!=0){
                    OfferServicesObserver(offerSubcategoryList[selectedCategoryIndex].id.toInt())
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        /********************************************************************************/
        services_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedServiceIndex=position-1
                if(offerServicesList.isNotEmpty()&&position!=0){
                    OfferUnitsObserver(offerServicesList[selectedCategoryIndex].id.toInt())
                    OfferSubServicesObserver(offerServicesList[selectedCategoryIndex].id.toInt())
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        /********************************************************************************/
        sub_survices_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedSubServiceIndex=position-1
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

    }
    private fun onAddOfferClicked(){
        add_offer_btn.setOnClickListener {
            when(preferences!!.getString(Q.USER_TYPE, "")){
                Q.USER_DOCTOR -> {
                }
                Q.USER_LAB -> {
                }
                Q.USER_PHARM -> {
                    addPharmOfferObserver()

                }

            }



        }
    }
    private fun addPharmOfferObserver() {
        var title_ar=title_ar_txt.text.toString()
        var title_en=offer_title_en_txt.text.toString()
        var price=offer_price_txt.text.toString()
        var img=""

        if(title_ar.isNotEmpty()&&title_en.isNotEmpty()&&price.isNotEmpty()){
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            apiClient.getApiService(this).addPharmOffer(title_ar, title_en, price, img)
                .enqueue(object : Callback<BaseResponce<PharmAddOfferModel>> {
                    override fun onFailure(
                        call: Call<BaseResponce<PharmAddOfferModel>>,
                        t: Throwable
                    ) {
                        alertNetwork(true)
                    }

                    override fun onResponse(
                        call: Call<BaseResponce<PharmAddOfferModel>>,
                        response: Response<BaseResponce<PharmAddOfferModel>>
                    ) {
                        if (response!!.isSuccessful) {
                            if (response.body()!!.success) {
                                Toast.makeText(
                                    this@AddDpctorOfferActivity,
                                    "SUCCESS",
                                    Toast.LENGTH_SHORT
                                ).show()
                                intent = Intent(this@AddDpctorOfferActivity, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this@AddDpctorOfferActivity,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        } else {
                            Toast.makeText(this@AddDpctorOfferActivity, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }


                })
        }else{
            Toast.makeText(this, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }

    }
    private fun onSelectIMageClicked(){
        offer_img.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else{
                    chooseImageGallery();
                }
            }else{
                chooseImageGallery();
            }
        }
    }
    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_CHOOSE)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    chooseImageGallery()
                }else{
                    Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            offer_img.setImageURI(data?.data)
        }
    }
    // companion object
    companion object {
        private val IMAGE_CHOOSE = 1000;
        private val PERMISSION_CODE = 1001;
    }

    private fun OfferCategoriesObserver() {


        if(true){
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
                                response.body()!!.data.let {
                                    offerCatigorisList.addAll(it!!)
                                }
                                offerCatigorisList.forEach { cat:OffersCategory->
                                    if (preferences!!.getString("language","")=="Arabic"){
                                        offerCatigorisNameList.add(cat.name_ar)
                                    }else{
                                        offerCatigorisNameList.add(cat.name_en)
                                    }
                                }
                                categoriesSpinnerAdapter.notifyDataSetChanged()


                            } else {
                                Toast.makeText(
                                    this@AddDpctorOfferActivity,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        } else {
                            Toast.makeText(this@AddDpctorOfferActivity, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }


                })
        }else{
            Toast.makeText(this, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }

    }
    private fun OfferSubCategoriesObserver(id:Int) {

        if(true){
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            var url=Q.DOC_OFFER_SUB_CATEGORIES_API+"$id"
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
                                }
                                offerSubcategoryList.forEach { cat:OffersSubGategory->
                                    if (preferences!!.getString("language","")=="Arabic"){
                                        offerSubcategoryNameList.add(cat.name_ar)
                                    }else{
                                        offerSubcategoryNameList.add(cat.name_en)
                                    }
                                }
                                subcategoriesSpinnerAdapter.notifyDataSetChanged()


                            } else {
                                Toast.makeText(
                                    this@AddDpctorOfferActivity,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        } else {
                            Toast.makeText(this@AddDpctorOfferActivity, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }


                })
        }else{
            Toast.makeText(this, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }

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
                                    this@AddDpctorOfferActivity,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        } else {
                            Toast.makeText(this@AddDpctorOfferActivity, "faild", Toast.LENGTH_SHORT)
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
                                    this@AddDpctorOfferActivity,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        } else {
                            Toast.makeText(this@AddDpctorOfferActivity, "faild", Toast.LENGTH_SHORT)
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
                                    this@AddDpctorOfferActivity,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        } else {
                            Toast.makeText(this@AddDpctorOfferActivity, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }


                })
        }else{
            Toast.makeText(this, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }

    }
    private fun validator(){
        
    }

}