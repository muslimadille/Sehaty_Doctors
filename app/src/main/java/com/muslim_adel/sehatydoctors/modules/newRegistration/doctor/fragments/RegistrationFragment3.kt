package com.muslim_adel.sehatydoctors.modules.newRegistration.doctor.fragments

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.muslim_adel.sehatydoctors.modules.newRegistration.doctor.DoctorRegistrationScreen
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.remote.apiServices.ApiClient
import com.seha_khanah_doctors.remote.apiServices.SessionManager
import com.seha_khanah_doctors.remote.objects.BaseResponce
import com.seha_khanah_doctors.remote.objects.Reagons
import com.seha_khanah_doctors.remote.objects.Specialties
import com.seha_khanah_doctors.remote.objects.doctor.DaysModel
import com.seha_khanah_doctors.remote.objects.doctor.SubSpiecialityModel
import com.seha_khanah_doctors.utiles.Q
import com.seha_khanah_doctors.utiles.SpinnerAdapterCustomFont
import kotlinx.android.synthetic.main.fragment_registration3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Error

class RegistrationFragment3 : Fragment() {
    var mContext: DoctorRegistrationScreen?=null
    lateinit var profDetailsSpinnerAdapter: SpinnerAdapterCustomFont
     var profDetailsNamesList = ArrayList<String>()
     var profDetailsList = ArrayList<DaysModel>()
    var profDetailsIndex=0;
     lateinit var perfixSpinnerAdapter: SpinnerAdapterCustomFont
     var perfixNamesList = ArrayList<String>()
     var perfixList = ArrayList<SubSpiecialityModel>()
    var perfixIndex=0;


     lateinit var SpicialitySpinnerAdapter: SpinnerAdapterCustomFont
     var SpicialityNameList = ArrayList<String>()
     var SpicList = ArrayList<Specialties>()
    var spicialityIndex=0

    private lateinit var subSpicialitySpinnerAdapter: SpinnerAdapterCustomFont
    private var subSpicialityNameList = ArrayList<String>()
    private var subSpicList = ArrayList<SubSpiecialityModel>()
    var subSpicIndex=0;

