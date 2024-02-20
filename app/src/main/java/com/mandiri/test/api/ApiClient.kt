package com.mandiri.test.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL  = "https://newsapi.org/v2/"
    var API_KEY                 = "8ced2e4f0bff40f68a18efc27c76c0ab"
//    var API_KEY                 = "0f4953cab574486f8a90bcfd68140eb0"

    val api: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}