package com.mikkipastel.blog.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mikkipastel.blog.R
import com.mikkipastel.blog.model.Item
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_content.*


class PostListAdapter(private val dataItems: List<Item>,
                      private val listener: PostItemListener)
    : RecyclerView.Adapter<PostListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false)
        return PostListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostListItemViewHolder, position: Int) {
        holder.apply {
            bind(dataItems[position])
            itemView.setOnClickListener {
                listener.onClick(dataItems[position], position)
            }
        }
    }

    override fun getItemCount() = dataItems.size

    interface PostItemListener {
        fun onClick(item: Item, position: Int)
    }
}

class PostListItemViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(item: Item) {
        Glide.with(containerView.context)
                .load(item.images[0].url)
                .placeholder(R.drawable.loading)
                .error(R.drawable.image_cover)
                .centerCrop()
                .fitCenter()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(cover_pic)

//        cover_pic.maxHeight = (cover_pic.measuredWidth * 2) / 3
//        val width = containerView.resources.displayMetrics.widthPixels
//        val height = (width * 2) / 3
//        cover_pic.layoutParams.height = height

        textPrimaryTopic.text = item.title
        textSecondary.text = item.content
        tag.text = item.labels.toString()

    }

}