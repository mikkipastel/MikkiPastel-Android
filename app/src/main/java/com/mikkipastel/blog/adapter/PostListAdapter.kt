package com.mikkipastel.blog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.mikkipastel.blog.R.*
import com.mikkipastel.blog.model.PostBlog
import com.mikkipastel.blog.model.TagBlog
import com.mikkipastel.blog.utils.ImageLoader
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_content.*

class PostListAdapter(
    private val dataItems: MutableList<PostBlog>,
    private val listener: PostItemListener
) :
    RecyclerView.Adapter<PostListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout.item_content, parent, false)
        return PostListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostListItemViewHolder, position: Int) {
        holder.apply {
            bind(dataItems[position], listener)
            itemView.setOnClickListener {
                listener.onContentClick(dataItems[position])
            }
        }
    }

    override fun getItemCount() = dataItems.size

    interface PostItemListener {
        fun onContentClick(item: PostBlog)
        fun onHashtagClick(hashtag: TagBlog)
    }
}

class PostListItemViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(item: PostBlog, hashtagListener: PostListAdapter.PostItemListener) {
        val params = imageCover.layoutParams
        val width = imageCover.resources.displayMetrics.widthPixels
        params.height = (width * 3) / 5
        imageCover.layoutParams = params

        ImageLoader().setBlogCover(containerView.context, item.feature_image, imageCover)

        textPrimaryTopic.text = item.title
        textSecondary.text = item.custom_excerpt?.replace("\n", "") ?: ""

        chipGroup.removeAllViews()

        item.tags?.let {
            if (it.isNotEmpty()) {
                it.forEach {
                    val chip = Chip(containerView.context)
                    chip.apply {
                        val tag = it
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        text = tag.name
                        setOnClickListener {
                            hashtagListener.onHashtagClick(tag)
                        }
                    }
                    chipGroup.addView(chip)
                }
            }
        }
    }
}
