package com.mikkipastel.blog.manager

import com.mikkipastel.blog.model.Item
import com.mikkipastel.blog.model.MikkiBlog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlogPostPresenter: BlogPostInterface {

    override fun getAllPost(listener: BlogPostListner) {
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

}

interface BlogPostInterface {
    fun getAllPost(listener: BlogPostListner)
}

interface BlogPostListner {
    fun onGetAllPostSuccess(list: List<Item>)
    fun onGetAllPostFailure()
}