package com.mikkipastel.blog.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mikkipastel.blog.R

internal class ImageLoader {

    fun setBlogCover(context: Context, url: String?, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.placeholder_loading)
            .error(R.drawable.placeholder_blog)
            .apply(RequestOptions.fitCenterTransform())
            .into(imageView)
    }

    fun setTagCover(context: Context, url: String?, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.placeholder_loading)
            .error(R.drawable.placeholder_cover)
            .apply(RequestOptions.fitCenterTransform())
            .into(imageView)
    }
}
