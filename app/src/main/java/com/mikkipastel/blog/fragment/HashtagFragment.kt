package com.mikkipastel.blog.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikkipastel.blog.R
import com.mikkipastel.blog.activity.ContentActivity
import com.mikkipastel.blog.activity.HashtagActivity
import com.mikkipastel.blog.adapter.PostListAdapter
import com.mikkipastel.blog.manager.BlogPostListener
import com.mikkipastel.blog.manager.BlogPostPresenter
import com.mikkipastel.blog.model.BlogData
import com.mikkipastel.blog.model.BlogItem
import kotlinx.android.synthetic.main.fragment_hashtag.*
import kotlinx.android.synthetic.main.layout_loading_error.*

class HashtagFragment : Fragment(), BlogPostListener, PostListAdapter.PostItemListener {

    private val hashtag: String
        get() = arguments?.getString(BUNDLE_BLOG_HASHTAG) ?: ""

    private val mBlogList = arrayListOf<BlogItem>()
    private val mAdapter by lazy {
        PostListAdapter(mBlogList, this)
    }

    private var isLoading = false
    private var mPage = 0

    private var lastPublished = ""
    private var isReloadData = false

    companion object {

        private const val BUNDLE_BLOG_HASHTAG = "ContentFragment.BUNDLE_BLOG_HASHTAG"

        fun newInstance(hashtag: String) = HashtagFragment().apply {
            arguments = Bundle().apply {
                putString(BUNDLE_BLOG_HASHTAG, hashtag)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_hashtag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()

        loadHashtagPostData()

        textToolbar.text = hashtag

        swipeRefreshLayout.setOnRefreshListener {
            isReloadData = true
            loadHashtagPostData()
        }

        recyclerView.apply {
            val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            layoutManager = linearLayoutManager
            adapter = mAdapter
            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    val totalItemCount = linearLayoutManager.itemCount
                    val lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                    if (!isLoading && totalItemCount <= (lastVisibleItem + 1)) {
                        isLoading = true
                        mPage++
                        loadHashtagPostData()
                        lottieProgress.visibility = View.VISIBLE
                    }
                }
            })
        }

    }

    private fun loadHashtagPostData() {
        BlogPostPresenter().getPostByTag(hashtag, lastPublished, this)
    }

    override fun onGetPostSuccess(data: BlogData) {
        lastPublished = data.lastPublished ?: ""

        layoutError.visibility = View.GONE
        lottieLoading.visibility = View.GONE
        lottieProgress.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = false
        isLoading = false

        if (isReloadData) {
            mBlogList.clear()
            isReloadData = false
        }

        mBlogList.addAll(data.items)
        mAdapter.notifyDataSetChanged()
    }

    override fun onGetPostFailure() {
        if (mBlogList.isEmpty()) {
            layoutError.visibility = View.VISIBLE
            lottieLoading.visibility = View.GONE
            buttonTryAgain.setOnClickListener {
                loadHashtagPostData()
            }
        }
        lottieProgress.visibility = View.GONE
    }

    override fun onClick(item: BlogItem, position: Int) {
        ContentActivity.newIntent(context!!, item.id!!, item.title!!)
    }

    override fun onHashtagClick(hashtag: String) {
        (activity as HashtagActivity).addHashtagListFragment(hashtag)
    }

    private fun setToolbar() {
        val supportToolbar = toolbar as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(supportToolbar)

        supportToolbar.apply {
            navigationIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_action_arrow_left)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }

        activity?.title = ""
    }
}