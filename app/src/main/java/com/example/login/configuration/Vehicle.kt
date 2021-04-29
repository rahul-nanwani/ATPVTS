package com.example.login.configuration

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Vehicle {

    @SerializedName("vehicleNumber")
    private val vehicleNumber: String? = null
    @SerializedName("vehicleType")
    private val vehicleType:String? = null

    fun getVehicleNumber(): String? {
        return this.vehicleNumber
    }

    fun getVehicleType(): String? {
        return this.vehicleType
    }

}