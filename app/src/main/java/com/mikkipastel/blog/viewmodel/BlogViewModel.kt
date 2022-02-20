package com.mikkipastel.blog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikkipastel.blog.domain.GetBlogPostUseCase
import com.mikkipastel.blog.domain.GetBlogTagUseCase
import com.mikkipastel.blog.model.PostBlog
import com.mikkipastel.blog.model.ResultResponse
import com.mikkipastel.blog.model.TagBlog
import com.mikkipastel.blog.model.request.GetBlogPostRequest
import com.mikkipastel.blog.repository.BlogRepository
import kotlinx.coroutines.*

class BlogViewModel(
    private val blogRepository: BlogRepository,
    private val getBlogPostUseCase: GetBlogPostUseCase,
    private val getBlogTagUseCase: GetBlogTagUseCase
) : ViewModel() {

    val allBlogPost = MutableLiveData<MutableList<PostBlog>>()
    val canLazyLoading = MutableLiveData<Boolean>()
    val blogPage = MutableLiveData<Int>()
    val getBlogError = MutableLiveData<Unit>()

    val allBlogTag = MutableLiveData<MutableList<TagBlog>>()
    val getTagError = MutableLiveData<Unit>()

    val localBlogTagList = MutableLiveData<MutableList<TagBlog>>()
    val localBlogContentList = MutableLiveData<MutableList<PostBlog>>()

    fun getBlogPost(
        page: Int,
        hashtag: String?
    ) = viewModelScope.launch(Dispatchers.Main) {
        val response = withContext(Dispatchers.IO) {
            getBlogPostUseCase.run(
                GetBlogPostRequest(
                    page,
                    hashtag
                )
            )
        }

        when (response) {
            is ResultResponse.Success -> {
                allBlogPost.value = response.data?.posts!!
                canLazyLoading.value = response.data.meta?.pagination?.next != null
                blogPage.value = response.data.meta?.pagination?.page!!
                withContext(Dispatchers.IO) {
                    if (hashtag == null && response.data.meta.pagination.page == 1) {
                        blogRepository.insertAllBlogToRoom(response.data.posts)
                    }
                }
            }
            is ResultResponse.Error -> {
                getBlogError.value = Unit
            }
        }
    }.run {
        GlobalScope.launch {
            if (hashtag == null) {
                localBlogContentList.postValue(blogRepository.getCachingBlogContent())
            }
        }
    }

    fun getBlogTag() = viewModelScope.launch(Dispatchers.Main) {
        val response = withContext(Dispatchers.IO) {
            getBlogTagUseCase.run(Unit)
        }

        when (response) {
            is ResultResponse.Success -> {
                val allTags = response.data?.tags!!
                allBlogTag.value = allTags
                withContext(Dispatchers.IO) {
                    blogRepository.insertAllTagToRoom(allTags)
                }
            }
            is ResultResponse.Error -> {
                getTagError.value = Unit
            }
        }
    }.run {
        GlobalScope.launch {
            localBlogTagList.postValue(blogRepository.getCachingTagContent())
        }
    }
}