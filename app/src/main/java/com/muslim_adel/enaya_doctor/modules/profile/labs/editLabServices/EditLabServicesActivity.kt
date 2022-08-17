package com.muslim_adel.enaya_doctor.modules.profile.labs.editLabServices

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muslim_adel.enaya_doctor.R
import com.muslim_adel.enaya_doctor.modules.base.BaseActivity
import com.muslim_adel.enaya_doctor.remote.apiServices.ApiClient
import com.muslim_adel.enaya_doctor.remote.apiServices.SessionManager
import com.muslim_adel.enaya_doctor.remote.objects.BaseResponce
import com.muslim_adel.enaya_doctor.remote.objects.Laboratory
import com.muslim_adel.enaya_doctor.remote.objects.LaboratoryServices
import com.muslim_adel.enaya_doctor.utiles.Q
import kotlinx.android.synthetic.main.activity_edit_lab_services.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditLabServicesActivity : BaseActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private var labServicesList:MutableList<LaboratoryServices> = ArrayList()
    private var LabServicesListAddapter: LabServicesAdapter?=null
    private var servicesAr=ArrayList<String?>()
    private var servicesEn=ArrayList<String?>()


    var dateId=0
    var labId=0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_lab_services)
        initRVAdapter()
        servicesObserver()
        onSaveClicked()
        onAddClicked()
    }
    private fun initRVAdapter(){
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        services_rv.layoutManager = layoutManager
        LabServicesListAddapter = LabServicesAdapter(this, labServicesList,dateId,labId)
        services_rv.adapter = LabServicesListAddapter
    }
    private fun servicesObserver(){
        val id =intent.getLongExtra("labId",-1)
        val url = Q.GET_LAB_BY_ID_API +"/${id}"
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        onObserveStart()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        apiClient.getApiService(this).fitchLabById(url)
            .enqueue(object : Callback<BaseResponce<Laboratory>> {
                override fun onFailure(call: Call<BaseResponce<Laboratory>>, t: Throwable) {
                    alertNetwork(true)
                }
                override fun onResponse(call: Call<BaseResponce<Laboratory>>, response: Response<BaseResponce<Laboratory>>) {
                    if(response!!.isSuccessful){
                        response.body()!!.data!!.laboratory_services.let {
                            labServicesList.addAll(it!!)
                            LabServicesListAddapter!!.notifyDataSetChanged()
                            onObserveSuccess()
                        }
                    }else{
                        onObservefaled()
                    }

                }

            })
    }
    private fun onObserveStart(){
        sevices_progrss_lay.visibility= View.VISIBLE
        services_rv.visibility= View.GONE
    }
    private fun onObserveSuccess(){
        sevices_progrss_lay.visibility= View.GONE
        services_rv.visibility= View.VISIBLE
    }
    private fun onObservefaled(){
        sevices_progrss_lay.visibility= View.GONE
        services_rv.visibility= View.GONE
    }

    private fun onSaveClicked(){
        update_lab_services_btn.setOnClickListener {
          // call add api
            getStringsList()
        }
    }
    private fun getStringsList(){
        labServicesList.forEach {
            servicesEn.add(it.name_en)
            servicesAr.add(it.name_ar)
        }
        updateServices()
    }
    private fun onAddClicked(){
        add_service_name_btn.setOnClickListener {
            if(service_name_en.text.toString().isNotEmpty()&&service_name_ar.text.toString().isNotEmpty()){
                var laboratoryServices=LaboratoryServices(-1,service_name_en.text.toString(),service_name_ar.text.toString(),"-1")
                labServicesList.add(laboratoryServices)
                LabServicesListAddapter!!.notifyDataSetChanged()
            }else{
                Toast.makeText(this, " ادخل بيانات الخدمة", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun updateServices(){
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        onObserveStart()
        apiClient.getApiService(this).updateLabServices(this.servicesEn,this.servicesAr)
            .enqueue(object : Callback<BaseResponce<Any>> {
                override fun onFailure(call: Call<BaseResponce<Any>>, t: Throwable) {
                    alertNetwork(true)
                }
                override fun onResponse(call: Call<BaseResponce<Any>>, response: Response<BaseResponce<Any>>) {
                    if(response!!.isSuccessful){
                        onObserveSuccess()
                        finish()
                    }else{
                        onObservefaled()
                    }

                }

            })
    }


}