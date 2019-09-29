package com.mikkipastel.blog.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mikkipastel.blog.MainActivity
import com.mikkipastel.blog.R
import com.mikkipastel.blog.activity.ContentActivity
import com.mikkipastel.blog.activity.HashtagActivity
import com.mikkipastel.blog.adapter.PostListAdapter
import com.mikkipastel.blog.manager.BlogPostListener
import com.mikkipastel.blog.manager.BlogPostPresenter
import com.mikkipastel.blog.model.BlogItem
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_loading_error.*


class MainFragment : Fragment(), BlogPostListener, PostListAdapter.PostItemListener {

    private val mBlogList = arrayListOf<BlogItem>()
    private val mAdapter by lazy {
        PostListAdapter(mBlogList, this)
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadPostData()

        swipeRefreshLayout.setOnRefreshListener {
            loadPostData()
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }
    }

    private fun loadPostData() {
        BlogPostPresenter().getAllPost(this)
    }

    override fun onGetAllPostSuccess(list: List<BlogItem>) {
        layoutError.visibility = View.GONE
        lottieLoading.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = false

        if (mBlogList.size == 0) {
            mBlogList.addAll(list)
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun onGetAllPostFailure() {
        layoutError.visibility = View.VISIBLE
        lottieLoading.visibility = View.GONE
        buttonTryAgain.setOnClickListener {
            loadPostData()
        }
    }

    override fun onClick(item: BlogItem, position: Int) {
        ContentActivity.newIntent(context!!, item.id!!, item.title!!)
    }

    override fun onHashtagClick(hashtag: String) {
        HashtagActivity.newIntent(context!!, hashtag)
    }

}