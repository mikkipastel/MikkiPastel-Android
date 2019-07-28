package com.mikkipastel.blog.fragment

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.mikkipastel.blog.R
import com.mikkipastel.blog.manager.BlogIdListener
import com.mikkipastel.blog.manager.BlogPostPresenter
import com.mikkipastel.blog.model.Item
import kotlinx.android.synthetic.main.fragment_content.*
import kotlinx.android.synthetic.main.layout_loading_error.*

class ContentFragment : Fragment(), BlogIdListener {

    private val blogId: String
        get() = arguments?.getString(BUNDLE_BLOG_ID) ?: ""

    private val blogTitle: String
        get() = arguments?.getString(BUNDLE_BLOG_TITLE) ?: ""

    companion object {

        private const val BUNDLE_BLOG_ID = "ContentFragment.BUNDLE_BLOG_ID"
        private const val BUNDLE_BLOG_TITLE = "ContentFragment.BUNDLE_BLOG_TITLE"

        fun newInstance(blogId: String, blogTitle: String) = ContentFragment().apply {
            arguments = Bundle().apply {
                putString(BUNDLE_BLOG_ID, blogId)
                putString(BUNDLE_BLOG_TITLE, blogTitle)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        textToolbar.text = blogTitle

        val fontsize = resources.getDimension(R.dimen.webview_text)

        webViewContent.apply {
            setInitialScale(1)
            settings.apply {
                loadWithOverviewMode = true
                useWideViewPort = true
                defaultFontSize = fontsize.toInt()
            }
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    progressLoading.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    progressLoading.visibility = View.GONE
                }
            }
        }

        getBlogContent()
    }

    private fun getBlogContent() {
        BlogPostPresenter().getBlogById(blogId, this)
    }

    override fun onGetBlogByIdSuccess(item: Item) {
        layoutError.visibility = View.GONE

        if (blogTitle.isEmpty()) {
            textToolbar.text = item.title
        }

        //?view=text
        webViewContent.loadDataWithBaseURL(
                null,
                item.content,
                "text/html",
                "utf-8",
                null
        )
    }

    override fun onGetBlogByIdFailure() {
        layoutError.visibility = View.VISIBLE
        buttonTryAgain.setOnClickListener {
            getBlogContent()
        }
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