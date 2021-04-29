package com.example.login.configuration

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Bill {
    @SerializedName("id")
    private val id:String? = null
    @SerializedName("number")
    private val number: String? = null
    @SerializedName("toll")
    private val toll:String? = null
    @SerializedName("vehicleNumber")
    private val vehicleNumber:String? = null
    @SerializedName("totalKm")
    private val totalKm:String? = null
    @SerializedName("date")
    private val date:String? = null

    fun getBillId(): String? {
        return this.id
    }

    fun getHighwayNumber(): String? {
        return this.number
    }

    fun getToll(): String? {
        return this.toll
    }

    fun getVehicleNumber(): String? {
        return this.vehicleNumber
    }

    fun getDate(): String? {
        return this.date
    }

    fun getKm(): String? {
        return this.totalKm
    }
}