package com.seha_khanah_doctors.modules.profile.labs

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.seha_khanah_doctors.R
import com.seha_khanah_doctors.modules.home.MainActivity
import com.seha_khanah_doctors.remote.apiServices.ApiClient
import com.seha_khanah_doctors.remote.apiServices.SessionManager
import com.seha_khanah_doctors.remote.objects.BaseResponce
import com.seha_khanah_doctors.remote.objects.Laboratory
import com.seha_khanah_doctors.utiles.Q
import kotlinx.android.synthetic.main.fragment_lab_profile.*
import kotlinx.android.synthetic.main.fragment_pharmacy_profile.*
import kotlinx.android.synthetic.main.fragment_pharmacy_profile.lab_location_btn
import kotlinx.android.synthetic.main.fragment_pharmacy_profile.pharm_details_lay
import kotlinx.android.synthetic.main.fragment_pharmacy_profile.pharm_pager_Slider
import kotlinx.android.synthetic.main.fragment_pharmacy_profile.pharm_progrss_lay
import kotlinx.android.synthetic.main.fragment_pharmacy_profile.pharm_title_txt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LabProfileFragment : Fragment() {
    var lat=0.0
    var lng=0.0
    var labName=""
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
        return inflater.inflate(R.layout.fragment_lab_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        labObserver()
        navToMap()
        onEditClicked()

    }

    var mContext: MainActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity as MainActivity
    }

    private fun setPageData(lab: Laboratory){
        if (mContext!!.preferences!!.getString("language","")=="Arabic"){
            lab_title_txt!!.text=lab.laboratory_name_ar
            lab_address_txt!!.text=lab.buildingNum_ar+"-"+lab.streetName_ar
            lab_phone_num_txt.text=lab.phonenumber
            lab_email_txt.text=lab.email
        }else{

            lab_title_txt!!.text=lab.laboratory_name_en
            lab_address_txt!!.text=lab.buildingNum_en+"-"+lab.streetName_en
            lab_phone_num_txt.text=lab.phonenumber
            lab_email_txt.text=lab.email

        }


    }
    private fun labObserver() {
        onObserveStart()
        val id =mContext!!.preferences!!.getInteger(Q.USER_ID,0)
        val url = Q.GET_PHARM_BY_ID_API +"/${id}"
        apiClient = ApiClient()
        sessionManager = SessionManager(mContext!!)
        apiClient.getApiService(mContext!!).fitchLabProfile()
            .enqueue(object : Callback<BaseResponce<Laboratory>> {
                override fun onFailure(call: Call<BaseResponce<Laboratory>>, t: Throwable) {
                    alertNetwork(true)
                }

                override fun onResponse(
                    call: Call<BaseResponce<Laboratory>>,
                    response: Response<BaseResponce<Laboratory>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            response.body()!!.data!!.let {
                                lat=it.lat
                                lng=it.lng
                                labName=it.laboratory_name_ar
                                Glide.with(mContext!!).applyDefaultRequestOptions(
                                    RequestOptions()
                                        .placeholder(R.drawable.person_ic)
                                        .error(R.drawable.person_ic))
                                    .load(it.featured)
                                    .centerCrop()
                                    .into(pharm_pager_Slider)
                                setPageData(it)
                                onObserveSuccess()
                            }
                        } else {
                            Toast.makeText(mContext!!, "faid", Toast.LENGTH_SHORT).show()

                        }

                    } else {
                        Toast.makeText(mContext!!, "connect faid", Toast.LENGTH_SHORT).show()

                    }

                }


            })
    }
    private fun onObserveStart(){
        pharm_progrss_lay.visibility= View.VISIBLE
        pharm_details_lay.visibility= View.GONE
    }
    private fun onObserveSuccess(){
        pharm_progrss_lay.visibility= View.GONE
        pharm_details_lay.visibility= View.VISIBLE
    }


    private fun navToMap(){
        val zoom=40
        var lable=labName
        val intent= Intent(Intent.ACTION_VIEW)
        lab_location_btn.setOnClickListener {
            intent.data= Uri.parse("geo:0,0?z=$zoom&q=$lat,$lng,$lable")
            if(intent.resolveActivity(mContext!!.packageManager)!=null){
                mContext!!.startActivity(intent)
            }
        }
    }
    fun alertNetwork(isExit: Boolean = true) {
        val alertBuilder = AlertDialog.Builder(mContext!!)
        //alertBuilder.setTitle(R.string.error)
        alertBuilder.setMessage(R.string.no_internet)
        if (isExit) {
            // alertBuilder.setPositiveButton(R.string.exit) { dialog: DialogInterface, _: Int -> context!!.finish() }
        } else {
            alertBuilder.setPositiveButton(R.string.dismiss) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        }
        if (!mContext!!.isFinishing){
            alertBuilder.show()
        }
    }
    private fun onEditClicked(){
        lab_edit_profile_btn.setOnClickListener {
            mContext!!.intent=Intent(mContext,LabEditProfileActivity::class.java)
            mContext!!.startActivity(mContext!!.intent)
        }
    }
}