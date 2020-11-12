package com.mikkipastel.blog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.mikkipastel.blog.fragment.SettingsFragment
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.settingsContainer, SettingsFragment())
                    .commit()
        }

        setToolbar()
    }

    private fun setToolbar() {
        val supportToolbar = toolbar as Toolbar
        setSupportActionBar(supportToolbar)
        supportToolbar.apply {
            navigationIcon = ContextCompat.getDrawable(
                    this@SettingsActivity,
                    R.drawable.ic_baseline_chevron_left_24
            )
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
        title = getString(R.string.action_settings)
    }
}