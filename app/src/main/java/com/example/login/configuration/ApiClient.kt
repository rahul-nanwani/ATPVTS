package com.example.login.configuration

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    const val BASE_URL = "http://3.137.102.202/loginapp/"
    var retrofit: Retrofit? = null
    @JvmStatic
    fun getApiClient(): Retrofit? {
            if (retrofit == null) {
                retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
            }
            return retrofit
        }
}