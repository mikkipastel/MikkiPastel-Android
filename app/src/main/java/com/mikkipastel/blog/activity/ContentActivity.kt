package com.mikkipastel.blog.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mikkipastel.blog.R
import com.mikkipastel.blog.fragment.ContentFragment

class ContentActivity : AppCompatActivity() {

    companion object {

        private const val BUNDLE_BLOG_ID = "ContentActivity.BUNDLE_BLOG_ID"
        private const val BUNDLE_BLOG_TITLE = "ContentActivity.BUNDLE_BLOG_TITLE"

        fun newIntent(context: Context, blogId: String, blogTitle: String) {
            val intent = Intent(context, ContentActivity::class.java).apply {
                putExtra(BUNDLE_BLOG_ID, blogId)
                putExtra(BUNDLE_BLOG_TITLE, blogTitle)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        val blogId = intent.extras.getString(BUNDLE_BLOG_ID) ?: ""
        val blogTitle = intent.extras.getString(BUNDLE_BLOG_TITLE) ?: ""

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.contentWebview, ContentFragment.newInstance(blogId, blogTitle))
                    .commit()
        }
    }
}