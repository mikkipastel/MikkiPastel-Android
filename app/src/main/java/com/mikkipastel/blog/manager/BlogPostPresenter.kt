package com.mikkipastel.blog.manager

import com.mikkipastel.blog.model.BlogData
import com.mikkipastel.blog.model.BlogItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlogPostPresenter: BlogPostInterface {

    override fun getAllPost(listener: BlogPostListener) {
        val call = HttpManager().getApiService().getAllPost()
        call.enqueue(object : Callback<BlogData> {
            override fun onResponse(call: Call<BlogData>, response: Response<BlogData>) {
                if (response.isSuccessful && response.body() != null) {
                    val items = response.body()
                    listener.onGetAllPostSuccess(items!!.items)
                } else {
                    listener.onGetAllPostFailure()
                }
            }

            override fun onFailure(call: Call<BlogData>?, t: Throwable?) {
                listener.onGetAllPostFailure()
            }
        })
    }

    override fun getPostByTag(hashtag: String, listener: BlogPostListener) {
        val call = HttpManager().getApiService().getPostByTag(hashtag)
        call.enqueue(object : Callback<BlogData> {
            override fun onResponse(call: Call<BlogData>, response: Response<BlogData>) {
                if (response.isSuccessful && response.body() != null) {
                    val items = response.body()
                    listener.onGetAllPostSuccess(items!!.items)
                } else {
                    listener.onGetAllPostFailure()
                }
            }

            override fun onFailure(call: Call<BlogData>?, t: Throwable?) {
                listener.onGetAllPostFailure()
            }
        })
    }

    override fun getBlogById(blogId: String, listener: BlogIdListener) {
        val call = HttpManager().getApiService().getBlogById(blogId)
        call.enqueue(object : Callback<BlogItem> {
            override fun onResponse(call: Call<BlogItem>, response: Response<BlogItem>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()
                    listener.onGetBlogByIdSuccess(data!!)
                } else {
                    listener.onGetBlogByIdFailure()
                }
            }

            override fun onFailure(call: Call<BlogItem>?, t: Throwable?) {
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
