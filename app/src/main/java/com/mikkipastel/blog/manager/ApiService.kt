package com.mikkipastel.blog.manager

import com.mikkipastel.blog.model.Item
import com.mikkipastel.blog.model.MikkiBlog
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("posts")
    fun getAllBlogPost(@Query("key") id: String,
                       @Query("maxResults") maxResults: Int,
                       @Query("orderBy") orderBy: String,
                       @Query("fetchImages") fetchImages: Boolean,
                       @Query("fetchBodies") fetchBodies: Boolean): Call<MikkiBlog>

    @GET("posts/{blogId}")
    fun getBlogById(@Path("blogId") blogId: String,
                    @Query("key") id: String): Call<Item>

    @GET("posts/search")
    fun getBlogFromSearch(@Query("q") q: String): Call<MikkiBlog>
}