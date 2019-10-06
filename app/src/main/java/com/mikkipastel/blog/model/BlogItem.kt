package com.mikkipastel.blog.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BlogItem(@SerializedName("id") val id: String?,
                    @SerializedName("url") val url: String?,
                    @SerializedName("title") val title: String?,
                    @SerializedName("label") val label: List<String>?,
                    @SerializedName("coverUrl") val coverUrl: String?,
                    @SerializedName("published") val published: String?,
                    @SerializedName("content") val content: String?,
                    @SerializedName("shortDescription") val shortDescription: String?): Parcelable