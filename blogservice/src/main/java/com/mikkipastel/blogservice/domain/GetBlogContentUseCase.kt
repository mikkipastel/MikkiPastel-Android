package com.mikkipastel.blogservice.domain

import com.mikkipastel.blogservice.model.GhostContentModel
import com.mikkipastel.blogservice.model.ResultResponse
import com.mikkipastel.blogservice.model.request.GetContentRequest
import com.mikkipastel.blogservice.model.request.Request
import com.mikkipastel.blogservice.repository.BlogRepository
import kotlinx.coroutines.CoroutineDispatcher

class GetBlogContentUseCase(
    private val repo: BlogRepository,
    coroutineDispatcher: CoroutineDispatcher
): CoroutineUseCase<GhostContentModel, GetContentRequest>(
    coroutineDispatcher
) {
    override suspend fun run(
        params: GetContentRequest,
        needFresh: Boolean
    ): ResultResponse<GhostContentModel> {
        return repo.getBlogContent(
            Request(
                params
            )
        )
    }
}