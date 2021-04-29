package com.example.login.configuration

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.example.login.R

class PrefConfig(private val context: Context) {
    private val sharedPreferences: SharedPreferences
    fun writeLoginStatus(status: String) {
        val editor = sharedPreferences.edit()
        editor.putString(context.getString(R.string.pref_login_status), status)
        editor.commit()
    }

    fun readLoginStatus(): String? {
        return sharedPreferences.getString(context.getString(R.string.pref_login_status), "loggedOut")
    }

    fun writeUsername(name: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(context.getString(R.string.pref_user_name), name)
        editor.commit()
    }

    fun readUsername(): String? {
        return sharedPreferences.getString(context.getString(R.string.pref_user_name), "Username")
    }

    fun writeName(name: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(context.getString(R.string.pref_name), name)
        editor.commit()
    }

    fun readName(): String? {
        return sharedPreferences.getString(context.getString(R.string.pref_name), "User")
    }

    fun writeLock(lock: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(context.getString(R.string.pref_lock),lock)
        editor.commit()
    }

    fun readLock(): String? {
        return sharedPreferences.getString(context.getString(R.string.pref_lock), null)
    }

    fun displayToast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    init {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file), Context.MODE_PRIVATE)
    }
}