package com.aelahmar.languageswitcher

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class PreferenceUtil(private val mContext: Context) {

    private val defaultEditor: SharedPreferences.Editor
        get() = defaultSharedPreferences.edit()

    private val defaultSharedPreferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(mContext)

    fun getStringValue(key: String, defaultValue: String = ""): String {
        val value = defaultSharedPreferences.getString(key, defaultValue)
        return if (value.isNullOrEmpty()) {
            ""
        } else {
            value
        }
    }

    fun setStringValue(key: String, value: String) {
        val editor = defaultEditor
        editor.putString(key, value)
        editor.commit()
    }

    fun clear() {
        defaultEditor.clear().commit()
    }
}