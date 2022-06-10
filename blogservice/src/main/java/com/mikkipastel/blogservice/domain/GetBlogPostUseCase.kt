package com.mikkipastel.blogservice.domain

import com.mikkipastel.blogservice.model.GhostBlogModel
import com.mikkipastel.blogservice.model.ResultResponse
import com.mikkipastel.blogservice.model.request.GetBlogPostRequest
import com.mikkipastel.blogservice.model.request.Request
import com.mikkipastel.blogservice.repository.BlogRepository
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