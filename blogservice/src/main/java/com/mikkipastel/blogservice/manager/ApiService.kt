package com.mikkipastel.blogservice.manager

import com.mikkipastel.blogservice.model.GhostBlogModel
import com.mikkipastel.blogservice.model.GhostContentModel
import com.mikkipastel.blogservice.model.GhostTagsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val ghost_key = "8443fdc53f9772eb7ee10c1bd8"

interface ApiService {
    @GET("posts?key=$ghost_key&include=tags&fields=id,title,url,feature_image,custom_excerpt,published_at")
    suspend fun getAllPost(
        @Query("page") page: Int,
        @Query("filter") filter: String?
    ): Response<GhostBlogModel>

    @GET("tags?key=$ghost_key")
    suspend fun getAllTags(): Response<GhostTagsModel>

    @GET("posts/{postId}/?key=$ghost_key&include=id&tags&fields=title,url,html,published_at")
    suspend fun getPostById(
        @Path("postId") postId: String
    ): Response<GhostContentModel>
}
