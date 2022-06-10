package com.mikkipastel.blogservice.model.request

data class GetBlogPostRequest(
    val page: Int,
    val hashtag: String?
)
