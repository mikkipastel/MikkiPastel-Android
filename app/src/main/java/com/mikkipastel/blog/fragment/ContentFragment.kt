package com.mikkipastel.blog.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikkipastel.blog.R
import com.mikkipastel.blog.manager.BlogIdListener
import com.mikkipastel.blog.manager.BlogPostPresenter
import com.mikkipastel.blog.model.Item
import kotlinx.android.synthetic.main.fragment_content.*

class ContentFragment : Fragment(), BlogIdListener {

    private val blogId: String
        get() = arguments?.getString(BUNDLE_BLOG_ID)!!

    companion object {

        private const val BUNDLE_BLOG_ID = "ContentFragment.BUNDLE_BLOG_ID"

        fun newInstance(blogId: String) = ContentFragment().apply {
            arguments = Bundle().apply {
                putString(BUNDLE_BLOG_ID, blogId)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fontsize = resources.getDimension(R.dimen.webview_text)

        webViewContent.setInitialScale(1)
        webViewContent.settings.apply {
            loadWithOverviewMode = true
            useWideViewPort = true
            defaultFontSize = fontsize.toInt()
        }

        BlogPostPresenter().getBlogById(blogId, this)
    }

    override fun onGetBlogByIdSuccess(item: Item) {
        webViewContent.loadDataWithBaseURL(null, item.content, "text/html", "utf-8", null)
    }

    override fun onGetBlogByIdFailure() {
        //
    }

}