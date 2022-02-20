package com.mikkipastel.blog.model.request

data class GetBlogPostRequest(
    val page: Int,
    val hashtag: String?
)
