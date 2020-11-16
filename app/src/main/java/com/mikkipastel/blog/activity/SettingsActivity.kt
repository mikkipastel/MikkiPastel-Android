package com.mikkipastel.blog.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.mikkipastel.blog.R
import com.mikkipastel.blog.fragment.SettingsFragment
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

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