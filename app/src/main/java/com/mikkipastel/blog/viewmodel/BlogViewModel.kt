package com.mikkipastel.blog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikkipastel.blog.model.PostBlog
import com.mikkipastel.blog.model.TagBlog
import com.mikkipastel.blog.repository.BlogRepository
import kotlinx.coroutines.*

class BlogViewModel(private val blogRepository: BlogRepository) : ViewModel() {

    val allBlogPost = MutableLiveData<MutableList<PostBlog>>()
    val canLazyLoading = MutableLiveData<Boolean>()
    val blogPage = MutableLiveData<Int>()
    val getBlogError = MutableLiveData<Unit>()

    val allBlogTag = MutableLiveData<MutableList<TagBlog>>()
    val getTagError = MutableLiveData<Unit>()

    val localBlogTagList = MutableLiveData<MutableList<TagBlog>>()
    val localBlogContentList = MutableLiveData<MutableList<PostBlog>>()

    fun getBlogPost(page: Int, hashtag: String?) {
        val tag = if (hashtag == null) "" else "tag:$hashtag"
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            getBlogError.value = Unit
        }
        viewModelScope.launch(exceptionHandler) {
            val response = blogRepository.getBlogPost(page, tag)
            allBlogPost.value = response.posts
            canLazyLoading.value = response.meta?.pagination?.next != null
            blogPage.value = response.meta?.pagination?.page
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
            getTagError.value = Unit
        }
        viewModelScope.launch(exceptionHandler) {
            val response = blogRepository.getBlogTag()
            allBlogTag.value = response.tags
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