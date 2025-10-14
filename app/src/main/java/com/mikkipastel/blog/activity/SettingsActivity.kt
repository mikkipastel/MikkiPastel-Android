package com.mikkipastel.blog.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.PreferenceManager
import com.mikkipastel.blog.R
import com.mikkipastel.blog.databinding.ActivitySettingsBinding
import com.mikkipastel.blog.fragment.SettingsFragment

class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val binding: ActivitySettingsBinding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootview) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(insets.left, insets.top, insets.right, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = true

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.settingsContainer, SettingsFragment())
                    .commit()
        }

        setToolbar()
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun setToolbar() {
        val supportToolbar = binding.toolbar
        setSupportActionBar(supportToolbar)
        supportToolbar.apply {
            navigationIcon = ContextCompat.getDrawable(
                    this@SettingsActivity,
                    R.drawable.ic_action_arrow_back
            )
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
        title = getString(R.string.action_settings)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        key?.let {
            if (it == "pref_dark_mode") {
                sharedPreferences?.let { pref ->
                    when (pref.getBoolean("pref_dark_mode", false)) {
                        true -> AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                        false -> AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                    }
                }
            }
        }
    }
}