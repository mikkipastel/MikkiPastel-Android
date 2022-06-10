package com.mikkipastel.readcontent.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mikkipastel.readcontent.R
import com.mikkipastel.readcontent.databinding.ActivityContentBinding

class ContentActivity : AppCompatActivity() {

    private val postId by lazy {
        intent.getStringExtra(EXTRA_BLOG_ID) ?: ""
    }

    companion object {
        private const val EXTRA_BLOG_ID = "ContentActivity.EXTRA_BLOG_ID"

        fun newIntent(context: Context, url: String?) {
            val intent = Intent(context, ContentActivity::class.java).apply {
                putExtra(EXTRA_BLOG_ID, url)
            }
            context.startActivity(intent)
        }
    }

    private val binding: ActivityContentBinding by lazy {
        ActivityContentBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.contentContainer, ContentFragment.newInstance(postId))
                .commit()
        }
    }
}