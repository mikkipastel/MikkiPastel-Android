package com.mikkipastel.blog.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.mikkipastel.blog.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_setting, rootKey)
    }
}