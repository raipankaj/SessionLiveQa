package session.hyderabad.live.sessionliveqa.utils

import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity

fun AppCompatActivity.saveString(key: String, value: String) {
    PreferenceManager.getDefaultSharedPreferences(this).edit().putString(key, value).apply()
}

fun AppCompatActivity.saveBoolean(key: String, value: Boolean) {
    PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(key, value).apply()
}

fun AppCompatActivity.getSavedString(key: String): String? {
    return PreferenceManager.getDefaultSharedPreferences(this).getString(key, "")
}

fun AppCompatActivity.getSavedBoolean(key: String): Boolean {
    return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(key, false)
}


fun AppCompatActivity.clearAllSavedData() {
    PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply()
}