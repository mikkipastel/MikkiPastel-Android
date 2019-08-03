package com.mikkipastel.blog.manager

import com.mikkipastel.blog.model.Item
import com.mikkipastel.blog.model.MikkiBlog
import com.mikkipastel.blog.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlogPostPresenter: BlogPostInterface {

    override fun getAllPost(listener: BlogPostListener) {
        val call = HttpManager().getApiService().getAllBlogPost(myBlogId, maxResults, orderBy, fetchImages, fetchBodies)
        call.enqueue(object : Callback<MikkiBlog> {
            override fun onResponse(call: Call<MikkiBlog>, response: Response<MikkiBlog>) {
                if (response.isSuccessful && response.body() != null) {
                    val list = response.body()
                    listener.onGetAllPostSuccess(list?.items!!)
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
        val call = HttpManager().getApiService().getBlogById(blogId, myBlogId)
        call.enqueue(object : Callback<Item> {
            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                if (response.isSuccessful && response.body() != null) {
                    val list = response.body()
                    listener.onGetBlogByIdSuccess(list!!)
                } else {
                    listener.onGetBlogByIdFailure()
                }
            }

            override fun onFailure(call: Call<Item>?, t: Throwable?) {
                listener.onGetBlogByIdFailure()
            }
        })
    }

    override fun getBlogBySearch(query: String, listener: BlogSearchListener) {
        val call = HttpManager().getApiService().getBlogFromSearch(query)
        call.enqueue(object : Callback<MikkiBlog> {
            override fun onResponse(call: Call<MikkiBlog>, response: Response<MikkiBlog>) {
                if (response.isSuccessful && response.body() != null) {
                    val list = response.body()
                    listener.onGetBlogSearchSuccess(list?.items!!)
                } else {
                    listener.onGetBlogSearchFailure()
                }
            }

            override fun onFailure(call: Call<MikkiBlog>?, t: Throwable?) {
                listener.onGetBlogSearchFailure()
            }
        })
    }

}

interface BlogPostInterface {
    fun getAllPost(listener: BlogPostListener)
    fun getBlogById(blogId: String, listener: BlogIdListener)
    fun getBlogBySearch(query: String, listener: BlogSearchListener)
}

interface BlogPostListener {
    fun onGetAllPostSuccess(list: List<Item>)
    fun onGetAllPostFailure()
}

interface BlogIdListener {
    fun onGetBlogByIdSuccess(item: Item)
    fun onGetBlogByIdFailure()
}

interface BlogSearchListener {
    fun onGetBlogSearchSuccess(list: List<Item>)
    fun onGetBlogSearchFailure()
}