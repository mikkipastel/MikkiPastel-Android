package com.mikkipastel.blog.manager

import com.mikkipastel.blog.model.BlogContent
import com.mikkipastel.blog.model.BlogData
import com.mikkipastel.blog.model.BlogItem
import com.mikkipastel.blog.model.GhostBlogModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlogPostPresenter: BlogPostInterface {

    override fun getAllPost(page: Int, hashtag: String?, listener: BlogPostListener) {
        val tag = if (hashtag == null) "" else "tag:$hashtag"
        val call = HttpManager().getApiService().getAllPost(page, tag)
        call.enqueue(object : Callback<GhostBlogModel> {
            override fun onResponse(call: Call<GhostBlogModel>, response: Response<GhostBlogModel>) {
                if (response.isSuccessful && response.body() != null) {
                    val items = response.body()
                    listener.onGetPostSuccess(items!!)
                } else {
                    listener.onGetPostFailure()
                }
            }

            override fun onFailure(call: Call<GhostBlogModel>?, t: Throwable?) {
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
    fun getAllPost(page: Int, hashtag: String?, listener: BlogPostListener)
    fun getBlogById(blogId: String, listener: BlogIdListener)
}

interface BlogPostListener {
    fun onGetPostSuccess(data: GhostBlogModel)
    fun onGetPostFailure()
}

interface BlogIdListener {
    fun onGetBlogByIdSuccess(item: BlogItem)
    fun onGetBlogByIdFailure()
}
