package com.mikkipastel.blog.manager

import com.mikkipastel.blog.model.BlogItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlogPostPresenter: BlogPostInterface {

    override fun getAllPost(listener: BlogPostListener) {
        val call = HttpManager().getApiService().getAllPost()
        call.enqueue(object : Callback<List<BlogItem>> {
            override fun onResponse(call: Call<List<BlogItem>>, response: Response<List<BlogItem>>) {
                if (response.isSuccessful && response.body() != null) {
                    val list = response.body()
                    listener.onGetAllPostSuccess(list!!)
                } else {
                    listener.onGetAllPostFailure()
                }
            }

            override fun onFailure(call: Call<List<BlogItem>>?, t: Throwable?) {
                listener.onGetAllPostFailure()
            }
        })
    }

    override fun getPostByTag(hashtag: String, listener: BlogPostListener) {
        val call = HttpManager().getApiService().getPostByTag(hashtag)
        call.enqueue(object : Callback<List<BlogItem>> {
            override fun onResponse(call: Call<List<BlogItem>>, response: Response<List<BlogItem>>) {
                if (response.isSuccessful && response.body() != null) {
                    val list = response.body()
                    listener.onGetAllPostSuccess(list!!)
                } else {
                    listener.onGetAllPostFailure()
                }
            }

            override fun onFailure(call: Call<List<BlogItem>>?, t: Throwable?) {
                listener.onGetAllPostFailure()
            }
        })
    }

    override fun getBlogById(blogId: String, listener: BlogIdListener) {
        val call = HttpManager().getApiService().getBlogById(blogId)
        call.enqueue(object : Callback<List<BlogItem>> {
            override fun onResponse(call: Call<List<BlogItem>>, response: Response<List<BlogItem>>) {
                if (response.isSuccessful && response.body() != null) {
                    val item = response.body()
                    listener.onGetBlogByIdSuccess(item!![0])
                } else {
                    listener.onGetBlogByIdFailure()
                }
            }

            override fun onFailure(call: Call<List<BlogItem>>?, t: Throwable?) {
                listener.onGetBlogByIdFailure()
            }
        })
    }

}

interface BlogPostInterface {
    fun getAllPost(listener: BlogPostListener)
    fun getPostByTag(hashtag: String, listener: BlogPostListener)
    fun getBlogById(blogId: String, listener: BlogIdListener)
}

interface BlogPostListener {
    fun onGetAllPostSuccess(list: List<BlogItem>)
    fun onGetAllPostFailure()
}

interface BlogIdListener {
    fun onGetBlogByIdSuccess(item: BlogItem)
    fun onGetBlogByIdFailure()
}
