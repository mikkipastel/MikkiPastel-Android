package com.mikkipastel.blog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.mikkipastel.blog.databinding.ItemContentBinding
import com.mikkipastel.blogservice.model.PostBlog
import com.mikkipastel.blogservice.model.TagBlog
import com.mikkipastel.blog.utils.ImageLoader

class PostListAdapter(
    private val dataItems: MutableList<PostBlog>,
    private val listener: PostItemListener
) :
    RecyclerView.Adapter<PostListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListItemViewHolder {
        return PostListItemViewHolder(
            ItemContentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
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

class PostListItemViewHolder(
    private val itemBinding: ItemContentBinding
) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(item: PostBlog, hashtagListener: PostListAdapter.PostItemListener) {
        itemBinding.apply {
            val params = imageCover.layoutParams
            val width = imageCover.resources.displayMetrics.widthPixels
            params.height = (width * 3) / 5
            imageCover.layoutParams = params

            ImageLoader().setBlogCover(root.context, item.feature_image, imageCover)

            textPrimaryTopic.text = item.title
            textSecondary.text = item.custom_excerpt?.replace("\n", "") ?: ""

            chipGroup.removeAllViews()

            item.tags?.let {
                if (it.isNotEmpty()) {
                    it.forEach {
                        val chip = Chip(root.context)
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
}
