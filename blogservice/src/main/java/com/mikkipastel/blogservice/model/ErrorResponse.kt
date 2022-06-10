package com.mikkipastel.blogservice.model

import java.lang.Exception

data class ErrorResponse(
    val message: String? = null,
    val exception: Exception? = null
)