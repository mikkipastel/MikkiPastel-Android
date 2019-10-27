package com.mikkipastel.blog.manager

import com.mikkipastel.blog.model.BlogContent
import com.mikkipastel.blog.model.BlogData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("blog")
    fun getAllPost(): Call<BlogData>

    @GET("blog")
    fun getPostByTag(@Query("tag") tag: String): Call<BlogData>

    @GET("content")
    fun getBlogById(@Query("id") id: String): Call<BlogContent>

}