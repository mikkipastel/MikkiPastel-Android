package com.mikkipastel.blog.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import com.mikkipastel.blog.R.*
import kotlinx.android.synthetic.main.item_hashtag.*


class HashtagChipAdapter(private val labelsList: List<String>?,
                      private val listener: HashtagListener)
    : RecyclerView.Adapter<HashtagChipViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HashtagChipViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout.item_hashtag, parent, false)
        return HashtagChipViewHolder(view)
    }

    override fun onBindViewHolder(holder: HashtagChipViewHolder, position: Int) {
        holder.apply {
            if (labelsList?.isNotEmpty()!!) {
                bind(labelsList[position])
                itemView.setOnClickListener {
                    listener.onClick(labelsList[position], position)
                }
            }

        }
    }

    override fun getItemCount() = labelsList?.size!!

    interface HashtagListener {
        fun onClick(hashtag: String, position: Int)
    }
}

class HashtagChipViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(item: String) {
        textHashtag.text = item
    }

}