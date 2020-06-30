package com.mikkipastel.blog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikkipastel.blog.model.PostBlog
import com.mikkipastel.blog.model.TagBlog
import com.mikkipastel.blog.repository.BlogRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class BlogViewModel(private val blogRepository: BlogRepository): ViewModel() {

    private val _allBlogPost = MutableLiveData<MutableList<PostBlog>>()
    val allBlogPost = _allBlogPost

    private val _canLazyLoading = MutableLiveData<Boolean>()
    val canLazyLoading = _canLazyLoading

    private val _blogPage = MutableLiveData<Int>()
    val blogPage = _blogPage

    private val _getBlogError = MutableLiveData<Unit>()
    val getBlogError = _getBlogError

    private val _allBlogTag = MutableLiveData<MutableList<TagBlog>>()
    val allBlogTag = _allBlogTag

    private val _getTagError = MutableLiveData<Unit>()
    val getTagError = _getTagError

    fun getBlogPost(page: Int, hashtag: String?) {
        val tag = if (hashtag == null) "" else "tag:$hashtag"
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            _getBlogError.value = Unit
        }
        viewModelScope.launch(exceptionHandler) {
            val response = blogRepository.getBlogPost(page, tag)
            _allBlogPost.value = response.posts
            _canLazyLoading.value = response.meta?.pagination?.next != null
            _blogPage.value = response.meta?.pagination?.page
        }
    }

    fun getBlogTag() {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            _getTagError.value = Unit
        }
        viewModelScope.launch(exceptionHandler) {
            val response = blogRepository.getBlogTag()
            _allBlogTag.value = response.tags
        }
    }
}