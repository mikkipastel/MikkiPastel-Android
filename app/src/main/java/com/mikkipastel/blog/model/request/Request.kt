package com.mikkipastel.blog.model.request

data class Request<T>(var value: T, var needFresh: Boolean = true)