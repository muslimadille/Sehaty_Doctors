package com.muslim_adel.enaya_doctor.modules.newRegistration.pharmacies.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import com.muslim_adel.enaya_doctor.R

import com.muslim_adel.enaya_doctor.modules.newRegistration.pharmacies.PharmRegisterationActivity
import com.muslim_adel.enaya_doctor.remote.apiServices.ApiClient
import com.muslim_adel.enaya_doctor.remote.apiServices.SessionManager
import com.muslim_adel.enaya_doctor.remote.objects.BaseResponce
import com.muslim_adel.enaya_doctor.remote.objects.Reagons
import com.muslim_adel.enaya_doctor.utiles.SpinnerAdapterCustomFont
import kotlinx.android.synthetic.main.fragment_lab_registeration5.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PharmRegisterationFragment4 : Fragment() {

    var mContext: PharmRegisterationActivity?=null
    var vlaidationText=""
    private var regionsList = ArrayList<Reagons>()
    private var regionsNameList = ArrayList<String>()
    private lateinit var regionsSpinnerAdapter: SpinnerAdapterCustomFont
    private var selectedRegionId=0
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pharm_registeration4, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
        regonObserver()
        onSelectLocationClicked()
        onHideMapClicked()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as PharmRegisterationActivity
    }
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity as PharmRegisterationActivity
    }
    fun validateInputData(){
        mContext!!.pharmRegisterationValidator!!.address_ar=edit_sn_txt.text.toString()
        mContext!!.pharmRegisterationValidator!!.address_en=edit_sn_en_txt.text.toString()
        mContext!!.pharmRegisterationValidator!!.landmark_ar=edit_lm_ar_txt.text.toString()
        mContext!!.pharmRegisterationValidator!!.landmark_en=edit_lm_en_txt.text.toString()
        mContext!!.pharmRegisterationValidator!!.lat=mContext!!.lat
        mContext!!.pharmRegisterationValidator!!.lng=mContext!!.lng
        mContext!!.register()

    }
    fun checkValidation():Boolean{
        vlaidationText=""
        var value=true
        if(this.edit_sn_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل العنوان(عربي)"+"\n"

        }
        if(this.edit_sn_en_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل العنوان (english)"+"\n"

        }
        if(this.edit_lm_ar_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل نقطة دالة(عربي)"+"\n"

        }
        if(this.edit_lm_en_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل نقطة دالة(english)"+"\n"

        }


        if(!value){
            Toast.makeText(
                mContext,
                vlaidationText,
                Toast.LENGTH_SHORT
            ).show()
        }
        return value
    }
    private fun onObserveStart() {
        frag5_progrss_lay?.visibility = View.VISIBLE
    }
    private fun onObserveSuccess() {
        frag5_progrss_lay?.visibility = View.GONE
    }
    private fun onObservefaled() {
        frag5_progrss_lay?.visibility = View.GONE
        Toast.makeText(mContext!!, "failed", Toast.LENGTH_SHORT).show()
    }

    private fun regonObserver(){
        onObserveStart()
        apiClient = ApiClient()
        sessionManager = SessionManager(mContext!!)
        apiClient.getApiService(mContext!!).fitchReagonsList()
            .enqueue(object : Callback<BaseResponce<List<Reagons>>> {
                override fun onFailure(call: Call<BaseResponce<List<Reagons>>>, t: Throwable) {
                    mContext!!.alertNetwork(true)
                }
                override fun onResponse(call: Call<BaseResponce<List<Reagons>>>, response: Response<BaseResponce<List<Reagons>>>) {
                    if(response!!.isSuccessful){
                        response.body()!!.data.let {
                            regionsList.addAll(it!!)
                            for (i in 0 until it.size ){
                                if (mContext!!.preferences!!.getString("language","")=="Arabic"){
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

    fun initSpinner(){
        regionsSpinnerAdapter = SpinnerAdapterCustomFont(mContext!!, android.R.layout.simple_spinner_item, regionsNameList)
        regionsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        regionsSpinnerAdapter.textSize = 12
        re_regions_spinner.adapter = regionsSpinnerAdapter
        regionsSpinnerAdapter.notifyDataSetChanged()
        re_regions_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if(regionsList.isNotEmpty()){
                    selectedRegionId=regionsList[position].id
                    mContext!!.pharmRegisterationValidator!!.area_id=regionsList[position].id.toString()
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }
    fun setLocationText(text:String){
        edit_location_txt?.let { it.text=text }
    }
    private fun onSelectLocationClicked(){
        select_location.setOnClickListener {
            mContext!!.showMap()
        }
    }
    private fun onHideMapClicked(){
        hid_map_btn.setOnClickListener {
            mContext!!.hideMap()
        }
    }
   
}