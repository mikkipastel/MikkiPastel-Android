package com.mikkipastel.blog.manager

import com.mikkipastel.blog.model.BlogData
import com.mikkipastel.blog.model.BlogItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("blog")
    fun getAllPost(): Call<BlogData>

    @GET("blog")
    fun getPostByTag(@Query("tag") tag: String): Call<BlogData>

    @GET("blog")
    fun getBlogById(@Query("id") id: String): Call<BlogItem>

}