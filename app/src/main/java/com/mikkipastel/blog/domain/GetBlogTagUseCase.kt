package com.mikkipastel.blog.domain

import com.mikkipastel.blog.model.GhostTagsModel
import com.mikkipastel.blog.model.ResultResponse
import com.mikkipastel.blog.repository.BlogRepository
import kotlinx.coroutines.CoroutineDispatcher

class GetBlogTagUseCase(
    private val repo: BlogRepository,
    coroutineDispatcher: CoroutineDispatcher
): CoroutineUseCase<GhostTagsModel, Unit>(
    coroutineDispatcher
) {
    override suspend fun run(
        params: Unit,
        needFresh: Boolean
    ): ResultResponse<GhostTagsModel> {
        return repo.getBlogTag()
    }
}