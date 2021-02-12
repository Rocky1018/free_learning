package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.UpdateListener
import com.example.myapplication.R
import com.example.myapplication.bean.User
import com.example.myapplication.utils.Config
import com.example.myapplication.utils.MD5Util
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

        val bmobQuery: BmobQuery<User> = BmobQuery<User>()
        bmobQuery.getObject("Rflr222K", object : QueryListener<User>() {
            override fun done(obj: User?, e: BmobException?) {
                if (e == null) {
                    Config.user = obj
                    Log.d("bmobQuery", "e==null ${obj?.toString()}")
                } else {
                    Toast.makeText(this@AdminActivity, "获取管理员信息失败", Toast.LENGTH_SHORT)
                }
            }
        })
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
                    val user = User("admin")
                    user.password = MD5Util.getMD5Str(newValue.toString())
                    user.update("Rflr222K", object : UpdateListener() {
                        override fun done(e: BmobException?) {
                            if (e == null) {
                                Toast.makeText(activity, ("更新成功"), Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(activity, ("更新失败:${e.message}"), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    })
                    true
                }
            findPreference<Preference>("logout")?.onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    Config.user = null
                    SharePreferencesUtils.clear(context)
                    Toast.makeText(context, "退出登录", Toast.LENGTH_SHORT).show()
                    activity?.finish()
                    false
                }
        }
    }
}