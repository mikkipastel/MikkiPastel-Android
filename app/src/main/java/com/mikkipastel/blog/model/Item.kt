package com.mikkipastel.blog.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Item(@SerializedName("kind") val king: String?,
                @SerializedName("id") val id: String?,
                @SerializedName("blog") val blog: Blog,
                @SerializedName("published") val published: String?,
                @SerializedName("updated") val updated: String?,
                @SerializedName("etag") val etag: String?,
                @SerializedName("url") val url: String?,
                @SerializedName("selfLink") val selfLink: String?,
                @SerializedName("title") val title: String?,
                @SerializedName("content") val content: String?,
                @SerializedName("images") val images: List<Image>,
                @SerializedName("labels") val labels: List<String>?): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Blog::class.java.classLoader),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(Image),
            parcel.createStringArrayList())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(king)
        parcel.writeString(id)
        parcel.writeParcelable(blog, flags)
        parcel.writeString(published)
        parcel.writeString(updated)
        parcel.writeString(etag)
        parcel.writeString(url)
        parcel.writeString(selfLink)
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeTypedList(images)
        parcel.writeStringList(labels)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}