package com.seha_khanah_doctors.remote.objects

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BaseResponce<T> : Serializable{
    var success: Boolean =false
    //var message: Message? = null
    var data: T? = null
}
