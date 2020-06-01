package com.mikkipastel.blog.manager

import com.mikkipastel.blog.model.GhostBlogModel
import com.mikkipastel.blog.model.GhostTagsModel
import retrofit2.http.GET
import retrofit2.http.Query

const val ghost_key = "8443fdc53f9772eb7ee10c1bd8"

interface ApiService {
    //Network class
    @GET("posts?key=${ghost_key}&include=tags&fields=title,url,feature_image,custom_excerpt,published_at")
    suspend fun getAllPost(@Query("page") page: Int,
                           @Query("filter") filter: String?): GhostBlogModel

    @GET("tags?key=${ghost_key}")
    suspend fun getAllTags(): GhostTagsModel
}