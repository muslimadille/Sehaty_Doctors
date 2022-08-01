package com.muslim_adel.enaya_doctor.modules.selectCuntry

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.muslim_adel.enaya_doctor.remote.objects.CountryModel
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.base.BaseActivity
import com.muslim_adel.enaya_doctor.modules.registration.LoginActivity
import com.muslim_adel.enaya_doctor.remote.apiServices.ApiClient
import com.muslim_adel.enaya_doctor.remote.apiServices.SessionManager
import com.muslim_adel.enaya_doctor.remote.objects.BaseResponce
import com.muslim_adel.enaya_doctor.utiles.Q
import com.muslim_adel.enaya_doctor.utiles.SpinnerAdapterCustomFont
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_new_doctor_add_offer.*
import kotlinx.android.synthetic.main.activity_select_country.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class SelectCountryActivity : BaseActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    //spinner adapter
    private lateinit var countriesSpinnerAdapter: SpinnerAdapterCustomFont
    private var selectedCountriesIndex=0
    private var countriesNamesList=ArrayList<String>()
    private lateinit var languageSpinnerAdapter: SpinnerAdapterCustomFont
    private var languageNamesList=ArrayList<String>()
    private var countriesList= ArrayList<CountryModel>()

    private var selectedLanguageIndex=0
    private var key=0
    private var change=""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_country)
        key=intent.getIntExtra("key",0)
        languageNamesList.add("العربية")
        languageNamesList.add("كوردي")
        languageNamesList.add("English")
        getCountries()
        initSpinnersAdapter()
        onNextclicked()
    }
    private fun onObserveStart() {
        countries_progrss_lay.visibility = View.VISIBLE
    }

    private fun onObserveSuccess() {
        countries_progrss_lay.visibility = View.GONE
    }

    private fun onObservefaled() {
        countries_progrss_lay.visibility = View.GONE
    }
    fun getCountries(){
            onObserveStart()
            apiClient = ApiClient()
            sessionManager = SessionManager(this)
            apiClient.getApiService(this)
                .getAllCountriesList()
                .enqueue(object : Callback<BaseResponce<List<CountryModel>>> {
                    override fun onFailure(call: Call<BaseResponce<List<CountryModel>>>, t: Throwable) {
                        alertNetwork(true)
                    }

                    override fun onResponse(
                        call: Call<BaseResponce<List<CountryModel>>>,
                        response: Response<BaseResponce<List<CountryModel>>>
                    ) {
                        val myResponse = response.body()
                        if (myResponse!!.success) {
                            onObserveSuccess()
                            Q.countriesList.clear()
                            Q.countriesList.addAll(myResponse.data!!)
                            Q.countriesList.forEach{
                                if (preferences!!.getString("language","")=="English"){
                                    countriesNamesList.add(it.nameEn!!)
                                }else{
                                    countriesNamesList.add(it.nameAr!!)
                                }

                            }
                            countriesList.addAll(myResponse.data!!)
                            Q.countriesList.clear()
                            Q.countriesList.addAll(myResponse.data!!)
                            Q.selectedCountry=Q.countriesList[0]
                            preferences!!.putInteger("COUNTRY_ID",Q.selectedCountry.id!!)
                            preferences!!.commit()
                            countriesSpinnerAdapter.notifyDataSetChanged()
                        } else {
                            onObservefaled()

                            Toast.makeText(
                                this@SelectCountryActivity,
                                "Error:${myResponse.data}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }


                })
    }
    private fun initSpinnersAdapter() {
        countriesSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, countriesNamesList)
        countriesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countriesSpinnerAdapter.textSize = 12
        countries_spinner.adapter = countriesSpinnerAdapter
        countriesSpinnerAdapter.notifyDataSetChanged()
        countries_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedCountriesIndex=position
                Q.selectedCountry=Q.countriesList[position]

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        //--------------------------------------------------------------------------------------------
        languageSpinnerAdapter = SpinnerAdapterCustomFont(this, android.R.layout.simple_spinner_item, languageNamesList)
        languageSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinnerAdapter.textSize = 12
        languages_spinner.adapter = languageSpinnerAdapter
        languageSpinnerAdapter.notifyDataSetChanged()
        languages_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedLanguageIndex=position
                when(position){
                    0->{
                        preferences!!.putString("language", "Arabic")
                        preferences!!.commit()
                    }
                    1->{

                        preferences!!.putString("language", "Kurdish")
                        preferences!!.commit()
                    }
                    2->{
                        preferences!!.putString("language", "English")
                        preferences!!.commit()
                    }
                }
                setLocalization()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }
    private fun onNextclicked(){

        countries_next_btn.setOnClickListener {
            val intent = Intent(this@SelectCountryActivity, LoginActivity::class.java)
            intent.putExtra("key",key)
            startActivity(intent)

        }
    }
    private fun setLocalization(){
        val language = preferences!!.getString("language", "en")
        if (language =="Arabic") {
            change="ar"
            Q.CURRENT_LANG="ar"
        } else if (language=="English" ) {
            change = "en"
            Q.CURRENT_LANG="en"
        }else if(language=="Kurdish") {
            change ="ur"
            Q.CURRENT_LANG="ur"
        }
        dLocale = Locale(change) //set any locale you want here

    }
}