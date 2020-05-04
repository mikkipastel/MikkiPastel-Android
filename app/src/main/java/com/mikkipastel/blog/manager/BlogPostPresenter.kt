package com.mikkipastel.blog.manager

import com.mikkipastel.blog.model.GhostBlogModel
import com.mikkipastel.blog.model.GhostTagsModel
import com.mikkipastel.blog.model.TagBlog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlogPostPresenter: BlogPostInterface {

    override fun getBlogPost(page: Int, hashtag: String?, listener: BlogPostListener) {
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

    override fun getBlogTag(listener: BlogTagListener) {
        val call = HttpManager().getApiService().getAllTags()
        call.enqueue(object : Callback<GhostTagsModel> {
            override fun onResponse(call: Call<GhostTagsModel>, response: Response<GhostTagsModel>) {
                if (response.isSuccessful && response.body() != null) {
                    val items = response.body()
                    listener.onGetTagSuccess(items?.tags!!)
                } else {
                    listener.onGetTagFailure()
                }
            }

            override fun onFailure(call: Call<GhostTagsModel>, t: Throwable) {
                listener.onGetTagFailure()
            }
        })
    }

}

interface BlogPostInterface {
    fun getBlogPost(page: Int, hashtag: String?, listener: BlogPostListener)
    fun getBlogTag(listener: BlogTagListener)
}

interface BlogPostListener {
    fun onGetPostSuccess(data: GhostBlogModel)
    fun onGetPostFailure()
}

interface BlogTagListener {
    fun onGetTagSuccess(data: MutableList<TagBlog>)
    fun onGetTagFailure()
}
