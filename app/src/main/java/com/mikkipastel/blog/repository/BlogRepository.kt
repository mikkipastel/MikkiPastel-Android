package com.mikkipastel.blog.repository

import com.mikkipastel.blog.dao.BlogDao
import com.mikkipastel.blog.manager.ApiService
import com.mikkipastel.blog.model.GhostBlogModel
import com.mikkipastel.blog.model.GhostTagsModel
import com.mikkipastel.blog.model.TagBlog

interface BlogRepository {
    suspend fun getBlogPost(page: Int, hashtag: String?): GhostBlogModel
    suspend fun getBlogTag(): GhostTagsModel
    suspend fun insertAllTagToRoom(list: MutableList<TagBlog>)
}

class BlogRepositoryImpl(private val service: ApiService, private val dao: BlogDao) : BlogRepository {
    override suspend fun getBlogPost(page: Int, hashtag: String?): GhostBlogModel {
        return service.getAllPost(page, hashtag)
    }

    override suspend fun getBlogTag(): GhostTagsModel {
        return service.getAllTags()
    }

    override suspend fun insertAllTagToRoom(list: MutableList<TagBlog>) {
        dao.deleteAllTag()
        dao.insertTag(list)
    }
}