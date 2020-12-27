package com.seha_khanah_doctors.utiles

import java.util.*

object Q {
    /*************** Locales  ***********/
    /**..................api......................................***/
    const val BASE_URL = "https://laravelapi.sehakhanah.com/api/"
    const val LOGIN_API = "doctor/login"
    const val PARMACY_LOGIN_API = "pharmacy/login"
    const val LAB_LOGIN_API = "laboratory/login"

    const val REGISTER_API = "register"
    const val SPECIALTY_LIST_API = "specialties"
    const val REAGONS_LIST_API = "areas"
    const val DOCTORS_LIST_API = "search"
    const val DOCTORS_DATES_API = "dates"
    const val DOCTORS_RATES_API = "ratings"
    const val DOCTOR_BY_ID_API="doctor/show"
    const val BOOKING_API = "doctor/dashboard/reservations/add"
    const val GET_BOOKING_API="user/booking"
    const val BOOKING_CANCEL_API="user/booking/cancle"
    const val ABOUT_US_API="admin/aboutUs"
    const val OFFER_SLIDER_IMAGES_API="offer/slideshow"
    const val OFFER_CATEGORIES_API="offer/categories"
    const val OFFERS_MOST_REQUEST_API="offer/mostrequest"
    const val GET_OFFER_BY_ID_API="offers"
    const val GET_OFFER_DATES_API="offer/dates"
    const val OFFER_BOOKING_API = "offer/reservation"
    const val LAB_BOOKING_API = "laboratory/reservation"

    const val MAIN_CATEGORY_OFFERS_API = "offer-category"
    const val SUB_CATEGORY_OFFERS_API = "offer-subcategory"
    const val PARMACY_OFFERS_API = "pharmacy/offers"
    const val ALL_LABS_API = "laboratories"
    const val LABS_SEARCH_API = "laboratory/search"
    const val GET_LAB_BY_ID_API="laboratory/show"
    /**------------------doctor---------------------------------*/
    const val GET_ALL_DAYS_API="doctor/dashboard/days"
    const val GET_ALL_RESERVATIONS_API="doctor/dashboard/reservations"
    const val GET_DOCTOR_OFFERS_API="doctor/dashboard/offers"
    const val GET_DOCTOR_PROFILE_API="doctor/dashboard/profile"
    const val POST_DOC_OFFER_API="doctor/dashboard/offer/add"
    const val DOC_OFFER_CATEGORIES_API="doctor/dashboard/offer/categories"
    const val DOC_OFFER_SUB_CATEGORIES_API="doctor/dashboard/offer/sub-categories/"
    const val DOC_OFFER_SERVICE_API="doctor/dashboard/offer/services/"
    const val DOC_OFFER_SUB_SERVICE_API="doctor/dashboard/offer/sub-services/"
    const val DOC_OFFER_UNITS_API="doctor/dashboard/offer/unit/"
    const val DOC_WORKING_DATES_API="doctor/dashboard/working-hours"
    const val DOC_VACANCIES_DATES_API="doctor/dashboard/vacations"
    const val DOC_ADD_OFFER_API="doctor/dashboard/offer/add"
    const val DOC_PROFISSIONAL_DETAILS_API="profissionalDetails"
    const val DOC_SUB_SPIC_API="subSpecialties"
    const val DOC_UPDATE_PROFILE_API="doctor/dashboard/update"
    const val DOC_UPDATE_ADDRESS_API="doctor/dashboard/address/update"
    const val DOC_PERFIX_TITLE_BY_SPIC_ID_API="doctor/dashboard/prefix-titles/"
    const val DOC_SUB_SICIALITY_BY_SPIC_ID_API="doctor/dashboard/subSpecialties/"
    const val DOC_REGISTER_API="doctor/register"
    const val DOC_ADD_VACATION_API="doctor/dashboard/vacation/add"
    const val DOC_VISITORS_API="visitorNum/"









    /**------------------pharmacy---------------------------------*/
    const val GET_PHARMACY_OFFERS_API="pharmacy/dashboard/offers"
    const val GET_PHARM_BY_ID_API="pharmacy/offer/show"
    const val POST_PHARM_OFFER_API="pharmacy/dashboard/offer/add"
    const val POST_PHARM_PROFILE_API="pharmacy/dashboard/profile"
    const val PHARM_UPDATE_PROFILE_API="pharmacy/dashboard/update"
    const val PHARM_UPDATE_ADDRESS_API="pharmacy/dashboard/address/update"



    /**------------------labs---------------------------------*/
    const val GET_ALL_LAB_RESERVATIONS_API="laboratory/dashboard/reservations"
    const val SEND_LAB_RESERVATIONS_API="laboratory/dashboard/reservations/add"
    const val GET_LAB_PROFILE_API="laboratory/dashboard/profile"
    const val LAB_UPDATE_PROFILE_API="laboratory/dashboard/update"
    const val LAB_UPDATE_ADDRESS_API="laboratory/dashboard/address/update"


























    const val AVATAR_PATH="https://www.obank.itcomunity.com/"
    /*****************************************************************/


    const val MODE_PRIVATE = 0
    const val PREF_FILE = "sehaty_pref"
    const val SELECTED_LOCALE_PREF = "sehaty_selected_locale"

    const val LOCALE_AR_INDEX = 0
    const val LOCALE_EN_INDEX = 1
    var FIRST_TIME = true
    var IS_FIRST_TIME = "first_time"

    var IS_LOGIN="is_login"
    var USER_NAME="USER_NAME"
    var USER_PHONE="USER_PHONE"
    var USER_EMAIL="USER_EMAIL"
    var USER_ID="user_id"
    var USER_GENDER="gender_id"
    var USER_BIRTH="USER_BIRTH"
    var USER_TYPE="user_type"
    var USER_DOCTOR="user_doctor"
    var USER_LAB="user_lab"
    var USER_PHARM="user_pharm"









    const val FIRST_TIME_PREF = "ADW_first_time"

}