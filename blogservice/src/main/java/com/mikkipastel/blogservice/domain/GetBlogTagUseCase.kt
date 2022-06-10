package com.mikkipastel.blogservice.domain

import com.mikkipastel.blogservice.model.GhostTagsModel
import com.mikkipastel.blogservice.model.ResultResponse
import com.mikkipastel.blogservice.repository.BlogRepository
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