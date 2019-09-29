package com.mikkipastel.blog.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.webkit.WebView
import android.webkit.WebViewClient
import com.mikkipastel.blog.R
import com.mikkipastel.blog.manager.BlogIdListener
import com.mikkipastel.blog.manager.BlogPostPresenter
import com.mikkipastel.blog.model.BlogItem
import kotlinx.android.synthetic.main.fragment_content.*
import kotlinx.android.synthetic.main.layout_loading_error.*
import android.content.Intent

class ContentFragment : Fragment(), BlogIdListener {

    private val blogId: String
        get() = arguments?.getString(BUNDLE_BLOG_ID) ?: ""

    private val blogTitle: String
        get() = arguments?.getString(BUNDLE_BLOG_TITLE) ?: ""

    private var blogUrl = ""

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
        setHasOptionsMenu(true)
        textToolbar.text = blogTitle

        val fontsize = resources.getDimension(com.mikkipastel.blog.R.dimen.webview_text)

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

    override fun onGetBlogByIdSuccess(item: BlogItem) {
        layoutError.visibility = View.GONE

        blogUrl = getString(R.string.template_web_blog_url, item.url)
        textLinkUrl.text = blogUrl

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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_share, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_share) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "$blogTitle $blogUrl")
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.title_share_blog)))
        }
        return super.onOptionsItemSelected(item)
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