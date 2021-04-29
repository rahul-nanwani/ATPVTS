package com.example.login.configuration

import retrofit2.Call
import retrofit2.http.*
import java.sql.Timestamp

interface ApiInterface {
    @GET("register.php")
    fun performRegistration(@Query("name") Name: String?, @Query("user_name") UserName: String?, @Query("user_password") UserPassword: String?): Call<User>

    @GET("login.php")
    fun performUserLogin(@Query("user_name") username: String?, @Query("user_password") password: String?): Call<User>

    @GET("registerVehicle.php")
    fun performVehicleRegistration(@Query("username") username: String?, @Query("vehicleNumber") vehicleNumber: String?, @Query("vehicleType") vehicleType: String?): Call<ServerResponse>

    @GET("getVehicle.php")
    fun getVehicle(@Query("user_name") username: String? ): Call<List<Vehicle>>

    @GET("getPendingBills.php")
    fun getPendingBills(@Query("user_name") username: String? ): Call<List<Bill>>

    @GET("getPaidBills.php")
    fun getPaidBills(@Query("user_name") username: String? ): Call<List<Bill>>

    @GET("location.php")
    fun submitLocation(@Query("lat") lattitude: String?, @Query("lon") longitude: String?,@Query("vehicle") vehicle: String?): Call<Location>

    @GET("locationTime.php")
    fun submitLocationTime(@Query("lat") lattitude: String?, @Query("lon") longitude: String?,@Query("vehicle") vehicle: String?,@Query("time") time: Timestamp?): Call<Location>

    @GET("running.php")
    fun isServerRunning(): Call<ServerResponse>
}