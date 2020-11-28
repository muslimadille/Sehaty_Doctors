package com.muslim_adel.sehatydoctors.modules.home.fragments

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muslim_adel.sehatydoctors.R
import com.muslim_adel.sehatydoctors.modules.home.MainActivity
import com.muslim_adel.sehatydoctors.modules.offers.AddDpctorOfferActivity
import com.muslim_adel.sehatydoctors.modules.offers.AddNewOfferActivity
import com.muslim_adel.sehatydoctors.modules.offers.OffersListAdapter
import com.muslim_adel.sehatydoctors.modules.pharmacyOffers.PharmacyOffersAdapter
import com.muslim_adel.sehatydoctors.remote.apiServices.ApiClient
import com.muslim_adel.sehatydoctors.remote.apiServices.SessionManager
import com.muslim_adel.sehatydoctors.remote.objects.BaseResponce
import com.muslim_adel.sehatydoctors.remote.objects.Offer
import com.muslim_adel.sehatydoctors.remote.objects.PharmacyOffer
import com.muslim_adel.sehatydoctors.utiles.Q
import kotlinx.android.synthetic.main.fragment_offers.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Error


class OffersFragment : Fragment() {
    private var offersList: MutableList<Offer> = ArrayList()
    private var pharmacyOffersList: MutableList<PharmacyOffer> = ArrayList()

    private var offersListAddapter: OffersListAdapter? = null
    private var pharmacyOffersListAddapter: PharmacyOffersAdapter? = null

    private var key=0
    private var title=""

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private val imagesList: MutableList<String> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_offers, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when(mContext!!.preferences!!.getString(Q.USER_TYPE,"")){
            Q.USER_DOCTOR->{
                offersObserver()
                initRVAdapter()
                onAddDocOfferPressed()
            }
            Q.USER_PHARM->{
                initRVAdapter()
                pharmacyOffersObserver()
                onAddPharmOfferPressed()
            }
            Q.USER_LAB->{}


        }

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
    private fun offersObserver() {
        onObserveStart()
        apiClient = ApiClient()
        sessionManager = SessionManager(mContext!!)
        apiClient.getApiService(mContext!!).fitchDoctorOffersList()
            .enqueue(object : Callback<BaseResponce<List<Offer>>> {
                override fun onFailure(call: Call<BaseResponce<List<Offer>>>, t: Throwable) {
                    alertNetwork(false)
                }

                override fun onResponse(
                    call: Call<BaseResponce<List<Offer>>>,
                    response: Response<BaseResponce<List<Offer>>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            response.body()!!.data!!.let {
                                if (it.isNotEmpty()) {
                                    offersList.addAll(it)
                                    offersListAddapter!!.notifyDataSetChanged()
                                    onObserveSuccess()
                                } else {
                                    onObservefaled()
                                }

                            }
                        } else {
                            onObservefaled()
                        }

                    } else {
                        onObservefaled()
                    }

                }


            })
    }
    private fun pharmacyOffersObserver() {
        onObserveStart()
        apiClient = ApiClient()
        sessionManager = SessionManager(mContext!!)
        apiClient.getApiService(mContext!!).fitchPharmacyOffersList()
            .enqueue(object : Callback<BaseResponce<List<PharmacyOffer>>> {
                override fun onFailure(call: Call<BaseResponce<List<PharmacyOffer>>>, t: Throwable) {
                    alertNetwork(false)
                }

                override fun onResponse(
                    call: Call<BaseResponce<List<PharmacyOffer>>>,
                    response: Response<BaseResponce<List<PharmacyOffer>>>
                ) {
                    if (response!!.isSuccessful) {
                        if (response.body()!!.success) {
                            response.body()!!.data!!.let {
                                if (it.isNotEmpty()) {
                                    pharmacyOffersList.addAll(it)
                                    pharmacyOffersListAddapter!!.notifyDataSetChanged()
                                    onObserveSuccess()
                                } else {
                                    onObservefaled()
                                }

                            }
                        } else {
                            onObservefaled()
                        }

                    } else {
                        onObservefaled()
                    }

                }


            })
    }
    private fun initRVAdapter() {

        when(mContext!!.preferences!!.getString(Q.USER_TYPE,"")){
            Q.USER_DOCTOR->{
                val offersLayoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
                category_offers_rv.layoutManager = offersLayoutManager
                offersListAddapter = OffersListAdapter(mContext!!, offersList)
                category_offers_rv.adapter = offersListAddapter
            }
            Q.USER_PHARM->{
                val pharmacyOffersLayoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
                category_offers_rv.layoutManager = pharmacyOffersLayoutManager
                pharmacyOffersListAddapter = PharmacyOffersAdapter(mContext!!, pharmacyOffersList)
                category_offers_rv.adapter = pharmacyOffersListAddapter
            }
            Q.USER_LAB->{}


        }




    }
    private fun onObserveStart() {
        progrss_lay?.visibility = View.VISIBLE
        category_offers_rv?.visibility = View.GONE
        no_search_lay?.visibility = View.GONE
    }
    private fun onObserveSuccess() {
        category_offers_rv?.visibility = View.VISIBLE
        progrss_lay?.visibility = View.GONE
        no_search_lay?.visibility = View.GONE
    }
    private fun onObservefaled() {
        no_search_lay?.visibility = View.VISIBLE
        progrss_lay?.visibility = View.GONE
        category_offers_rv?.visibility = View.GONE
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
    private fun onAddPharmOfferPressed(){
        add_offer_btn.setOnClickListener {
            mContext!!.intent= Intent(mContext, AddNewOfferActivity::class.java)
            mContext!!.startActivity(mContext!!.intent)
        }
    }
    private fun onAddDocOfferPressed(){
        add_offer_btn.setOnClickListener {
            mContext!!.intent= Intent(mContext, AddDpctorOfferActivity::class.java)
            mContext!!.startActivity(mContext!!.intent)
        }
    }


}