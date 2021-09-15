package com.mikkipastel.blog.fragment

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.mikkipastel.blog.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_setting, rootKey)

        val openSourcePreference = findPreference<Preference>("open_source_menu")
        openSourcePreference?.setOnPreferenceClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    OssLicensesMenuActivity::class.java)
            )
            true
        }
    }
}