    var vlaidationText=""
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private var selectedSubSpic=ArrayList<String>()




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        intiSpinners()
        SubSpiecObserver()
        spiecObserver()
        profDetailsObserver()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration3, container, false)


    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as DoctorRegistrationScreen
    }
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity as DoctorRegistrationScreen
    }

    fun validateInputData(){
        mContext!!.doctorRegistrationModel!!.aboutDoctor_ar=edit_about_doc_ar_txt.text.toString()
        mContext!!.doctorRegistrationModel!!.aboutDoctor_en=edit_about_doc_en_txt.text.toString()

    }
    fun checkValidation():Boolean{
        vlaidationText=""
        var value=true
        if(this.edit_about_doc_ar_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل بيانات الدكتور(عربي)"+"\n"

        }
        if(this.edit_about_doc_en_txt.text.toString().isEmpty()){
            value=false
            vlaidationText=vlaidationText+"أدخل بيانات الدكتور  english"+"\n"

        }
        if(spicialityIndex<1){
            value=false
            vlaidationText=vlaidationText+"أختر التخصص"+"\n"
        }
        if(subSpicIndex<1){
            value=false
            vlaidationText=vlaidationText+"أختر التخصص الفرعي"+"\n"
        }

        if(profDetailsIndex<1){
            value=false
            vlaidationText=vlaidationText+"أختر الدرجة"+"\n"
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
    fun intiSpinners(){
        SpicialityNameList.add("التخصص")
        SpicialitySpinnerAdapter= SpinnerAdapterCustomFont(mContext!!, android.R.layout.simple_spinner_item, SpicialityNameList)
        SpicialitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        SpicialitySpinnerAdapter.textSize = 12
        speciality_spinner.adapter = SpicialitySpinnerAdapter

        subSpicialityNameList.add("التخصص الفرعي")
        subSpicialitySpinnerAdapter= SpinnerAdapterCustomFont(mContext!!, android.R.layout.simple_spinner_item, subSpicialityNameList)
        subSpicialitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        subSpicialitySpinnerAdapter.textSize = 12
        sub_speciality_spinner.adapter = subSpicialitySpinnerAdapter


        perfixNamesList.add("اللقب")
        perfixSpinnerAdapter = SpinnerAdapterCustomFont(mContext!!, android.R.layout.simple_spinner_item, perfixNamesList)
        perfixSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        perfixSpinnerAdapter.textSize = 12
        re_perfix_title_spinner.adapter = perfixSpinnerAdapter


        profDetailsNamesList.add("التفاصيل المهنية")
        profDetailsSpinnerAdapter = SpinnerAdapterCustomFont(mContext!!, android.R.layout.simple_spinner_item, profDetailsNamesList)
        profDetailsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        profDetailsSpinnerAdapter.textSize = 12
        doc_re_prof_details__spinner.adapter = profDetailsSpinnerAdapter


        speciality_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if(SpicList.isNotEmpty()&&position>0){
                    mContext!!.doctorRegistrationModel!!.specialty_id=SpicList[position-1].id.toString()
                    perfixList.clear()
                    perfixNamesList.clear()
                    perfixObserver(SpicList[position-1].id)
                    spicialityIndex=position

                }

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        /**-------------------------------------------------------------------------------------------------*/
        sub_speciality_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if(subSpicList.isNotEmpty()&&position>0){
                    selectedSubSpic.clear()
                    selectedSubSpic.add(subSpicList[position-1].id.toString())
                    mContext!!.doctorRegistrationModel!!.subSpecialties_id=selectedSubSpic
                }
                subSpicIndex=position

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        /**-------------------------------------------------------------------------------------------------*/
        re_perfix_title_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if(perfixList.isNotEmpty()&&position>0){
                    mContext!!.doctorRegistrationModel!!.prefix_title_id=perfixList[position-1].id.toString()
                    perfixIndex=position
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        /**-------------------------------------------------------------------------------------------------*/
        doc_re_prof_details__spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if(profDetailsList.isNotEmpty()&&position>0){
                    mContext!!.doctorRegistrationModel!!.profissionalDetails_id=profDetailsList[position-1].id.toString()
                    mContext!!.doctorRegistrationModel!!.profissionalTitle_ar=profDetailsList[position-1].name_ar
                    mContext!!.doctorRegistrationModel!!.profissionalTitle_en=profDetailsList[position-1].name_en
                    profDetailsIndex=position
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun onObserveStart() {
        frag3_progrss_lay?.visibility = View.VISIBLE
    }
    private fun onObserveSuccess() {
        frag3_progrss_lay?.visibility = View.GONE
    }
    private fun onObservefaled() {
        frag3_progrss_lay?.visibility = View.GONE
        Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show()
    }
    private fun profDetailsObserver() {
        if(true){
            onObserveStart()
            apiClient = ApiClient()
            sessionManager = SessionManager(mContext!!)
            apiClient.getApiService(mContext!!).doctorProfissionalDetails()
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
                                        if (mContext!!.preferences!!.getString("language","")=="Arabic"){
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
                                    mContext!!,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            onObservefaled()
                            Toast.makeText(mContext!!, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }


                })
        }else{
            Toast.makeText(mContext!!, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }

    }

    private fun SubSpiecObserver() {
        if(true){
            onObserveStart()
            var url= Q.DOC_SUB_SPIC_API
            apiClient = ApiClient()
            sessionManager = SessionManager(mContext!!)
            apiClient.getApiService(mContext!!).fitchSubSpecialitiesList(url)
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
                                        if (mContext!!.preferences!!.getString("language","")=="Arabic"){
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
                                    mContext!!,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            onObservefaled()
                            Toast.makeText(mContext!!, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }


                })
        }else{
            Toast.makeText(mContext!!, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }

    }
    private fun spiecObserver() {
        if(true){
            onObserveStart()
            apiClient = ApiClient()
            sessionManager = SessionManager(mContext!!)
            apiClient.getApiService(mContext!!).fitchSpecialitiesList()
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
                                        if (mContext!!.preferences!!.getString("language","")=="Arabic"){
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
                                    mContext!!,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            onObservefaled()
                            Toast.makeText(mContext!!, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }


                })
        }else{
            Toast.makeText(mContext!!, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }

    }
    private fun perfixObserver(id:Int) {
        if(true){
            onObserveStart()
            var url= Q.DOC_PERFIX_TITLE_BY_SPIC_ID_API+"$id"
            apiClient = ApiClient()
            sessionManager = SessionManager(mContext!!)
            apiClient.getApiService(mContext!!).fitchPerfixList(url)
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
                                        if (mContext!!.preferences!!.getString("language","")=="Arabic"){
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
                                    mContext!!,
                                    "faild",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            onObservefaled()
                            Toast.makeText(mContext!!, "faild", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }


                })
        }else{
            Toast.makeText(mContext!!, "الرجاء أدخل بيانات صحيحة", Toast.LENGTH_SHORT).show()
        }

    }
    open fun alertNetwork(isExit: Boolean = false) {
        val alertBuilder = AlertDialog.Builder(mContext!!)
        //alertBuilder.setTitle(R.string.error)
        alertBuilder.setMessage(R.string.no_internet)
        if (isExit) {
            alertBuilder.setPositiveButton(R.string.exit) { dialog: DialogInterface, _: Int -> mContext!!.finish() }
        } else {
            alertBuilder.setPositiveButton(R.string.dismiss) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        }
        try {
            if(!mContext!!.isFinishing){
                alertBuilder.show()
            }

        }catch (e: Error){}
    }


}