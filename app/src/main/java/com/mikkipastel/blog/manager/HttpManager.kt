package com.mikkipastel.blog.manager

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class HttpManager {

    val url = "https://asia-east2-mikkipastel.cloudfunctions.net/"

    val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create()

    private val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    fun getApiService(): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}