package com.mikkipastel.blog.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.ArrayList

@Parcelize
data class GhostTagsModel(@SerializedName("tags") val tags: MutableList<TagBlog>) : Parcelable

@Parcelize
data class GhostBlogModel(
        @SerializedName("posts") val posts: MutableList<PostBlog>?,
        @SerializedName("meta") val meta: Meta?
) : Parcelable

@Parcelize
data class Meta(@SerializedName("pagination") val pagination: Pagination) : Parcelable

@Parcelize
data class PostBlog(
        @SerializedName("title") val title: String? = null,
        @SerializedName("feature_image") val featureImage: String? = null,
        @SerializedName("custom_excerpt") val customExcerpt: String? = null,
        @SerializedName("tags") val tags: ArrayList<TagBlog>? = null,
        @SerializedName("published_at") val publishedAt: String? = null,
        @SerializedName("url") val url: String? = null
) : Parcelable

@Parcelize
data class TagBlog(
        @SerializedName("id") val id: String? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("slug") val slug: String? = null,
        @SerializedName("description") val description: String? = null,
        @SerializedName("feature_image") val featureImage: String? = null,
        @SerializedName("visibility") val visibility: String? = null,
        @SerializedName("url") val url: String? = null
) : Parcelable

@Parcelize
data class Pagination(
        @SerializedName("page") val page: Int? = null,
        @SerializedName("limit") val limit: Int? = null,
        @SerializedName("pages") val pages: Int? = null,
        @SerializedName("total") val total: Int? = null,
        @SerializedName("next") val next: Int? = null,
        @SerializedName("prev") val prev: Int? = null
) : Parcelable