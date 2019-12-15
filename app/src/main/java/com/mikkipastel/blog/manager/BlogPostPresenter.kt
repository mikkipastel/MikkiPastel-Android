package com.mikkipastel.blog.manager

import com.mikkipastel.blog.model.BlogContent
import com.mikkipastel.blog.model.BlogData
import com.mikkipastel.blog.model.BlogItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlogPostPresenter: BlogPostInterface {

    override fun getAllPost(lastPublished: String?, listener: BlogPostListener) {
        val call = HttpManager().getApiService().getAllPost(lastPublished)
        call.enqueue(object : Callback<BlogData> {
            override fun onResponse(call: Call<BlogData>, response: Response<BlogData>) {
                if (response.isSuccessful && response.body() != null) {
                    val items = response.body()
                    listener.onGetPostSuccess(items!!)
                } else {
                    listener.onGetPostFailure()
                }
            }

            override fun onFailure(call: Call<BlogData>?, t: Throwable?) {
                listener.onGetPostFailure()
            }
        })
    }

    override fun getPostByTag(hashtag: String, lastPublished: String?, listener: BlogPostListener) {
        val call = HttpManager().getApiService().getPostByTag(hashtag, lastPublished)
        call.enqueue(object : Callback<BlogData> {
            override fun onResponse(call: Call<BlogData>, response: Response<BlogData>) {
                if (response.isSuccessful && response.body() != null) {
                    val items = response.body()
                    listener.onGetPostSuccess(items!!)
                } else {
                    listener.onGetPostFailure()
                }
            }

            override fun onFailure(call: Call<BlogData>?, t: Throwable?) {
                listener.onGetPostFailure()
            }
        })
    }

    override fun getBlogById(blogId: String, listener: BlogIdListener) {
        val call = HttpManager().getApiService().getBlogById(blogId)
        call.enqueue(object : Callback<BlogContent> {
            override fun onResponse(call: Call<BlogContent>, response: Response<BlogContent>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()
                    listener.onGetBlogByIdSuccess(data!!.data)
                } else {
                    listener.onGetBlogByIdFailure()
                }
            }

            override fun onFailure(call: Call<BlogContent>?, t: Throwable?) {
                listener.onGetBlogByIdFailure()
            }
        })
    }

}

interface BlogPostInterface {
    fun getAllPost(lastPublished: String?, listener: BlogPostListener)
    fun getPostByTag(hashtag: String, lastPublished: String?, listener: BlogPostListener)
    fun getBlogById(blogId: String, listener: BlogIdListener)
}

interface BlogPostListener {
    fun onGetPostSuccess(data: BlogData)
    fun onGetPostFailure()
}

interface BlogIdListener {
    fun onGetBlogByIdSuccess(item: BlogItem)
    fun onGetBlogByIdFailure()
}
