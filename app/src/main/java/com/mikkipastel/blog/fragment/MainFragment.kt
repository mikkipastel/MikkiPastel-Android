package com.mikkipastel.blog.fragment

import android.content.ComponentName
import android.os.Bundle
import android.support.customtabs.CustomTabsClient
import android.support.customtabs.CustomTabsServiceConnection
import android.support.customtabs.CustomTabsSession
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikkipastel.blog.R
import com.mikkipastel.blog.adapter.PostListAdapter
import com.mikkipastel.blog.manager.BlogPostListener
import com.mikkipastel.blog.manager.BlogPostPresenter
import com.mikkipastel.blog.model.Item
import com.mikkipastel.blog.utils.CustomChromeUtils
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment(), BlogPostListener, PostListAdapter.PostItemListener {

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

        swipeRefreshLayout.setOnRefreshListener {
            loadPostData()
        }

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

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

    override fun onResume() {
        super.onResume()
        loadPostData()
    }

    private fun loadPostData() {
        BlogPostPresenter().getAllPost(this)
    }

    override fun onGetAllPostSuccess(list: List<Item>) {
        swipeRefreshLayout.isRefreshing = false
        recyclerView.adapter = PostListAdapter(list, this)
    }

    override fun onClick(item: Item, position: Int) {
        CustomChromeUtils().setBlogWebpage(context!!, item.url!!, item.title!!)
    }

    override fun onGetAllPostFailure() {
        //TODO
    }


}