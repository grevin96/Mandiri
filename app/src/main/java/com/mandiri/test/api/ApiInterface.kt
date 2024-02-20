package com.mandiri.test.api

import com.mandiri.test.model.response.article.Articles
import com.mandiri.test.model.response.source.Sources
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("top-headlines/sources")
    fun getSources(@Query("apiKey") apiKey: String, @Query("category") category: String): Call<Sources>

    @GET("everything?pageSize=20")
    fun getArticles(@Query("apikey") apiKey: String, @Query("sources") sources: String, @Query("q") q: String, @Query("page") page: Int): Call<Articles>
}