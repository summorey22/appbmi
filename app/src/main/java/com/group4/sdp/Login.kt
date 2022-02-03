package com.group4.sdp

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    var databaseReference : DatabaseReference? = null
    var database: FirebaseDatabase? = null
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.statusBarColor =  ContextCompat.getColor(this, R.color.pink)


        findViewById<ImageButton>(R.id.back1).setOnClickListener {
            this.finish()
        }

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

        val reg: TextView = findViewById(R.id.textView)

        reg.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        findViewById<FloatingActionButton>(R.id.submitsigin).setOnClickListener(){

            prgbr.isVisible=true

            when {
                TextUtils.isEmpty(emailyaa.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
                    prgbr.isVisible=false
                }

                TextUtils.isEmpty(passs.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                    txtpass.isVisible=true
                    prgbr.isVisible=false
                }

                else -> {

                    auth.signInWithEmailAndPassword(emailyaa.text.toString(),passs.text.toString())
                        .addOnCompleteListener{

                            prgbr.isVisible=false

                            if(it.isSuccessful){
                                val intent = Intent(this,MainActivity::class.java)
                                val currentUser = auth.currentUser
                                val db = Firebase.firestore
                                db.collection("profile").document(currentUser?.uid.toString()).get()
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful)
                                        {
                                            val document = task.result
                                            val buff = document?.data?.get("fname")
                                            sharedPreferences = this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
                                            val editor: SharedPreferences.Editor =  sharedPreferences.edit()
                                            editor.putString("user_name",buff.toString())
                                            editor.apply()
                                            editor.commit()

                                        }
                                    }
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