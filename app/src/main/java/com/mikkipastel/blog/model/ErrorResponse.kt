package com.mikkipastel.blog.model

import java.lang.Exception

data class ErrorResponse(
    val message: String? = null,
    val exception: Exception? = null
)