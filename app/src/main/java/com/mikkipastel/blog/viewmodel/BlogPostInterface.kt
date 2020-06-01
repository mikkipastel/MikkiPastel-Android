package com.mikkipastel.blog.viewmodel

import com.mikkipastel.blog.model.GhostBlogModel
import com.mikkipastel.blog.model.TagBlog

interface BlogPostListener {
    fun onGetPostSuccess(data: GhostBlogModel)
    fun onGetPostFailure()
}

interface BlogTagListener {
    fun onGetTagSuccess(data: MutableList<TagBlog>)
    fun onGetTagFailure()
}