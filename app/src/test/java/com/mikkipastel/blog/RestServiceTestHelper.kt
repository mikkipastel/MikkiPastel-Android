package com.mikkipastel.blog

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestServiceTestHelper {
    fun <T> createRetrofitService(host: String, classTarget: Class<T>): T {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = okhttp3.OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        return Retrofit.Builder()
                .baseUrl(host)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(classTarget)
    }
}