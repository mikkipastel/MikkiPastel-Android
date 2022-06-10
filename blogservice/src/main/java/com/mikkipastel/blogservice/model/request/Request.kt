package com.mikkipastel.blogservice.model.request

data class Request<T>(var value: T, var needFresh: Boolean = true)