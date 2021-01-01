package com.mikkipastel.blog.repository

import com.mikkipastel.blog.dao.BlogContentDao
import com.mikkipastel.blog.dao.BlogTagDao
import com.mikkipastel.blog.manager.ApiService
import com.mikkipastel.blog.model.GhostBlogModel
import com.mikkipastel.blog.model.GhostTagsModel
import com.mikkipastel.blog.model.PostBlog
import com.mikkipastel.blog.model.TagBlog

interface BlogRepository {
    suspend fun getBlogPost(page: Int, hashtag: String?): GhostBlogModel
    suspend fun getBlogTag(): GhostTagsModel
    suspend fun insertAllTagToRoom(list: MutableList<TagBlog>)
    suspend fun insertAllBlogToRoom(list: MutableList<PostBlog>)
}

class BlogRepositoryImpl(
    private val service: ApiService,
    private val tagDao: BlogTagDao,
    private val blogContentDao: BlogContentDao
) : BlogRepository {
    override suspend fun getBlogPost(page: Int, hashtag: String?): GhostBlogModel {
        return service.getAllPost(page, hashtag)
    }

    override suspend fun getBlogTag(): GhostTagsModel {
        return service.getAllTags()
    }

    override suspend fun insertAllTagToRoom(list: MutableList<TagBlog>) {
        tagDao.deleteAllTag()
        tagDao.insertTag(list)
    }

    override suspend fun insertAllBlogToRoom(list: MutableList<PostBlog>) {
        blogContentDao.deleteAllContent()
        blogContentDao.insertContent(list)
    }
}