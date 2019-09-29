package com.mikkipastel.blog.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mikkipastel.blog.R
import com.mikkipastel.blog.fragment.HashtagFragment

class HashtagActivity : AppCompatActivity() {

    private val hashtag: String
        get() = intent.extras.getString(BUNDLE_BLOG_HASHTAG) ?: ""

    companion object {
        private const val BUNDLE_BLOG_HASHTAG = "HashtagActivity.BUNDLE_BLOG_HASHTAG"

        fun newIntent(context: Context, hashtag: String) {
            val intent = Intent(context, HashtagActivity::class.java).apply {
                putExtra(BUNDLE_BLOG_HASHTAG, hashtag)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hashtag)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.contentContainer, HashtagFragment.newInstance(hashtag))
                    .commit()
        }
    }

    fun addHashtagListFragment(hashtag: String) {
        supportFragmentManager.beginTransaction()
                .add(R.id.contentContainer, HashtagFragment.newInstance(hashtag))
                .addToBackStack(null)
                .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}