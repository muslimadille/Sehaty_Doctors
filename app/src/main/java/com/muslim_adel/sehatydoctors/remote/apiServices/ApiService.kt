package com.muslim_adel.sehatydoctors.remote.apiServices
import com.muslim_adel.sehatydoctors.remote.objects.*
import com.muslim_adel.sehatydoctors.remote.objects.doctor.DaysModel
import com.muslim_adel.sehatydoctors.remote.objects.doctor.ReservationModel
import com.muslim_adel.sehatydoctors.utiles.Q
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST(Q.LOGIN_API)
    fun login(@Query("email")email:String, @Query("password")password:String): Call<LoginResponce>

    @POST(Q.REGISTER_API)
    @FormUrlEncoded
    fun userregister(
        @Field("name") name:String,
        @Field("email") email:String,
        @Field("password") password:String,
        @Field("phonenumber") phonenumber:String,
        @Field("birthday") birthday: String,
        @Field("gender_id") gender_id:String): Call<BaseResponce<LoginData>>

    @GET(Q.SPECIALTY_LIST_API)
    fun fitchSpecialitiesList():Call<BaseResponce<List<Specialties>>>

    @GET(Q.REAGONS_LIST_API)
    fun fitchReagonsList():Call<BaseResponce<List<Reagons>>>

    @GET(Q.DOCTORS_LIST_API)
    fun fitchDoctorsList(@Query("specialty_id")specialty_id:Int,@Query("area_id")area_id:Int):Call<BaseResponce<Search>>

    @GET(Q.DOCTORS_LIST_API)
    fun fitchDoctorsList(@Query("name")name:String):Call<BaseResponce<Search>>

    @GET
    fun fitchDoctorDatesList(@Url url:String):Call<BaseResponce<Dates>>
    @POST(Q.BOOKING_API)
    fun sendBook(@Query("name") name:String,
                 @Query("email") email:String,
                 @Query("phonenumber") phonenumber: String,
                 @Query("doctor_id") doctor_id:Int,
                 @Query("checkbox") checkbox:Int,
                 @Query("booking_date") booking_date:String): Call<BaseResponce<Booking>>

    @POST(Q.OFFER_BOOKING_API)
    fun sendOfferBook(@Query("name") name:String,
                 @Query("email") email:String,
                 @Query("phonenumber") phonenumber: String,
                 @Query("offer_id") offer_id:Int,
                 @Query("checkbox") checkbox:Int,
                 @Query("booking_date") booking_date:String): Call<BaseResponce<Booking>>
    @POST(Q.LAB_BOOKING_API)
    fun sendLabBook(@Query("name") name:String,
                      @Query("email") email:String,
                      @Query("phonenumber") phonenumber: String,
                      @Query("laboratory_id") laboratory_id:Int,
                      @Query("checkbox") checkbox:Int,
                      @Query("booking_date") booking_date:String): Call<BaseResponce<Booking>>
    @GET
    fun fitchDoctorRatesList(@Url url:String):Call<BaseResponce<List<Rates>>>
    @GET
    fun fitchDoctorById(@Url url:String):Call<BaseResponce<Doctor>>
    @GET
    fun fitchUserById(@Url url:String):Call<BaseResponce<User>>

    @GET(Q.GET_BOOKING_API)
    fun fitchBookingList():Call<BaseResponce<Appointment>>
    @GET
    fun bookingCancel(@Url url:String):Call<BaseResponce<AppointmentData>>
    @GET(Q.DOCTORS_LIST_API)
    fun fitchAllDoctorsList():Call<BaseResponce<Search>>
    @GET(Q.ABOUT_US_API)
    fun fitchAboutUs():Call<BaseResponce<List<AboutUsData>>>
    @GET(Q.OFFER_SLIDER_IMAGES_API)
    fun fitchOffersSLiderImages():Call<BaseResponce<List<OfferSlider>>>
    @GET(Q.OFFER_CATEGORIES_API)
    fun fitchOffersSGategories():Call<BaseResponce<List<OffersCategory>>>

    @GET(Q.OFFERS_MOST_REQUEST_API)
    fun fitchOffersMostRequest():Call<BaseResponce<List<Offer>>>

    @GET
    fun fitchOfferById(@Url url:String):Call<BaseResponce<Offer>>

    @GET
    fun fitchOfferrDatesList(@Url url:String):Call<BaseResponce<Dates>>
    @GET
    fun fitchMainCategoryOffersList(@Url url:String):Call<BaseResponce<List<Offer>>>

    @GET(Q.PARMACY_OFFERS_API)
    fun fitchPharmacyOffers():Call<BaseResponce<List<PharmacyOffer>>>

    @GET(Q.ALL_LABS_API)
    fun fitchAllLabsList():Call<BaseResponce<LabsSearch>>
    @GET(Q.LABS_SEARCH_API)
    fun fitchLabsByNameList(@Query("name")name:String):Call<BaseResponce<LabsSearch>>
    @GET(Q.LABS_SEARCH_API)
    fun fitchLabsByRegionList(@Query("area_id")area_id:Int):Call<BaseResponce<LabsSearch>>
    @GET
    fun fitchLabById(@Url url:String):Call<BaseResponce<Laboratory>>
    /**---------------------------------------------doctor----------------------------------------------------*/
    @GET(Q.GET_ALL_DAYS_API)
    fun fitchAllDaysList():Call<BaseResponce<List<DaysModel>>>
    @GET(Q.GET_ALL_RESERVATIONS_API)
    fun fitchAllReservationsList():Call<BaseResponce<List<ReservationModel>>>
    @GET(Q.GET_DOCTOR_OFFERS_API)
    fun fitchDoctorOffersList():Call<BaseResponce<List<Offer>>>

}