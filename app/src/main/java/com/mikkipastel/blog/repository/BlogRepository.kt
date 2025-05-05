package com.mikkipastel.blog.repository

import com.mikkipastel.blog.manager.ApiService
import com.mikkipastel.blog.model.*
import com.mikkipastel.blog.model.request.GetBlogPostRequest
import com.mikkipastel.blog.model.request.Request
import retrofit2.Response

interface BlogRepository {
    suspend fun getBlogPost(request: Request<GetBlogPostRequest>): ResultResponse<GhostBlogModel>
    suspend fun getBlogTag(): ResultResponse<GhostTagsModel>
}

class BlogRepositoryImpl(
    private val service: ApiService
) : BlogRepository {
    override suspend fun getBlogPost(request: Request<GetBlogPostRequest>): ResultResponse<GhostBlogModel> {
        return object : DirectNetworkBoundResource<GhostBlogModel, GhostBlogModel>() {
            override suspend fun createCall(): Response<GhostBlogModel> =
                service.getAllPost(request.value.page, request.value.hashtag)

            override suspend fun convertToResultType(response: GhostBlogModel): GhostBlogModel =
                response
        }.asResult()
    }

    override suspend fun getBlogTag(): ResultResponse<GhostTagsModel> {
        return object : DirectNetworkBoundResource<GhostTagsModel, GhostTagsModel>() {
            override suspend fun createCall(): Response<GhostTagsModel> =
                service.getAllTags()

            override suspend fun convertToResultType(response: GhostTagsModel): GhostTagsModel =
                response
        }.asResult()
    }
}