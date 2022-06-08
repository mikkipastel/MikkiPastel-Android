package com.mikkipastel.blog.repository

import com.mikkipastel.blog.dao.BlogContentDao
import com.mikkipastel.blog.dao.BlogTagDao
import com.mikkipastel.blog.manager.ApiService
import com.mikkipastel.blog.model.*
import com.mikkipastel.blog.model.request.GetBlogPostRequest
import com.mikkipastel.blog.model.request.Request
import retrofit2.Response

interface BlogRepository {
    suspend fun getBlogPost(request: Request<GetBlogPostRequest>): ResultResponse<GhostBlogModel>
    suspend fun getBlogTag(): ResultResponse<GhostTagsModel>
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