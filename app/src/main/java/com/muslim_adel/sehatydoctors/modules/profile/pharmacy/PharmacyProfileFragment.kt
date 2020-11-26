package com.muslim_adel.sehatydoctors.modules.profile.pharmacy

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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.muslim_adel.sehatydoctors.R
import com.muslim_adel.sehatydoctors.modules.home.MainActivity
import com.muslim_adel.sehatydoctors.remote.apiServices.ApiClient
import com.muslim_adel.sehatydoctors.remote.apiServices.SessionManager
import com.muslim_adel.sehatydoctors.remote.objects.BaseResponce
import com.muslim_adel.sehatydoctors.remote.objects.PharmacyOffer
import com.muslim_adel.sehatydoctors.utiles.Q
import kotlinx.android.synthetic.main.fragment_pharmacy_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PharmacyProfileFragment : Fragment() {
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
        // Inflate the layout for mContext!! fragment
        return inflater.inflate(R.layout.fragment_pharmacy_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        labObserver()
        navToMap()
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

    private fun setPageData(pharm: PharmacyOffer){
        if (mContext!!.preferences!!.getString("language","")=="Arabic"){
            pharm_title_txt!!.text=pharm.pharmacy.pharmacy_name_ar
            address!!.text=pharm.pharmacy.buildingNum_ar+"-"+pharm.pharmacy.streetName_ar
            pharm_info_txt.text=pharm.pharmacy.about_ar
            pharm_doc_name.text=pharm.pharmacy.firstName_ar+" "+pharm.pharmacy.lastName_ar
        }else{
            pharm_title_txt!!.text=pharm.pharmacy.pharmacy_name_en
            address!!.text=pharm.pharmacy.buildingNum_en+"-"+pharm.pharmacy.streetName_en
            pharm_info_txt.text=pharm.pharmacy.about_en
            pharm_doc_name.text=pharm.pharmacy.firstName_en+" "+pharm.pharmacy.lastName_en


        }


        pharm_show_more_txt.setOnClickListener {
            if(pharm_show_more_txt.text==getString(R.string.more)){
                pharm_info_txt.maxLines=20
                pharm_show_more_txt.text=getString(R.string.less)
            }else{
                pharm_info_txt.maxLines=5
                pharm_show_more_txt.text=getString(R.string.more)

            }
        }
    }
    private fun labObserver() {
        onObserveStart()
        val id =mContext!!.preferences!!.getInteger(Q.USER_ID,0)
        val url = Q.GET_PHARM_BY_ID_API +"/${id}"
        apiClient = ApiClient()
        sessionManager = SessionManager(mContext!!)
        apiClient.getApiService(mContext!!).fitchPharmacyById(url)
            .enqueue(object : Callback<BaseResponce<PharmacyOffer>> {
                override fun onFailure(call: Call<BaseResponce<PharmacyOffer>>, t: Throwable) {
                    alertNetwork(true)
                }

                override fun onResponse(
                    call: Call<BaseResponce<PharmacyOffer>>,
                    response: Response<BaseResponce<PharmacyOffer>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            response.body()!!.data!!.let {
                                lat=it.pharmacy.lat
                                lng=it.pharmacy.lng
                                labName=it.pharmacy.pharmacy_name_ar
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
}