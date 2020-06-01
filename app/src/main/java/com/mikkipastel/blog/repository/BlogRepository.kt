package com.mikkipastel.blog.repository

import com.mikkipastel.blog.manager.ApiService
import com.mikkipastel.blog.model.GhostBlogModel
import com.mikkipastel.blog.model.GhostTagsModel

interface BlogRepository {
    suspend fun getBlogPost(page: Int, hashtag: String?): GhostBlogModel
    suspend fun getBlogTag(): GhostTagsModel
}

class BlogRepositoryImpl(private val service: ApiService): BlogRepository {
    override suspend fun getBlogPost(page: Int, hashtag: String?): GhostBlogModel {
        return service.getAllPost(page, hashtag)
    }

    override suspend fun getBlogTag(): GhostTagsModel {
        return service.getAllTags()
    }
}