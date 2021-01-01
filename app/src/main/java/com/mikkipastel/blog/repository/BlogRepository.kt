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
    suspend fun getCachingTagContent(): MutableList<TagBlog>
    suspend fun getCachingBlogContent(): MutableList<PostBlog>
}

class BlogRepositoryImpl(
    private val service: ApiService,
    private val blogTagDao: BlogTagDao,
    private val blogContentDao: BlogContentDao
) : BlogRepository {
    override suspend fun getBlogPost(page: Int, hashtag: String?): GhostBlogModel {
        return service.getAllPost(page, hashtag)
    }

    override suspend fun getBlogTag(): GhostTagsModel {
        return service.getAllTags()
    }

    override suspend fun insertAllTagToRoom(list: MutableList<TagBlog>) {
        blogTagDao.deleteAllTag()
        blogTagDao.insertTag(list)
    }

    override suspend fun insertAllBlogToRoom(list: MutableList<PostBlog>) {
        blogContentDao.deleteAllContent()
        blogContentDao.insertContent(list)
    }

    override suspend fun getCachingTagContent(): MutableList<TagBlog> {
        return blogTagDao.getTagBlog()
    }

    override suspend fun getCachingBlogContent(): MutableList<PostBlog> {
        return blogContentDao.getContentBlog()
    }
}