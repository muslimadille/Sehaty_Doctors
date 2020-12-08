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
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muslim_adel.sehatydoctors.R
import com.muslim_adel.sehatydoctors.modules.base.BaseActivity
import com.muslim_adel.sehatydoctors.modules.home.MainActivity
import com.muslim_adel.sehatydoctors.remote.apiServices.ApiClient
import com.muslim_adel.sehatydoctors.remote.apiServices.SessionManager
import com.muslim_adel.sehatydoctors.remote.objects.*
import com.muslim_adel.sehatydoctors.utiles.Q
import com.muslim_adel.sehatydoctors.utiles.SpinnerAdapterCustomFont
import kotlinx.android.synthetic.main.activity_add_dpctor_offer.*
import kotlinx.android.synthetic.main.activity_new_doctor_add_offer.*
import kotlinx.android.synthetic.main.activity_new_doctor_add_offer.offer_img
import kotlinx.android.synthetic.main.activity_new_doctor_add_offer.offer_price_txt
import kotlinx.android.synthetic.main.activity_new_doctor_add_offer.offer_title_en_txt
import kotlinx.android.synthetic.main.activity_new_doctor_add_offer.services_spinner
import kotlinx.android.synthetic.main.activity_new_doctor_add_offer.sub_survices_spinner
import kotlinx.android.synthetic.main.activity_new_doctor_add_offer.title_ar_txt
import kotlinx.android.synthetic.main.activity_new_doctor_add_offer.unit_spinner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewDoctorAddOfferActivity : BaseActivity() {
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



    private  val REQUEST_CODE = 13


    var selectedCtegory=-1
    var selectedSubCtegory=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_doctor_add_offer)
        initRVAdapter()
        categoriesObserver()

        initSpinners()
        implementListeners()
        onAddOfferClicked()
        onSelectIMageClicked()

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
        add_new_offer_btn.setOnClickListener {
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
                                    this@NewDoctorAddOfferActivity,
                                    "SUCCESS",
                                    Toast.LENGTH_SHORT
                                ).show()
                                intent = Intent(this@NewDoctorAddOfferActivity, MainActivity::class.java)
                                startActivity(intent)
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
    private fun validator(){

    }
}