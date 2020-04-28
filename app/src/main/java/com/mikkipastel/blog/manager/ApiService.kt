package com.mikkipastel.blog.manager

import com.mikkipastel.blog.model.BlogContent
import com.mikkipastel.blog.model.BlogData
import com.mikkipastel.blog.model.GhostBlogModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val ghost_key = "8443fdc53f9772eb7ee10c1bd8"

interface ApiService {
    @GET("posts?key=${ghost_key}&include=tags&fields=title,url,feature_image,custom_excerpt,published_at")
    fun getAllPost(@Query("page") page: Int,
                   @Query("filter") filter: String?): Call<GhostBlogModel>

    @GET("blog/id/{id}")
    fun getBlogById(@Path("id") id: String): Call<BlogContent>

}