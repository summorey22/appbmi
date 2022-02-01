package com.group4.sdp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.statusBarColor =  ContextCompat.getColor(this, R.color.emerald)

        auth = FirebaseAuth.getInstance()

        val currentuser = auth.currentUser
        if(currentuser!= null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<CardView>(R.id.signin).setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        findViewById<CardView>(R.id.register).setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}