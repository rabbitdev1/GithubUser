package com.rizalsujana.githubuser

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

object PreferenceUtil {

    private const val PREFERENCE_NAME = "MyPrefs"
    private const val DARK_MODE_KEY = "dark_mode_enabled"

    fun isDarkModeEnabled(context: Context): Boolean {
        val sharedPrefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean(DARK_MODE_KEY, false) // Menggunakan default value false
    }

    fun toggleDarkMode(context: Context) {
        val sharedPrefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val isDarkModeEnabled = isDarkModeEnabled(context)
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        editor.putBoolean(DARK_MODE_KEY, !isDarkModeEnabled)
        editor.apply()

        applyDarkMode(context)
    }

    fun applyDarkMode(context: Context) {
        if (isDarkModeEnabled(context)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
