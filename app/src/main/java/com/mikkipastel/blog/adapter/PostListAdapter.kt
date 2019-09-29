package com.mikkipastel.blog.adapter

import com.google.android.material.chip.Chip
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_content.*
import com.mikkipastel.blog.R.*
import com.mikkipastel.blog.model.BlogItem


class PostListAdapter(private val dataItems: List<BlogItem>,
                      private val listener: PostItemListener)
    : RecyclerView.Adapter<PostListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout.item_content, parent, false)
        return PostListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostListItemViewHolder, position: Int) {
        holder.apply {
            bind(dataItems[position], listener)
            itemView.setOnClickListener {
                listener.onClick(dataItems[position], position)
            }
        }
    }

    override fun getItemCount() = dataItems.size

    interface PostItemListener {
        fun onClick(item: BlogItem, position: Int)
        fun onHashtagClick(hashtag: String)
    }
}

class PostListItemViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(item: BlogItem, hashtagListener: PostListAdapter.PostItemListener) {
        Glide.with(containerView.context)
                .load(item.coverUrl)
                .placeholder(drawable.loading)
                .error(drawable.image_cover)
                .centerCrop()
                .fitCenter()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageCover)

        val params = imageCover.layoutParams
        val width = imageCover.resources.displayMetrics.widthPixels
        params.height = (width * 2) / 3
        imageCover.layoutParams = params

        textPrimaryTopic.text = item.title

        textSecondary.text = item.shortDescription!!

        chipGroup.removeAllViews()
        if (item.label!!.isNotEmpty()) {
            item.label.forEach {
                val chip = Chip(containerView.context)
                chip.apply {
                    val label = it
                    layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                    text = label
                    setOnClickListener {
                        hashtagListener.onHashtagClick(label)
                    }
                }
                chipGroup.addView(chip)
            }
        }
    }
}