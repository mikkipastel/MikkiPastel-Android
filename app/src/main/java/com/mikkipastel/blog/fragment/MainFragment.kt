package com.mikkipastel.blog.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mikkipastel.blog.R
import com.mikkipastel.blog.activity.ContentActivity
import com.mikkipastel.blog.activity.HashtagActivity
import com.mikkipastel.blog.adapter.PostListAdapter
import com.mikkipastel.blog.manager.BlogPostListener
import com.mikkipastel.blog.manager.BlogPostPresenter
import com.mikkipastel.blog.model.BlogData
import com.mikkipastel.blog.model.BlogItem
import com.mikkipastel.blog.model.GhostBlogModel
import com.mikkipastel.blog.model.PostBlog
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_loading_error.*


class MainFragment : Fragment(), BlogPostListener, PostListAdapter.PostItemListener {

    private val mBlogList = mutableListOf<PostBlog>()
    private val mAdapter by lazy {
        PostListAdapter(mBlogList, this)
    }

    private var isLoading = false
    private var mPage = 1

    private var isReloadData = false
    private var canLazyLoading = true

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
            isReloadData = true
            loadPostData()
        }

        recyclerView.apply {
            val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            layoutManager = linearLayoutManager
            adapter = mAdapter
            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (canLazyLoading) {
                        val totalItemCount = linearLayoutManager.itemCount
                        val lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                        if (!isLoading && totalItemCount <= (lastVisibleItem + 1)) {
                            isLoading = true
                            mPage++
                            loadPostData()
                            lottieProgress.visibility = View.VISIBLE
                        }
                    }
                }
            })
        }
    }

    private fun loadPostData() {
        BlogPostPresenter().getAllPost(mPage, null, this)
    }

    override fun onGetPostSuccess(data: GhostBlogModel) {
        layoutError.visibility = View.GONE
        lottieLoading.visibility = View.GONE
        lottieProgress.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = false
        isLoading = false

        if (isReloadData) {
            mBlogList.clear()
            isReloadData = false
        }

        mBlogList.addAll(data.posts!!)
        mAdapter.notifyDataSetChanged()

        canLazyLoading = data.meta?.pagination?.next != null
    }

    override fun onGetPostFailure() {
        if (mBlogList.isEmpty()) {
            layoutError.visibility = View.VISIBLE
            lottieLoading.visibility = View.GONE
            buttonTryAgain.setOnClickListener {
                loadPostData()
            }
        }
        lottieProgress.visibility = View.GONE
    }

    override fun onClick(item: PostBlog, position: Int) {
//        ContentActivity.newIntent(context!!, item.id!!, item.title!!)
    }

    override fun onHashtagClick(hashtag: String) {
        HashtagActivity.newIntent(context!!, hashtag)
    }

}