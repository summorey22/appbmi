package com.group4.sdp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {

    public val userData = "USER_DATA"

    lateinit var sharedPreferences: SharedPreferences

    lateinit var auth: FirebaseAuth
    lateinit var prgBr: ProgressBar
    var databaseReference : DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        window.statusBarColor =  ContextCompat.getColor(this, R.color.orenga)

        prgBr= findViewById(R.id.prgbr)

        prgBr.isVisible = false

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        sharedPreferences = this.getSharedPreferences(userData, Context.MODE_PRIVATE)

        auth = FirebaseAuth.getInstance()

        findViewById<ImageButton>(R.id.back).setOnClickListener {
            this.finish()
        }


        findViewById<TextView>(R.id.regsignin).setOnClickListener(){

            val intent = Intent(this,Login::class.java)
            startActivity(intent)

        }

        register()
    }

    private fun register() {

        val emailya: EditText = findViewById(R.id.regemail)
        val fname: EditText = findViewById(R.id.regfirstnm)
        val pass: EditText = findViewById(R.id.regpass)
        val text3: EditText = findViewById(R.id.middlenm)
        val text4: EditText = findViewById(R.id.reglasnm)

        findViewById<FloatingActionButton>(R.id.submitregcard).setOnClickListener() {
            when {
                TextUtils.isEmpty(emailya.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(fname.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter first name", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(text3.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter middle name", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(text4.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter last name", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(pass.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                }
                else -> {

                    prgBr.isVisible = true

                    auth.createUserWithEmailAndPassword(emailya.text.toString(),pass.text.toString())
                        .addOnCompleteListener{

                            if(it.isSuccessful){

                                val currentUser = auth.currentUser
                                val currentUserDb = databaseReference?.child((currentUser?.uid!!))
                                val editor:SharedPreferences.Editor =  sharedPreferences.edit()
                                editor.putString("user_name",fname.text.toString())
                                editor.putString("last_name", text4.text.toString())
                                editor.apply()
                                editor.commit()
                                auth.signInWithEmailAndPassword(emailya.text.toString(), pass.text.toString())
                                Toast.makeText(this,"Registration Succesful", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()

                            }
                            else{
                                Toast.makeText(this,"Registration Failed", Toast.LENGTH_SHORT).show()
                            }
                        }

                }
            }
        }
    }
}