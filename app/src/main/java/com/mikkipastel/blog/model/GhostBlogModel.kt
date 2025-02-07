package com.mikkipastel.blog.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.mikkipastel.blog.dao.blogContentTable
import com.mikkipastel.blog.dao.blogTagTable
import kotlinx.parcelize.Parcelize

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
        @SerializedName("title") val title: String? = null,
        @SerializedName("feature_image") val featureImage: String? = null,
        @SerializedName("custom_excerpt") val customExcerpt: String? = null,
        @Embedded @SerializedName("tags") val tags: ArrayList<TagBlog>? = null,
        @SerializedName("published_at") val publishedAt: String? = null,
        @SerializedName("url") val url: String? = null
) : Parcelable

@Entity(tableName = blogTagTable)
@Parcelize
data class TagBlog(
        @PrimaryKey(autoGenerate = true) val primaryKey: Int,
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