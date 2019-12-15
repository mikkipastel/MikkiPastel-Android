package com.mikkipastel.blog.manager

import com.mikkipastel.blog.model.BlogContent
import com.mikkipastel.blog.model.BlogData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("blog")
    fun getAllPost(@Query("published") published: String?): Call<BlogData>

    @GET("blog/tag/{tag}")
    fun getPostByTag(@Path("tag") tag: String,
                     @Query("published") published: String?): Call<BlogData>

    @GET("blog/id/{id}")
    fun getBlogById(@Path("id") id: String): Call<BlogContent>

}