package com.mikkipastel.blog.manager

import com.mikkipastel.blog.model.Item
import com.mikkipastel.blog.model.MikkiBlog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlogPostPresenter: BlogPostInterface {

    override fun getAllPost(listener: BlogPostListener) {
        val call = HttpManager().getApiService().getAllBlogPost()
        call.enqueue(object : Callback<MikkiBlog> {
            override fun onResponse(call: Call<MikkiBlog>, response: Response<MikkiBlog>) {
                if (response.isSuccessful && response.body() != null) {
                    val list = response.body()
                    listener.onGetAllPostSuccess(list.items)
                } else {
                    listener.onGetAllPostFailure()
                }
            }

            override fun onFailure(call: Call<MikkiBlog>?, t: Throwable?) {
                listener.onGetAllPostFailure()
            }

        })
    }

    override fun getBlogById(blogId: String, listener: BlogIdListener) {
        val call = HttpManager().getApiService().getBlogById(blogId)
        call.enqueue(object : Callback<Item> {
            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                if (response.isSuccessful && response.body() != null) {
                    val list = response.body()
                    listener.onGetBlogByIdSuccess(list)
                } else {
                    listener.onGetBlogByIdFailure()
                }
            }

            override fun onFailure(call: Call<Item>?, t: Throwable?) {
                listener.onGetBlogByIdFailure()
            }
        })
    }

}

interface BlogPostInterface {
    fun getAllPost(listener: BlogPostListener)
    fun getBlogById(blogId: String, listener: BlogIdListener)
}

interface BlogPostListener {
    fun onGetAllPostSuccess(list: List<Item>)
    fun onGetAllPostFailure()
}

interface BlogIdListener {
    fun onGetBlogByIdSuccess(item: Item)
    fun onGetBlogByIdFailure()
}