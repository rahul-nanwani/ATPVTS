package com.example.login.configuration

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

class Location {
    @SerializedName("response")
    val response: String? = null
    @SerializedName("lon")
    val longitude: String? = null
    @SerializedName("lat")
    val lattitude: String? = null
    @SerializedName("vehicle")
    val vehicle: String? = null
    @SerializedName("time")
    val time: Timestamp? = null
}