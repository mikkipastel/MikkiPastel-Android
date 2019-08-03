package com.mikkipastel.blog.fragment

import android.content.ComponentName
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mikkipastel.blog.R
import com.mikkipastel.blog.activity.ContentActivity
import com.mikkipastel.blog.adapter.PostListAdapter
import com.mikkipastel.blog.manager.BlogPostListener
import com.mikkipastel.blog.manager.BlogPostPresenter
import com.mikkipastel.blog.model.Item
import com.mikkipastel.blog.utils.CustomChromeUtils
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_loading_error.*


class MainFragment : Fragment(), BlogPostListener, PostListAdapter.PostItemListener, PostListAdapter.HashtagListener {

    private lateinit var mCustomTabsServiceConnection: CustomTabsServiceConnection
    internal lateinit var mCustomTabsClient: CustomTabsClient
    internal lateinit var mCustomTabsSession: CustomTabsSession

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

        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        mCustomTabsServiceConnection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(componentName: ComponentName, customTabsClient: CustomTabsClient) {
                mCustomTabsClient = customTabsClient
                mCustomTabsClient.warmup(0L)
                mCustomTabsSession = mCustomTabsClient.newSession(null)
            }

            override fun onServiceDisconnected(name: ComponentName) {
                // mCustomTabsClient = null;
            }
        }

    }

    private fun loadPostData() {
        BlogPostPresenter().getAllPost(this)
    }

    override fun onGetAllPostSuccess(list: List<Item>) {
        layoutError.visibility = View.GONE
        lottieLoading.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = false
        recyclerView.adapter = PostListAdapter(list, this, this)
    }

    override fun onClick(item: Item, position: Int) {
//        ContentActivity.newIntent(context!!, item.id!!, item.title!!)
        CustomChromeUtils().setBlogWebpage(
                context!!,
                item.url!!,
                item.title!!
        )
    }

    override fun onGetAllPostFailure() {
        layoutError.visibility = View.VISIBLE
        lottieLoading.visibility = View.GONE
        buttonTryAgain.setOnClickListener {
            loadPostData()
        }
    }

    override fun onHashtagClick(hashtag: String) {
        //TODO
    }

}