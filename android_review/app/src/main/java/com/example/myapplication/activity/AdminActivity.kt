package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.myapplication.R
import com.example.myapplication.utils.SharePreferencesUtils

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            findPreference<Preference>("user_info")?.onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    startActivity(Intent(Settings.ACTION_DISPLAY_SETTINGS))
                    false
                }
            findPreference<EditTextPreference>("password")?.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { preference, newValue ->
                    Log.d("terminal_number", "preference:$preference,newValue:$newValue")
                    //SharePreferencesUtils.setParam(activity, "terminal_number", newValue.toString())
                    true
                }
        }
    }
}