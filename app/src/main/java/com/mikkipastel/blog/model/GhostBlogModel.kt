package com.mikkipastel.blog.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.mikkipastel.blog.dao.BlogTagDatabase
import com.mikkipastel.blog.dao.blogContentTable
import com.mikkipastel.blog.dao.blogTagTable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GhostTagsModel(@SerializedName("tags") val tags: MutableList<TagBlog>) : Parcelable

@Parcelize
data class GhostBlogModel(
        @SerializedName("posts") val posts: MutableList<PostBlog>?,
        @SerializedName("meta") val meta: Meta?
) : Parcelable

@Parcelize
data class Meta(@SerializedName("pagination") val pagination: Pagination) : Parcelable

@Entity(tableName = blogContentTable)
@Parcelize
data class PostBlog(
        @PrimaryKey(autoGenerate = true) val primaryKey: Int,
        @SerializedName("title") val title: String?,
        @SerializedName("feature_image") val feature_image: String?,
        @SerializedName("custom_excerpt") val custom_excerpt: String?,
        @Embedded @SerializedName("tags") val tags: ArrayList<TagBlog>?,
        @SerializedName("published_at") val published_at: String?,
        @SerializedName("url") val url: String?
) : Parcelable

@Entity(tableName = blogTagTable)
@Parcelize
data class TagBlog(
        @PrimaryKey(autoGenerate = true) val primaryKey: Int,
        @SerializedName("id") val id: String?,
        @SerializedName("name") val name: String?,
        @SerializedName("slug") val slug: String?,
        @SerializedName("description") val description: String?,
        @SerializedName("feature_image") val feature_image: String?,
        @SerializedName("visibility") val visibility: String?,
        @SerializedName("url") val url: String?
) : Parcelable

@Parcelize
data class Pagination(
        @SerializedName("page") val page: Int?,
        @SerializedName("limit") val limit: Int?,
        @SerializedName("pages") val pages: Int?,
        @SerializedName("total") val total: Int?,
        @SerializedName("next") val next: Int?,
        @SerializedName("prev") val prev: Int?
) : Parcelable