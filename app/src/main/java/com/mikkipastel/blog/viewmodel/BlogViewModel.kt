package com.mikkipastel.blog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikkipastel.blog.model.PostBlog
import com.mikkipastel.blog.model.TagBlog
import com.mikkipastel.blog.repository.BlogRepository
import kotlinx.coroutines.*

class BlogViewModel(private val blogRepository: BlogRepository) : ViewModel() {

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

    val localBlogTagList = MutableLiveData<MutableList<TagBlog>>()
    val localBlogContentList = MutableLiveData<MutableList<PostBlog>>()

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
            withContext(Dispatchers.IO) {
                if (hashtag == null && response.meta?.pagination?.page == 1)
                    blogRepository.insertAllBlogToRoom(response.posts!!)
            }
        }.run {
            GlobalScope.launch {
                if (hashtag == null) {
                    localBlogContentList.postValue(blogRepository.getCachingBlogContent())
                }
            }
        }
    }

    fun getBlogTag() {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            _getTagError.value = Unit
        }
        viewModelScope.launch(exceptionHandler) {
            val response = blogRepository.getBlogTag()
            _allBlogTag.value = response.tags
            withContext(Dispatchers.IO) {
                blogRepository.insertAllTagToRoom(response.tags)
            }
        }.run {
            GlobalScope.launch {
                localBlogTagList.postValue(blogRepository.getCachingTagContent())
            }
        }
    }
}