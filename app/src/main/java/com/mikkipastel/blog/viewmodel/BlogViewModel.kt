package com.mikkipastel.blog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikkipastel.blogservice.domain.GetBlogPostUseCase
import com.mikkipastel.blogservice.domain.GetBlogTagUseCase
import com.mikkipastel.blogservice.model.PostBlog
import com.mikkipastel.blogservice.model.ResultResponse
import com.mikkipastel.blogservice.model.TagBlog
import com.mikkipastel.blogservice.model.request.GetBlogPostRequest
import com.mikkipastel.blogservice.repository.BlogRepository
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
        val tag = if (hashtag == null) "" else "tag:$hashtag"
        val response = withContext(Dispatchers.IO) {
            getBlogPostUseCase.run(
                GetBlogPostRequest(
                    page,
                    tag
                )
            )
        }

        when (response) {
            is ResultResponse.Success -> {
                allBlogPost.value = response.data?.posts!!
                canLazyLoading.value = response.data?.meta?.pagination?.next != null
                blogPage.value = response.data?.meta?.pagination?.page!!
//                withContext(Dispatchers.IO) {
//                    if (hashtag == null && response.data.meta.pagination.page == 1) {
//                        blogRepository.insertAllBlogToRoom(response.data.posts)
//                    }
//                }
            }
            is ResultResponse.Error -> {
                getBlogError.value = Unit
            }
        }
    }
//        .run {
//        viewModelScope.launch {
//            if (hashtag == null) {
//                localBlogContentList.postValue(blogRepository.getCachingBlogContent())
//            }
//        }
//    }

    fun getBlogTag() = viewModelScope.launch(Dispatchers.Main) {
        val response = withContext(Dispatchers.IO) {
            getBlogTagUseCase.run(Unit)
        }

        when (response) {
            is ResultResponse.Success -> {
                val allTags = response.data?.tags!!
                allBlogTag.value = allTags
//                withContext(Dispatchers.IO) {
//                    blogRepository.insertAllTagToRoom(allTags)
//                }
            }
            is ResultResponse.Error -> {
                getTagError.value = Unit
            }
        }
    }
//        .run {
//        viewModelScope.launch {
//            localBlogTagList.postValue(blogRepository.getCachingTagContent())
//        }
//    }
}