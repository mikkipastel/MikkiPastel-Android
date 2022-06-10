package com.mikkipastel.readcontent.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikkipastel.blogservice.domain.GetBlogContentUseCase
import com.mikkipastel.blogservice.model.PostDetail
import com.mikkipastel.blogservice.model.ResultResponse
import com.mikkipastel.blogservice.model.request.GetContentRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContentViewModel(
    private val getBlogContentUseCase: GetBlogContentUseCase
) : ViewModel() {

    val contentPost = MutableLiveData<PostDetail>()
    val getContentError = MutableLiveData<Unit>()

    fun getContent(postId: String) = viewModelScope.launch(Dispatchers.Main) {
        val response = withContext(Dispatchers.IO) {
            getBlogContentUseCase.run(
                GetContentRequest(
                    postId
                )
            )
        }

        when (response) {
            is ResultResponse.Success -> {
                when (response.data?.posts.isNullOrEmpty()) {
                    true -> getContentError.postValue(Unit)
                    false -> contentPost.postValue(response.data?.posts!![0])
                }
            }
            is ResultResponse.Error -> getContentError.value = Unit
        }
    }
}