package com.group4.sdp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val txtpass: TextView = findViewById(R.id.loginforpass)
        val prgbr: ProgressBar = findViewById(R.id.progressBar)

        txtpass.isVisible=false

        txtpass.setOnClickListener(){
            Toast.makeText(this,"Mail us at abc@gmail.com", Toast.LENGTH_SHORT).show()
        }

        prgbr.isVisible=false

        auth = FirebaseAuth.getInstance()
        login()

    }

    private fun login(){

        val emailyaa: EditText = findViewById(R.id.signinuser)
        val passs: EditText = findViewById(R.id.signinpass)
        val prgbr: ProgressBar = findViewById(R.id.progressBar)
        val txtpass: TextView = findViewById(R.id.loginforpass)

        findViewById<CardView>(R.id.submitsigin).setOnClickListener(){ view ->

            prgbr.isVisible=true

            when {
                TextUtils.isEmpty(emailyaa.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(passs.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                    txtpass.isVisible=true
                }

                else -> {

                    auth.signInWithEmailAndPassword(emailyaa.text.toString(),passs.text.toString())
                        .addOnCompleteListener{

                            prgbr.isVisible=false

                            if(it.isSuccessful){

                                val intent = Intent(this,MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }
                            else{
                                Toast.makeText(this,it.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                            }

                        }

                }

            }

        }

    }


}