package com.mikkipastel.blog.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GhostTagsModel(@SerializedName("tags") val tags: MutableList<TagBlog>): Parcelable

@Parcelize
data class GhostBlogModel(@SerializedName("posts") val posts: MutableList<PostBlog>?,
                          @SerializedName("meta") val meta: Meta?): Parcelable

@Parcelize
data class Meta(@SerializedName("pagination") val pagination: Pagination): Parcelable

@Parcelize
data class PostBlog(@SerializedName("title") val title: String?,
                    @SerializedName("feature_image") val feature_image: String?,
                    @SerializedName("custom_excerpt") val custom_excerpt: String?,
                    @SerializedName("tags") val tags: MutableList<TagBlog>?,
                    @SerializedName("published_at") val published_at: String?,
                    @SerializedName("url") val url: String?): Parcelable

@Parcelize
data class TagBlog(@SerializedName("id") val id: String?,
                   @SerializedName("name") val name: String?,
                   @SerializedName("slug") val slug: String?,
                   @SerializedName("description") val description: String?,
                   @SerializedName("feature_image") val feature_image: String?,
                   @SerializedName("visibility") val visibility: String?,
                   @SerializedName("url") val url: String?): Parcelable

@Parcelize
data class Pagination(@SerializedName("page") val page: Int?,
                      @SerializedName("limit") val limit: Int?,
                      @SerializedName("pages") val pages: Int?,
                      @SerializedName("total") val total: Int?,
                      @SerializedName("next") val next: Int?,
                      @SerializedName("prev") val prev: Int?): Parcelable