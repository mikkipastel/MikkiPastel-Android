package com.mikkipastel.blog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikkipastel.blog.repository.BlogRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class BlogViewModel(private val blogRepository: BlogRepository): ViewModel() {
    fun getBlogPost(page: Int, hashtag: String?, listener: BlogPostListener) {
        val tag = if (hashtag == null) "" else "tag:$hashtag"
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            listener.onGetPostFailure()
        }
        viewModelScope.launch(exceptionHandler) {
            val response = blogRepository.getBlogPost(page, tag)
            listener.onGetPostSuccess(response)
        }
    }

    fun getBlogTag(listener: BlogTagListener) {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            listener.onGetTagFailure()
        }
        viewModelScope.launch(exceptionHandler) {
            val response = blogRepository.getBlogTag()
            listener.onGetTagSuccess(response.tags)
        }
    }
}