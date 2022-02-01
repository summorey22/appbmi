package com.group4.sdp

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        window.statusBarColor =  ContextCompat.getColor(this, R.color.teal_200)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
        val sharedNameValue = sharedPreferences.getString("user_name","Not Found")

        findViewById<TextView>(R.id.textmain).text = "Hi! \n" + sharedNameValue.toString()

    }
}