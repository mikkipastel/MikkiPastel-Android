package com.mikkipastel.blog.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class MikkiBlog(@SerializedName("kind") val kind: String?,
                     @SerializedName("nextPageToken") val nextPageToken: String?,
                     @SerializedName("items") val items: List<Item>,
                     @SerializedName("etag") val etag: String?): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            listOf<Item>().apply {
                parcel.readList(this, Item::class.java.classLoader)
            },
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(kind)
        parcel.writeString(nextPageToken)
        parcel.writeList(items)
        parcel.writeString(etag)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MikkiBlog> {
        override fun createFromParcel(parcel: Parcel): MikkiBlog {
            return MikkiBlog(parcel)
        }

        override fun newArray(size: Int): Array<MikkiBlog?> {
            return arrayOfNulls(size)
        }
    }
}