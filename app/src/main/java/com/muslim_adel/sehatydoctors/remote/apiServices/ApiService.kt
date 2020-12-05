package com.muslim_adel.sehatydoctors.remote.apiServices

import com.muslim_adel.sehatydoctors.remote.objects.*
import com.muslim_adel.sehatydoctors.remote.objects.doctor.DaysModel
import com.muslim_adel.sehatydoctors.remote.objects.doctor.DoctorProfileModel
import com.muslim_adel.sehatydoctors.remote.objects.doctor.ReservationModel
import com.muslim_adel.sehatydoctors.remote.objects.doctor.WorkingDatesModel
import com.muslim_adel.sehatydoctors.utiles.Q
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST(Q.LOGIN_API)
    fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<LoginResponce>

    @POST(Q.LAB_LOGIN_API)
    fun labLogin(
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<LaboratoryLoginResponce>

    @POST(Q.PARMACY_LOGIN_API)
    fun pharmLogin(
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<PharmacyLoginResponce>

    @POST(Q.REGISTER_API)
    @FormUrlEncoded
    fun userregister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phonenumber") phonenumber: String,
        @Field("birthday") birthday: String,
        @Field("gender_id") gender_id: String
    ): Call<BaseResponce<LoginData>>

    @GET(Q.SPECIALTY_LIST_API)
    fun fitchSpecialitiesList(): Call<BaseResponce<List<Specialties>>>

    @GET(Q.REAGONS_LIST_API)
    fun fitchReagonsList(): Call<BaseResponce<List<Reagons>>>

    @GET(Q.DOCTORS_LIST_API)
    fun fitchDoctorsList(
        @Query("specialty_id") specialty_id: Int,
        @Query("area_id") area_id: Int
    ): Call<BaseResponce<Search>>

    @GET(Q.DOCTORS_LIST_API)
    fun fitchDoctorsList(@Query("name") name: String): Call<BaseResponce<Search>>

    @GET
    fun fitchDoctorDatesList(@Url url: String): Call<BaseResponce<Dates>>

    @POST(Q.BOOKING_API)
    fun sendBook(
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("phonenumber") phonenumber: String,
        @Query("booking_date") booking_date: String
    ): Call<BaseResponce<Booking>>

    @POST(Q.SEND_LAB_RESERVATIONS_API)
    fun sendLabBook(
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("phonenumber") phonenumber: String,
        @Query("booking_date") booking_date: String
    ): Call<BaseResponce<Booking>>

    @POST(Q.OFFER_BOOKING_API)
    fun sendOfferBook(
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("phonenumber") phonenumber: String,
        @Query("offer_id") offer_id: Int,
        @Query("checkbox") checkbox: Int,
        @Query("booking_date") booking_date: String
    ): Call<BaseResponce<Booking>>

    @POST(Q.LAB_BOOKING_API)
    fun sendLabBook(
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("phonenumber") phonenumber: String,
        @Query("laboratory_id") laboratory_id: Int,
        @Query("checkbox") checkbox: Int,
        @Query("booking_date") booking_date: String
    ): Call<BaseResponce<Booking>>

    @GET
    fun fitchDoctorRatesList(@Url url: String): Call<BaseResponce<List<Rates>>>

    @GET
    fun fitchDoctorById(@Url url: String): Call<BaseResponce<Doctor>>

    @GET
    fun fitchUserById(@Url url: String): Call<BaseResponce<User>>

    @GET(Q.GET_BOOKING_API)
    fun fitchBookingList(): Call<BaseResponce<Appointment>>

    @GET
    fun bookingCancel(@Url url: String): Call<BaseResponce<AppointmentData>>

    @GET(Q.DOCTORS_LIST_API)
    fun fitchAllDoctorsList(): Call<BaseResponce<Search>>

    @GET(Q.ABOUT_US_API)
    fun fitchAboutUs(): Call<BaseResponce<List<AboutUsData>>>

    @GET(Q.OFFER_SLIDER_IMAGES_API)
    fun fitchOffersSLiderImages(): Call<BaseResponce<List<OfferSlider>>>

    @GET(Q.OFFER_CATEGORIES_API)
    fun fitchOffersSGategories(): Call<BaseResponce<List<OffersCategory>>>

    @GET(Q.OFFERS_MOST_REQUEST_API)
    fun fitchOffersMostRequest(): Call<BaseResponce<List<Offer>>>

    @GET
    fun fitchOfferById(@Url url: String): Call<BaseResponce<Offer>>

    @GET
    fun fitchOfferrDatesList(@Url url: String): Call<BaseResponce<Dates>>

    @GET
    fun fitchMainCategoryOffersList(@Url url: String): Call<BaseResponce<List<Offer>>>

    @GET(Q.PARMACY_OFFERS_API)
    fun fitchPharmacyOffers(): Call<BaseResponce<List<PharmacyOffer>>>

    @GET(Q.ALL_LABS_API)
    fun fitchAllLabsList(): Call<BaseResponce<LabsSearch>>

    @GET(Q.LABS_SEARCH_API)
    fun fitchLabsByNameList(@Query("name") name: String): Call<BaseResponce<LabsSearch>>

    @GET(Q.LABS_SEARCH_API)
    fun fitchLabsByRegionList(@Query("area_id") area_id: Int): Call<BaseResponce<LabsSearch>>

    @GET
    fun fitchLabById(@Url url: String): Call<BaseResponce<Laboratory>>

    /**---------------------------------------------doctor----------------------------------------------------*/
    @GET(Q.GET_ALL_DAYS_API)
    fun fitchAllDaysList(): Call<BaseResponce<List<DaysModel>>>

    @GET(Q.GET_ALL_RESERVATIONS_API)
    fun fitchAllReservationsList(): Call<BaseResponce<List<ReservationModel>>>

    @GET(Q.GET_DOCTOR_OFFERS_API)
    fun fitchDoctorOffersList(): Call<BaseResponce<List<Offer>>>

    @GET(Q.GET_DOCTOR_PROFILE_API)
    fun fitchDoctorProfile(): Call<BaseResponce<DoctorProfileModel>>

    @POST(Q.POST_DOC_OFFER_API)
    fun addDocOffer(
        @Query("title_ar") title_ar: String,
        @Query("title_en") title_en: String,
        @Query("description_ar") description_ar: String,
        @Query("description_en") description_en: String,
        @Query("price") price: String,
        @Query("discount") discount: String,
        @Query("device_name_ar") device_name_ar: String,
        @Query("device_name_en") device_name_en: String,
        @Query("category_id") category_id: String,
        @Query("sub_category_id") sub_category_id: String,
        @Query("service_id") service_id: String,
        @Query("sub_service_id") sub_service_id: String,
        @Query("unit_id") unit_id: String,
        @Query("unit_number") unit_number: String,
        @Query("date_from") date_from: String,
        @Query("date_to") date_to: String,
        @Query("featured1") featured1: String
    ): Call<BaseResponce<PharmAddOfferModel>>

    @GET(Q.DOC_OFFER_CATEGORIES_API)
    fun doctorOffersCategories(): Call<BaseResponce<List<OffersCategory>>>

    @GET
    fun doctorOffersSubCategories(@Url url: String): Call<BaseResponce<List<OffersSubGategory>>>

    @GET
    fun doctorOffersServices(@Url url: String): Call<BaseResponce<List<OfferServicesModel>>>
    @GET
    fun doctorOffersSubServices(@Url url: String): Call<BaseResponce<List<OfferServicesModel>>>
    @GET
    fun doctorOfferUnits(@Url url: String): Call<BaseResponce<List<OfferUnitsModel>>>

    @GET(Q.DOC_WORKING_DATES_API)
    fun doctorWorkingDates(): Call<BaseResponce<List<WorkingDatesModel>>>

    /**---------------------------------------------pharmacy----------------------------------------------------*/
    @GET(Q.GET_PHARMACY_OFFERS_API)
    fun fitchPharmacyOffersList(): Call<BaseResponce<List<PharmacyOffer>>>

    @GET
    fun fitchPharmacyById(@Url url: String): Call<BaseResponce<PharmacyOffer>>

    @POST(Q.POST_PHARM_OFFER_API)
    fun addPharmOffer(
        @Query("title_ar") title_ar: String,
        @Query("title_en") title_en: String,
        @Query("price") price: String,
        @Query("featured") featured: String
    ): Call<BaseResponce<PharmAddOfferModel>>

    /**---------------------------------------------labs----------------------------------------------------*/
    @GET(Q.GET_ALL_LAB_RESERVATIONS_API)
    fun fitchAllLabReservationsList(): Call<BaseResponce<List<ReservationModel>>>

    @GET(Q.GET_LAB_PROFILE_API)
    fun fitchLabProfile(): Call<BaseResponce<Laboratory>>


}