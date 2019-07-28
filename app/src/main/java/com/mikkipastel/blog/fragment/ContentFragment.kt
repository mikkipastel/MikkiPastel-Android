package com.mikkipastel.blog.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
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

        setToolbar()

        val fontsize = resources.getDimension(R.dimen.webview_text)

        webViewContent.apply {
            setInitialScale(1)
            settings.apply {
                loadWithOverviewMode = true
                useWideViewPort = true
                defaultFontSize = fontsize.toInt()
            }
        }

        BlogPostPresenter().getBlogById(blogId, this)
    }

    override fun onGetBlogByIdSuccess(item: Item) {
        textToolbar.text = item.title
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
        //TODO
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