package com.mikkipastel.blog.domain

import com.mikkipastel.blog.model.GhostBlogModel
import com.mikkipastel.blog.model.ResultResponse
import com.mikkipastel.blog.model.request.GetBlogPostRequest
import com.mikkipastel.blog.model.request.Request
import com.mikkipastel.blog.repository.BlogRepository
import kotlinx.coroutines.CoroutineDispatcher

class GetBlogPostUseCase(
    private val repo: BlogRepository,
    coroutineDispatcher: CoroutineDispatcher
): CoroutineUseCase<GhostBlogModel, GetBlogPostRequest>(
    coroutineDispatcher
) {
    override suspend fun run(
        params: GetBlogPostRequest,
        needFresh: Boolean
    ): ResultResponse<GhostBlogModel> {
        return repo.getBlogPost(
            Request(
                params
            )
        )
    }
}