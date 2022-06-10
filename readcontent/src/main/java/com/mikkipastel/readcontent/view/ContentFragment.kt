package com.mikkipastel.readcontent.view

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.mikkipastel.readcontent.viewmodel.ContentViewModel
import com.mikkipastel.readcontent.databinding.FragmentContentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContentFragment: Fragment() {

    private lateinit var binding: FragmentContentBinding

    private val postId by lazy {
        arguments?.getString(BUNDLE_BLOG_ID) ?: ""
    }

    companion object {
        private const val BUNDLE_BLOG_ID = "ContentFragment.EXTRA_BLOG_ID"

        fun newInstance(postId: String) = ContentFragment().apply {
            arguments = Bundle().apply {
                putString(BUNDLE_BLOG_ID, postId)
            }
        }
    }

    private val contentViewModel: ContentViewModel by viewModel()

    private val mimeType: String = "text/html; charset=UTF-8"
    private val encodingType: String = "base64"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerData()
    }

    private fun initWebView(htmlText: String) {
        val encodedHtml = Base64.encodeToString(htmlText.toByteArray(), Base64.NO_PADDING)

        binding.webview.apply {
            webViewClient = object: WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    injectCss(view)
                    super.onPageFinished(view, url)
                }

                override fun onPageCommitVisible(view: WebView?, url: String?) {
                    injectCss(view)
                    super.onPageCommitVisible(view, url)
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    //WebViewActivity.newIntent(view?.context!!, request?.url.toString())
                    return true
                }

                @Suppress("DEPRECATION")
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    //WebViewActivity.newIntent(view?.context!!, url)
                    return true
                }
            }
            isVerticalScrollBarEnabled = false
            settings.apply {
                javaScriptEnabled = true
                loadWithOverviewMode = true
            }
            loadData(
                encodedHtml,
                mimeType,
                encodingType
            )
        }
    }

    private fun observerData() {
        contentViewModel.apply {
            getContent(postId)
            contentPost.observe(viewLifecycleOwner) {
                initWebView(it.html!!)
            }
            getContentError.observe(viewLifecycleOwner) {
                //
            }
        }
    }

    private fun injectCss(webview: WebView?) {
        try {
            val inputStream = requireContext().assets.open("style.css")
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            inputStream.close()
            val encoded = Base64.encodeToString(buffer , Base64.NO_WRAP)
            webview?.loadUrl(
                "javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "style.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(style)" +
                    "})()"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}