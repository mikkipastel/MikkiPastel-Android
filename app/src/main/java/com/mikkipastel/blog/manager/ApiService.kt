package com.mikkipastel.blog.manager

import com.mikkipastel.blog.model.Item
import com.mikkipastel.blog.model.MikkiBlog
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
//    @GET("posts?fetchImages=true&maxResults=20&orderBy=published&key=AIzaSyBHCzo2LyvwWc4utLoONgiuDCO96yMFF2U&fetchBodies=false")
    @GET("posts?fetchImages=true&maxResults=20&orderBy=published&key=AIzaSyBHCzo2LyvwWc4utLoONgiuDCO96yMFF2U")
    fun getAllBlogPost(): Call<MikkiBlog>

    @GET("post/{blogId}?key=AIzaSyBHCzo2LyvwWc4utLoONgiuDCO96yMFF2U")
    fun getBlogById(@Path("blogId") blogId: String): Call<Item>
}